package com.hyk.shoppingstreet.common.utils;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class UUIDUtil {

    private static String profile = "";

    public static void setProfile(String profile) {
        UUIDUtil.profile = profile;
    }

    public static AtomicInteger atomicInteger = new AtomicInteger();

    public static String uuid() {
        if (profile.equalsIgnoreCase("qa")) {
            return atomicInteger.getAndIncrement() + "";
        } else {
            return UUID.randomUUID().toString();
        }
    }
}