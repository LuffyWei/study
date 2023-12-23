package com.study.config;

/**
 * 针对h2底层不支持的数据库操作，在此处可以自定义返回结果
 */
public class H2CustomFunction {

    public static String ST_AsText(String s) {
        return s;
    }

    public static String ST_MultiPolygonFromText(String s, int gridId) {
        return s;
    }
}
