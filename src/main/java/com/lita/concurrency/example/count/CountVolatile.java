package com.lita.concurrency.example.count;

import com.lita.concurrency.annoations.NotThreadSafe;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
@NotThreadSafe
public class CountVolatile {

    // total requests
    public static int clientTotal = 5000;

    // simultaneous running max threads
    public static int threadTotal = 200;

    public static AtomicLong count = new AtomicLong(0);


    public static void main(String[] args) {
        ExecutorService executorService = Executors.newCachedThreadPool();
        final Semaphore semaphore = new Semaphore(threadTotal);
        final CountDownLatch countDownLatch = new CountDownLatch(clientTotal);
        for (int i = 0; i < clientTotal; i++) {
            executorService.execute(() -> {
                try {
                    semaphore.acquire();
                    add();
                    semaphore.release();
                } catch (InterruptedException e) {
                    log.error("exception", e);
                }
                countDownLatch.countDown();
            });

        }
        try {
            countDownLatch.await();
            executorService.shutdown();
        } catch (InterruptedException e) {
            log.error("exception", e);
        }
        log.info("count: {}", count);
    }

    private static void add() {
        count.getAndAdd(1);
    }
}
