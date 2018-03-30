package com.lita.concurrency.example.atomic;

import com.lita.concurrency.annoations.ThreadSafe;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
@ThreadSafe
public class AtomicBooleanShowcase {
    private static AtomicBoolean isHappened = new AtomicBoolean(false);

    public static int clientTotal = 5000;
    // simultaneous running max threads
    public static int threadTotal = 200;

    public static void main(String[] args) {
         ExecutorService executorService = Executors.newCachedThreadPool();
         final Semaphore semaphore = new Semaphore(threadTotal);
         final CountDownLatch countDownLatch = new CountDownLatch(clientTotal);
         for (int i = 0; i < clientTotal; i++) {
             executorService.execute(() -> {
                 try {
                     semaphore.acquire();
                     test();
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
         log.info("count: {}", isHappened.get());
    }

    private static void test () {
        if (isHappened.compareAndSet(false, true)) {
            log.info("execute");
        }
    }
}
