package com.crazymakercircle.util;

import com.crazymakercircle.anno.ConfigFieldAnno;
import com.crazymakercircle.anno.ConfigFileAnno;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Properties;


/**
 * @author
 */
public class ConfigProperties {

    private String properiesName = "";
    private Properties properties = new Properties();


    public ConfigProperties() {

    }

    public ConfigProperties(String fileName) {
        this.properiesName = fileName;
    }


    protected void loadFromFile() {
        InputStream in = null;
        InputStreamReader ireader = null;
        try {
            String filePath = IOUtil.getResourcePath(properiesName);
            in = new FileInputStream(filePath);
            //解决读非UTF-8编码的配置文件时，出现的中文乱码问题
            ireader = new InputStreamReader(in, "utf-8");
            properties.load(ireader);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtil.closeQuietly(ireader);
        }
    }


    /**
     * 按key获取值
     *
     * @param key
     * @return
     */
    public String readProperty(String key) {
        String value = "";

        value = properties.getProperty(key);

        return value;
    }


    public String getValue(String key) {

        return readProperty(key);

    }

    public int getIntValue(String key) {

        return Integer.parseInt((readProperty(key)));

    }

    public static ConfigProperties loadFromFile(Class aClass)
            throws IllegalAccessException {

        ConfigProperties propertiesUtil = null;


        //判断是否使用了定义的注解接口
        boolean exist = aClass.isAnnotationPresent(ConfigFileAnno.class);
        if (!exist) {
            return null;
        }

        //获取注解接口中的
        Annotation a = aClass.getAnnotation(ConfigFileAnno.class);

        //强制转换成ConfigAnnotation类型
        ConfigFileAnno configFile = (ConfigFileAnno) a;
        //取得标记的属性值
        String propertyFile = configFile.file();

        Logger.info(" load properties: " + propertyFile);


        propertiesUtil = new ConfigProperties(propertyFile);
        //加载配置文件
        propertiesUtil.loadFromFile();


        return propertiesUtil;
    }

    public static void loadAnnotations(Class aClass) {

        ConfigProperties configProperties = null;
        try {
            configProperties = loadFromFile(aClass);


            if (null == configProperties) return;

            Field[] fields = aClass.getDeclaredFields();


            for (Field field : fields) {
//                Logger.info(field.getName());
                boolean exist = field.isAnnotationPresent(ConfigFieldAnno.class);
                if (!exist) continue;
                //获取注解接口中的
                Annotation[] annotations = field.getAnnotations();
                for (Annotation annotation : annotations) {
                    if (!(annotation instanceof ConfigFieldAnno))
                        continue;

                    //业务操作,取文件值,赋值
                    loadField(configProperties, field, (ConfigFieldAnno) annotation);

                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private static void loadField(
            ConfigProperties cp
            , Field field
            , ConfigFieldAnno config)
            throws IllegalAccessException {

//        Logger.info(field.getName() + ": " + config.proterty());

        String label = config.proterty();
        Class<?> type = field.getType();
        if (type.equals(Integer.class) || type.equals(int.class)) {
            int value = cp.getIntValue(label);
            field.set(null, value);
        } else if (type.equals(boolean.class) || type.equals(Boolean.class)) {
            String value = cp.getValue(label);
            field.set(null, Boolean.valueOf(value));
        } else {
            String value = cp.getValue(label);
            field.set(null, value);
        }
    }
}