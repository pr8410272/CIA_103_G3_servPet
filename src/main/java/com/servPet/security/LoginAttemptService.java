package com.servPet.security;

import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Service
public class LoginAttemptService {

    private final int MAX_ATTEMPTS = 5;
    private final long LOCK_TIME = TimeUnit.MINUTES.toMillis(15); // 鎖定 15 分鐘
    private final ConcurrentHashMap<String, Long> attemptsCache = new ConcurrentHashMap<>();

    public void loginFailed(String username) {
        long currentTime = System.currentTimeMillis();
        attemptsCache.put(username, currentTime);
    }

    public boolean isBlocked(String username) {
        Long lastAttemptTime = attemptsCache.get(username);
        if (lastAttemptTime == null) {
            return false;
        }
        if ((System.currentTimeMillis() - lastAttemptTime) > LOCK_TIME) {
            attemptsCache.remove(username);
            return false;
        }
        return true;
    }

    public void loginSucceeded(String username) {
        attemptsCache.remove(username);
    }
}
