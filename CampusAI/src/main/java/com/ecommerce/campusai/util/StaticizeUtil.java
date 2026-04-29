package com.ecommerce.campusai.util;

import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileWriter;
import java.util.Map;

@Component
public class StaticizeUtil {

    private final Configuration freemarkerConfig;
    
    @Value("${staticize.output.path:./static}")
    private String staticOutputPath;

    public StaticizeUtil(Configuration freemarkerConfig) {
        this.freemarkerConfig = freemarkerConfig;
    }

    public void createHtml(String templateName, String targetFileName, Map<String, Object> data) throws Exception {
        File dir = new File(staticOutputPath);
        if (!dir.exists()) dir.mkdirs();

        File targetFile = new File(dir, targetFileName);
        try (FileWriter writer = new FileWriter(targetFile)) {
            Template template = freemarkerConfig.getTemplate(templateName);
            template.process(data, writer);
        }
    }
}