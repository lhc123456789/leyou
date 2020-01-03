/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.leyou.utils;

import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.util.Properties;

/**
 * Created by admin on 2019/1/4.
 * 获取yml配置文件内容工具类
 */
public class PropertiesUtils {
    private static String PROPERTY_NAME = "application.yml";

    public static String getProperty(Object key) {
        Resource resource = new ClassPathResource(PROPERTY_NAME);
        Properties properties = null;
        try {
            YamlPropertiesFactoryBean yamlFactory = new YamlPropertiesFactoryBean();
            yamlFactory.setResources(resource);
            properties = yamlFactory.getObject();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        String value = (String) properties.get(key);
        return value;
    }


}
