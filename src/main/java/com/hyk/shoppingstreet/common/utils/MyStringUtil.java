package com.hyk.shoppingstreet.common.utils;

import org.apache.commons.lang3.StringUtils;

public class MyStringUtil {

    public static String like(String str) {
        if (StringUtils.isNotBlank(str)) {
            return new StringBuilder().append("%").append(str).append("%").toString();
        }
        return str;
    }

}
