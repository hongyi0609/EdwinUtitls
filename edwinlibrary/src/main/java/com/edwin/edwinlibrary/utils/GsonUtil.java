package com.edwin.edwinlibrary.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * Created by hongy_000 on 2017/12/9.
 */

public class GsonUtil {
    private static Gson gson = null;
    private static Gson prettyGson = null;
    static {
        gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        prettyGson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss")
                .setPrettyPrinting().create();
    }

    /**
     * 小写下划线的形式解析json字符串到对象
     * <p>e.g. is_success->isSuccess</p>
     * @param json
     * @param tClass
     * @param <T>
     * @return
     */
    public static <T> T fromJsonUnderScoreStyle(String json, Class<T> tClass) {
        return gson.fromJson(json, tClass);
    }

    /**
     * Json字符串转化为Map<String,String>
     * @param json
     * @param <T>
     * @return
     */
    @SuppressWarnings("all")
    private static <T> T fromJson2Map(String json) {
        return gson.fromJson(json, new TypeToken<Map<String, String>>() {
        }.getType());
    }

    /**
     * 小写下划线的格式将对象转换成JSON字符串
     *
     * @param src
     * @return
     */
    public static String toJson(Object src) {
        return gson.toJson(src);
    }

    public static String toPrettyString(Object src) {
        return prettyGson.toJson(src);
    }

    public static <T> T fromJson2Object(String src, Class<T> t) {
        return gson.fromJson(src, t);
    }

    public static <T> T fromJson2Object(String src, Type t) {
        return gson.fromJson(src, t);
    }
}

