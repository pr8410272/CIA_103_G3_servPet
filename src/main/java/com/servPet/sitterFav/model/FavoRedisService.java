package com.servPet.sitterFav.model;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class FavoRedisService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private static final String REDIS_REMOVED_KEY_PREFIX = "removed_favorites:";

    /** 添加移除紀錄到 Redis **/
    public void addRemovedFavorite(Integer mebId, Integer psId) {
        String key = REDIS_REMOVED_KEY_PREFIX + mebId;
        redisTemplate.opsForSet().add(key, psId);
    }

    /** 從 Redis 中移除指定的收藏 **/
    public void restoreFavorite(Integer mebId, Integer psId) {
        String key = REDIS_REMOVED_KEY_PREFIX + mebId;
        redisTemplate.opsForSet().remove(key, psId);
    }

    /** 獲取已移除的收藏列表 **/
    public Set<Object> getRemovedFavorites(Integer mebId) {
        String key = REDIS_REMOVED_KEY_PREFIX + mebId;
        return redisTemplate.opsForSet().members(key);
    }

    /** 檢查某收藏是否存在於移除列表中 **/
    public boolean isRemovedFavorite(Integer mebId, Integer psId) {
        String key = REDIS_REMOVED_KEY_PREFIX + mebId;
        return redisTemplate.opsForSet().isMember(key, psId);
    }
}
