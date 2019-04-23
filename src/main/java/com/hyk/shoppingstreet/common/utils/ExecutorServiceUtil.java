package com.hyk.shoppingstreet.common.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExecutorServiceUtil {

    // volatile 防止可能get到初始化尚未完成的fixedThreadPool
    private volatile static ExecutorService fixedThreadPool;

    public static ExecutorService getFixedThreadPool() {
        if (fixedThreadPool == null) {
            synchronized (ExecutorServiceUtil.class) {
                if (fixedThreadPool == null) {
                    fixedThreadPool = Executors.newFixedThreadPool(4);
                }
            }
        }
        return fixedThreadPool;
    }

}
