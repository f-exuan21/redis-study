package com.app.movie.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Aspect
@Component
public class DistributedLockAspect {

    private final RedissonClient redissonClient;

    public DistributedLockAspect(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    private String generateLockKey(ProceedingJoinPoint pjp) {
        String methodName = pjp.getSignature().toShortString();
        String args = Arrays.stream(pjp.getArgs())
                .map(Object::toString)
                .collect(Collectors.joining(","));
        return methodName + "(" + args + ")";
    }

    @Around("@annotation(distributedLock)")
    public Object around(ProceedingJoinPoint joinPoint, DistributedLock distributedLock) throws Throwable {

        String lockKey = distributedLock.key();
        if(lockKey.isEmpty()) {
            lockKey = generateLockKey(joinPoint);
        }

        RLock lock = redissonClient.getLock(lockKey);
        boolean acquired = false;
        try {
            acquired = lock.tryLock(distributedLock.waitTime(), distributedLock.leaseTime(), TimeUnit.SECONDS);
            if(acquired) {
                return joinPoint.proceed();
            } else {
                throw new RuntimeException("Unable to acquire distributed lock for key: " + lockKey);
            }
        } finally {
            if(acquired) {
                lock.unlock();
            }
        }

    }
}
