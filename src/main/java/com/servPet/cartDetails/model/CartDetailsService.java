package com.servPet.cartDetails.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.servPet.pdDetails.model.PdDetailsRepository;
import com.servPet.pdDetails.model.PdDetailsVO;
import com.servPet.pdImg.model.PdImgService;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Service("cartDetailsService")
public class CartDetailsService {

	@Autowired
	private JedisPool jedisPool;

	@Autowired
	private ObjectMapper objectMapper; // 用於將對象轉換為 JSON

	@Autowired
	private PdDetailsRepository pdDetailsRepository; // 商品數據庫的 Repository

	@Autowired
	private PdImgService pdImgService;

//	private static final String CART_KEY_PREFIX = "cart:";

	public void addToCart(Integer userId, CartDetailsVO cartDetails) {
		try (Jedis jedis = jedisPool.getResource()) {
			String redisKey = "cart:" + userId;
			String productId = cartDetails.getPdDetailsVO().getPdId().toString();

			// 將 CartDetailsVO 序列化為 JSON 字符串
			String jsonValue = objectMapper.writeValueAsString(cartDetails);

			// 存儲到 Redis
			jedis.hset(redisKey, productId, jsonValue);
		} catch (JsonProcessingException e) {
			throw new RuntimeException("JSON 序列化失敗: " + e.getMessage(), e);
		}
	}

	public void removeFromCart(Integer mebId, String productId) {
		try (Jedis jedis = jedisPool.getResource()) {
			String redisKey = "cart:" + mebId;
			jedis.hdel(redisKey, productId);
		} catch (Exception e) {
			throw new RuntimeException("Redis 操作失敗: " + e.getMessage(), e);
		}
	}

	public List<CartDetailsVO> getCart(Integer mebId) {
		List<CartDetailsVO> cartDetailsList = new ArrayList<>();
		try (Jedis jedis = jedisPool.getResource()) {
			String cartKey = "cart:" + mebId;
			Map<String, String> cartData = jedis.hgetAll(cartKey);

			for (Map.Entry<String, String> entry : cartData.entrySet()) {
				try {
					CartDetailsVO cartItem = objectMapper.readValue(entry.getValue(), CartDetailsVO.class);

					// 確保 pdDetailsVO 不為 null
					if (cartItem.getPdDetailsVO() == null) {
						cartItem.setPdDetailsVO(new PdDetailsVO());
					}

					// 確保 pdPrice 和 quantity 有默認值
					if (cartItem.getPdDetailsVO().getPdPrice() == null) {
						cartItem.getPdDetailsVO().setPdPrice(0);
					}
					if (cartItem.getQuantity() == null) {
						cartItem.setQuantity(1); // 設置默認數量
					}

					cartDetailsList.add(cartItem);
				} catch (JsonProcessingException e) {
					throw new RuntimeException("JSON 反序列化失敗: " + e.getMessage(), e);
				}
			}
		} catch (Exception e) {
			throw new RuntimeException("Redis 操作失敗: " + e.getMessage(), e);
		}
		return cartDetailsList;
	}

	public List<CartDetailsVO> getCartDetails(Integer mebId) {
		List<CartDetailsVO> cartDetailsList = new ArrayList<>();
		ObjectMapper objectMapper = new ObjectMapper(); // JSON 解析器

		try (Jedis jedis = jedisPool.getResource()) {
			String redisKey = "cart:" + mebId;
			Map<String, String> cartData = jedis.hgetAll(redisKey); // 從 Redis 獲取所有商品數據

			for (Map.Entry<String, String> entry : cartData.entrySet()) {
				Integer pdId = Integer.parseInt(entry.getKey()); // 商品 ID

				// 解析 JSON 字串以獲取數量
				CartDetailsVO cartDetailsVO = objectMapper.readValue(entry.getValue(), CartDetailsVO.class);
				Integer quantity = cartDetailsVO.getQuantity();

				// 查詢商品資料
				PdDetailsVO pdDetailsVO = pdDetailsRepository.findById(pdId).orElse(null);
				if (pdDetailsVO != null) {
					cartDetailsVO.setPdDetailsVO(pdDetailsVO);
					cartDetailsList.add(cartDetailsVO);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return cartDetailsList;
	}

	public Map<Integer, Integer> getDefaultImages(List<CartDetailsVO> cartDetailsList) {
		Map<Integer, Integer> defaultImages = new HashMap<>();
		for (CartDetailsVO cartItem : cartDetailsList) {
			Integer pdId = cartItem.getPdDetailsVO().getPdId();
			Integer imageId = pdImgService.getDefaultImgIdByProductId(pdId); // 調用 PdImgService 方法
			defaultImages.put(pdId, imageId);
		}
		return defaultImages;
	}

	public void updateQuantity(Integer mebId, String productId, int quantity) {
		try (Jedis jedis = jedisPool.getResource()) {
			String cartKey = "cart:" + mebId;
			String cartItemJson = jedis.hget(cartKey, productId);
			if (cartItemJson != null) {
				// 反序列化 JSON 為 CartDetailsVO
				CartDetailsVO cartItem = objectMapper.readValue(cartItemJson, CartDetailsVO.class);
				cartItem.setQuantity(quantity); // 更新數量
				// 將更新後的 CartDetailsVO 序列化為 JSON 並存回 Redis
				jedis.hset(cartKey, productId, objectMapper.writeValueAsString(cartItem));
			}
		} catch (Exception e) {
			throw new RuntimeException("更新商品數量失敗: " + e.getMessage(), e);
		}
	}
	
	public void clearCart(Integer mebId) {
	    try (Jedis jedis = jedisPool.getResource()) {
	        String redisKey = "cart:" + mebId; // Redis 購物車的 Key
	        jedis.del(redisKey); // 刪除整個購物車
	        System.out.println("購物車已清空，Key: " + redisKey);
	    } catch (Exception e) {
	        throw new RuntimeException("刪除購物車資料失敗: " + e.getMessage(), e);
	    }
	}
	public PdDetailsVO getProductDetails(Integer productId) {
	    // 使用 Repository 查詢商品資料
	    return pdDetailsRepository.findById(productId)
	            .orElseThrow(() -> new RuntimeException("找不到該商品的詳細資訊，商品 ID: " + productId));
	}
}

//	以下是有登入的

/**
 * 將商品加入購物車
 * 
 * @param cartDetailsVO 購物車資料
 */
//    public void addToCart(CartDetailsVO cartDetailsVO) {
//        try (Jedis jedis = jedisPool.getResource()) {
//            // 從 CartDetailsVO 取得會員 ID 作為 Redis Key 的一部分
//            String userId = cartDetailsVO.getMebVO().getMebId().toString();
//            String redisKey = CART_KEY_PREFIX + userId;
//
//            // 商品 ID 作為 Redis Hash 的 field
//            String productId = cartDetailsVO.getPdDetailsVO().getPdId().toString();
//
//            // 將 CartDetailsVO 物件序列化為 JSON 字符串
//            String jsonValue = objectMapper.writeValueAsString(cartDetailsVO);
//
//            // 將資料存入 Redis
//            jedis.hset(redisKey, productId, jsonValue);
//            System.out.println("商品已存入 Redis，Key: " + redisKey + ", Field: " + productId);
//        } catch (JsonProcessingException e) {
//            throw new RuntimeException("JSON 序列化失敗: " + e.getMessage(), e);
//        }
//    }