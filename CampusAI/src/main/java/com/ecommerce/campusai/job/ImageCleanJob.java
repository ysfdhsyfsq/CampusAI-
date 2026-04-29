package com.ecommerce.campusai.job;

import com.ecommerce.campusai.mapper.DishMapper;
import com.ecommerce.campusai.mapper.MerchantMapper;
import com.ecommerce.campusai.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@EnableScheduling
public class ImageCleanJob {

    @Autowired
    private DishMapper dishMapper;
    
    @Autowired
    private MerchantMapper merchantMapper;
    
    @Autowired
    private UserMapper userMapper;

    private static final String UPLOAD_FOLDER = System.getProperty("user.dir") + "/src/main/resources/static/upload/";

    @Scheduled(cron = "0 0 2 * * ?")
    public void cleanUnusedImages() {
        System.out.println("开始清理孤儿图片...");
        
        File dir = new File(UPLOAD_FOLDER);
        if (!dir.exists()) {
            System.out.println("上传目录不存在");
            return;
        }

        File[] files = dir.listFiles();
        if (files == null || files.length == 0) {
            System.out.println("没有图片文件");
            return;
        }

        Set<String> usedImages = getAllUsedImages();
        int deletedCount = 0;

        for (File file : files) {
            String fileName = file.getName();
            if (!usedImages.contains(fileName)) {
                if (file.delete()) {
                    deletedCount++;
                    System.out.println("已删除孤儿图片: " + fileName);
                }
            }
        }

        System.out.println("清理完成，共删除 " + deletedCount + " 张孤儿图片");
    }

    private Set<String> getAllUsedImages() {
        Set<String> usedImages = new HashSet<>();

        try {
            List<String> dishImages = dishMapper.getAllImagePaths();
            usedImages.addAll(dishImages);

            List<String> merchantLogos = merchantMapper.getAllLogoPaths();
            usedImages.addAll(merchantLogos);

            List<String> userAvatars = userMapper.getAllAvatarPaths();
            usedImages.addAll(userAvatars);
        } catch (Exception e) {
            System.err.println("获取已使用图片列表失败: " + e.getMessage());
        }

        return usedImages;
    }
}