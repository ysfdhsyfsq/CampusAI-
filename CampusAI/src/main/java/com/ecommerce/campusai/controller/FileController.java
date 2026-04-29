package com.ecommerce.campusai.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/file")
public class FileController {

    private static final String UPLOAD_DIR =
            System.getProperty("user.dir")
                    + "/src/main/resources/static/upload/";

    @PostMapping("/upload")
    public Map<String, Object> upload(MultipartFile file) {
        Map<String, Object> map = new HashMap<>();
        try {
            File dir = new File(UPLOAD_DIR);
            if (!dir.exists()) dir.mkdirs();

            String originalFilename = file.getOriginalFilename();
            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            String fileName = UUID.randomUUID().toString().replace("-", "") + "_" 
                    + System.currentTimeMillis() + extension;

            File dest = new File(dir, fileName);
            file.transferTo(dest);

            map.put("code", 200);
            map.put("msg", "上传成功");
            map.put("url", "/upload/" + fileName);
            map.put("fileName", fileName);

        } catch (IOException e) {
            e.printStackTrace();
            map.put("code", 500);
            map.put("msg", "上传失败: " + e.getMessage());
        }
        return map;
    }
}