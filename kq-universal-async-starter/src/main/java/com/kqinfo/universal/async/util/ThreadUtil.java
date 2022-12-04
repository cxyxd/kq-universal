/*
 * Copyright 1999-2018 Alibaba Group Holding Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.kqinfo.universal.async.util;

import lombok.extern.slf4j.Slf4j;

import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author Zijian Liao
 * @since 1.14.0
 */
@Slf4j
public final class ThreadUtil {

    private static final int THREAD_MULTIPLE = 2;

    /**
     * Sleep.
     *
     * @param millis sleep millisecond
     */
    public static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    public static void countDown(CountDownLatch latch) {
        Objects.requireNonNull(latch, "latch");
        latch.countDown();
    }
    
    /**
     * Through the number of cores, calculate the appropriate number of threads; 1.5-2 times the number of CPU cores.
     *
     * @return thread count
     */
    public static int getSuitableThreadCount() {
        return getSuitableThreadCount(THREAD_MULTIPLE);
    }
    
    /**
     * Through the number of cores, calculate the appropriate number of threads.
     *
     * @param threadMultiple multiple time of cores
     * @return thread count
     */
    public static int getSuitableThreadCount(int threadMultiple) {
        final int coreCount = Runtime.getRuntime().availableProcessors();
        int workerCount = 1;
        while (workerCount < coreCount * threadMultiple) {
            workerCount <<= 1;
        }
        return workerCount;
    }
    

    /**
     * Shutdown thread pool.
     *
     * @param executor thread pool
     */
    public static void shutdownThreadPool(ExecutorService executor) {
        executor.shutdown();
        int retry = 3;
        while (retry > 0) {
            retry--;
            try {
                if (executor.awaitTermination(1, TimeUnit.SECONDS)) {
                    return;
                }
            } catch (InterruptedException e) {
                executor.shutdownNow();
                Thread.currentThread().interrupt();
            } catch (Throwable ex) {
                log.error("ThreadPoolManager shutdown executor has error", ex);
            }
        }
        executor.shutdownNow();
    }
    
    public static void addShutdownHook(Runnable runnable) {
        Runtime.getRuntime().addShutdownHook(new Thread(runnable));
    }

}
