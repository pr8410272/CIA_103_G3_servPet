package com.servPet.meb.model;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class PasswordResetService {
    private final Map<String, Integer> resetTokens = new HashMap<>(); // 使用 Integer 作為 userId 的型別

    public void saveResetToken(Integer userId, String token) {
        resetTokens.put(token, userId);
    }

    public Integer getUserIdByToken(String token) {
        return resetTokens.get(token);
    }

    public void removeToken(String token) {
        resetTokens.remove(token);
    }
}
