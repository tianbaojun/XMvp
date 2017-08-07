/*
 * 源文件名：AppOperator
 * 文件版本：1.0.0
 * 创建作者：captailgodwin
 * 创建日期：2016/11/7
 * 修改作者：captailgodwin
 * 修改日期：2016/11/7
 * 文件描述：app操作线程池
 * 版权所有：Copyright 2016 zjhz, Inc。 All Rights Reserved.
 */
package com.zjhz.teacher.ui.view.images.app;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public final class AppOperator {
    private static ExecutorService EXECUTORS_INSTANCE;

    public static Executor getExecutor() {
        if (EXECUTORS_INSTANCE == null) {
            synchronized (AppOperator.class) {
                if (EXECUTORS_INSTANCE == null) {
                    EXECUTORS_INSTANCE = Executors.newFixedThreadPool(
                            Runtime.getRuntime().availableProcessors() > 0 ?
                                    Runtime.getRuntime().availableProcessors() : 2);
                }
            }
        }
        return EXECUTORS_INSTANCE;
    }

    public static void runOnThread(Runnable runnable) {
        getExecutor().execute(runnable);
    }

}
