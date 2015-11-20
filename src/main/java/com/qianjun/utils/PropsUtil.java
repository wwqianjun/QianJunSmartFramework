package com.qianjun.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by ZiJun
 * Description:
 * 属性文件工具类，读取config.properties
 * Date: 2015/11/18 :15:47.
 */
public class PropsUtil {

    private static final Logger LOG = LoggerFactory.getLogger(PropsUtil.class);

    /**
     * 加载属性文件
     * @param fileName
     * @return
     */
    public static Properties loadPros(String fileName){
        Properties prop = null;
        InputStream is = null;
        try {
            is = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
            if ( is == null ){
                throw new FileNotFoundException(fileName + " file is not found");
            }
            prop = new Properties();
            prop.load(is);
        }catch (IOException e){
            LOG.error("load properties file failure",e);
        }finally {
            if ( is != null ){
                try {
                    is.close();
                } catch (IOException e) {
                    LOG.error("close input stream failure",e);
                }
            }
        }
        return  prop;
    }

    /**
     * 获取字符型属性(默认是null)
     * @param props
     * @param key
     * @return
     */
    public static String getString(Properties props, String key){
        return getString(props, key, null);
    }

    /**
     * 获取字符型属性(可指定默认值)
     * @param props
     * @param key
     * @param defaultValue 指定的默认值
     * @return
     */
    public static String getString(Properties props, String key, String defaultValue){
        String value = defaultValue;
        if ( props.containsKey(key)){
            value = props.getProperty(key);
        }

        return value;
    }

    public static int getInt(Properties props, String key){
        return getInt(props, key, 0);
    }

    private static int getInt(Properties props, String key, int defaultValue) {
        int value = defaultValue;
        if ( props.containsKey(key) ){
            value = CastUtil.castInt(props.getProperty(key));
        }

        return value;
    }
}
