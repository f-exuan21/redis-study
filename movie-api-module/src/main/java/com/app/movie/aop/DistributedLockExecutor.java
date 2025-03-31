package com.app.movie.aop;

import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;

import java.util.concurrent.TimeUnit;

@Slf4j
public class DistributedLockExecutor {


    public static void withLock(RLock lock, long waitTime, long leasTime, TimeUnit unit, Runnable action) {
        boolean locked = false;
        try {
            locked = lock.tryLock(waitTime, leasTime, unit);
            if(!locked) {
                throw new RuntimeException("다른 쓰레드가 락 점유");
            }
            action.run();
        } catch(InterruptedException e){
            Thread.currentThread().interrupt();
            throw new RuntimeException("락 획득 중 인터럽트 발생 : ", e);
        } finally {
            if(locked) {
                try {
                    lock.unlock();
                } catch(IllegalMonitorStateException e) {
                    log.error("언락 도중 에러 발생 : " + e.getMessage());
                }
            }
        }
    }


}
