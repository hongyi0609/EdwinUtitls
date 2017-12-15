package com.edwin.edwinlibrary.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by hongy_000 on 2017/12/9.
 */

public class RegxUtils {

    /**
     *
     * @param str e.g. 12,wew3423.36,wewsf,000,null
     * @return
     */
    public static float getPureDouble(String str) {
        if (str == null || str.length() == 0) {
            return 0;
        }
        float result = 0f;
        try {
            Pattern compile = Pattern.compile("(\\d+\\.\\d+)|(\\d+)");
            Matcher matcher = compile.matcher(str);
            if (matcher.find()) {
                String string = matcher.group();//提取匹配到的结果
                result = Float.parseFloat(string);
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return result;
    }
}
