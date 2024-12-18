package com.servPet.cartDetails.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.servPet.cartDetails.model.CartDetailsService;
import com.servPet.cartDetails.model.CartDetailsVO;
import com.servPet.meb.model.MebService;
import com.servPet.meb.model.MebVO;
import com.servPet.pdDetails.model.PdDetailsRepository;
import com.servPet.pdDetails.model.PdDetailsService;
import com.servPet.pdDetails.model.PdDetailsVO;

import redis.clients.jedis.Jedis;

@Controller
@RequestMapping("/cartDetails")
public class CartDetailsController {

	@Autowired
	CartDetailsService cartDetailsSvc;
	
	@Autowired
	PdDetailsService pdDetailsSvc;
	
	@Autowired
	PdDetailsRepository pdDetailsRepository;	
	
	@Autowired
	private MebService mebService;
	
	//=======================
	private PdDetailsVO getProductDetails(Integer productId) {
	    return cartDetailsSvc.getProductDetails(productId);
	}


	@PostMapping("/addToCart")
	public ResponseEntity<String> addToCart(@RequestBody CartDetailsVO cartDetails,Principal principal) {
        String email = principal.getName();
        Optional<MebVO> memberOptional = mebService.findMemberByEmail(email);
        MebVO mebVO = memberOptional.get();
        Integer mebId = mebVO.getMebId();
		System.out.println("接收到的數據：" + cartDetails);
		cartDetailsSvc.addToCart(mebId, cartDetails); // 使用預設的用戶 ID
		return ResponseEntity.ok("商品已加入購物車");
	}

	@GetMapping("/viewCart")
	public ResponseEntity<List<CartDetailsVO>> viewCart(Principal principal) {
		// 不需要驗證 USER_ID，直接處理請求        
		String email = principal.getName();
        Optional<MebVO> memberOptional = mebService.findMemberByEmail(email);
        MebVO mebVO = memberOptional.get();
        Integer mebId = mebVO.getMebId();
		List<CartDetailsVO> cartItems = cartDetailsSvc.getCart(mebId); // 使用預設的用戶 ID
		return ResponseEntity.ok(cartItems);
	}

    @PostMapping("/removeFromCart")
    public ResponseEntity<String> removeFromCart(@RequestParam String productId,Principal principal) {
		String email = principal.getName();
        Optional<MebVO> memberOptional = mebService.findMemberByEmail(email);
        MebVO mebVO = memberOptional.get();
        Integer mebId = mebVO.getMebId();
//        String userId = "defaultUser";
        cartDetailsSvc.removeFromCart(mebId, productId);
        return ResponseEntity.ok("商品已從購物車移除");
    }
	
	@GetMapping("/shoppingCart")
	public String getShoppingCart(Model model,Principal principal) {
//	    String userId = "defaultUser"; 
		String email = principal.getName();
        Optional<MebVO> memberOptional = mebService.findMemberByEmail(email);
        MebVO mebVO = memberOptional.get();
        Integer mebId = mebVO.getMebId();
	    // 調用 Service 方法獲取購物車數據
	    List<CartDetailsVO> cartDetailsList = cartDetailsSvc.getCart(mebId);

	    // 建立商品 ID 和對應的圖片 ID 的映射
	    Map<Integer, Integer> defaultImages = cartDetailsSvc.getDefaultImages(cartDetailsList);

	    // 僅在 Controller 中打印日誌，確認數據完整性
	    if (cartDetailsList.isEmpty()) {
	        System.out.println("購物車為空");
	    } else {
	        cartDetailsList.forEach(item -> {
	            System.out.println("商品 ID: " + item.getPdDetailsVO().getPdId());
	            System.out.println("商品價格: " + item.getPdDetailsVO().getPdPrice());
	            System.out.println("商品數量: " + item.getQuantity());
	        });
	    }

	    // 將購物車清單與圖片映射傳遞給前端
	    model.addAttribute("cartDetailsList", cartDetailsList);
	    model.addAttribute("defaultImages", defaultImages);

	    return "front_end/shoppingCart"; // 返回模板名稱
	}
	
	@PostMapping("/updateQuantity")
	public ResponseEntity<String> updateQuantity(@RequestParam String productId, @RequestParam int quantity,Principal principal) {
//	    String userId = "defaultUser"; // 假設使用預設用戶
		String email = principal.getName();
        Optional<MebVO> memberOptional = mebService.findMemberByEmail(email);
        MebVO mebVO = memberOptional.get();
        Integer mebId = mebVO.getMebId();
	    cartDetailsSvc.updateQuantity(mebId, productId, quantity);
	    return ResponseEntity.ok("商品數量已更新");
	}
	
//	@GetMapping("/shoppingCart")
//	public String getShoppingCartGuest(Model model, Principal principal) {
//	    List<CartDetailsVO> cartDetailsList;
//	    Map<Integer, Integer> defaultImages;
//
//	    if (principal != null) { // 用戶已登入
//	        String email = principal.getName();
//	        Optional<MebVO> memberOptional = mebService.findMemberByEmail(email);
//
//	        if (memberOptional.isPresent()) {
//	            MebVO mebVO = memberOptional.get();
//	            Integer mebId = mebVO.getMebId();
//
//	            // 調用 Service 方法獲取購物車數據
//	            cartDetailsList = cartDetailsSvc.getCart(mebId);
//	        } else {
//	            // 用戶不存在的情況
//	            cartDetailsList = Collections.emptyList();
//	        }
//	    } else { // 未登入用戶的購物車邏輯
//	        cartDetailsList = getGuestCartDetails(); // 使用預設或 Session 的購物車數據
//	    }
//
//	    // 建立商品 ID 和對應的圖片 ID 的映射
//	    defaultImages = cartDetailsSvc.getDefaultImages(cartDetailsList);
//
//	    // 將購物車清單與圖片映射傳遞給前端
//	    model.addAttribute("cartDetailsList", cartDetailsList);
//	    model.addAttribute("defaultImages", defaultImages);
//
//	    return "front_end/shoppingCart"; // 返回模板名稱
//	}
//
//	private List<CartDetailsVO> getGuestCartDetails() {
//	    // 模擬未登入用戶的預設購物車數據
//	    return new ArrayList<>(); // 可替換為 Session 資料
//	}
//
//	@PostMapping("/addToCart")
//	public ResponseEntity<String> addToCart(@RequestParam(required = false) Integer productId,
//	                                       @RequestParam(required = false) Integer quantity,
//	                                       @RequestBody(required = false) CartDetailsVO cartDetails,
//	                                       Principal principal, HttpSession session) {
//
//	    if (principal != null) {
//	        // 已登入邏輯：使用 Redis
//	        String email = principal.getName();
//	        Integer mebId = mebService.findMemberByEmail(email).get().getMebId();
//
//	        if (cartDetails != null) {
//	            cartDetailsSvc.addToCart(mebId, cartDetails);
//	        } else if (productId != null && quantity != null) {
//	            CartDetailsVO details = new CartDetailsVO();
//	            details.setQuantity(quantity);
//	            details.setPdDetailsVO(getProductDetails(productId));
//	            cartDetailsSvc.addToCart(mebId, details);
//	        } else {
//	            return ResponseEntity.badRequest().body("參數不足，無法加入購物車");
//	        }
//	    } else {
//	        // 未登入邏輯：使用 Session
//	        List<CartDetailsVO> cartList = (List<CartDetailsVO>) session.getAttribute("cart");
//	        if (cartList == null) {
//	            cartList = new ArrayList<>();
//	        }
//
//	        if (cartDetails != null) {
//	            cartList.add(cartDetails);
//	        } else if (productId != null && quantity != null) {
//	            CartDetailsVO details = new CartDetailsVO();
//	            details.setQuantity(quantity);
//	            details.setPdDetailsVO(getProductDetails(productId));
//	            cartList.add(details);
//	        } else {
//	            return ResponseEntity.badRequest().body("參數不足，無法加入購物車");
//	        }
//
//	        // 更新 Session
//	        session.setAttribute("cart", cartList);
//	    }
//
//	    return ResponseEntity.ok("商品已成功加入購物車");
//	}
//
//	@GetMapping("/viewCart")
//	public ResponseEntity<List<CartDetailsVO>> viewCart(Principal principal, HttpSession session) {
//	    List<CartDetailsVO> cartItems;
//
//	    if (principal != null) {
//	        // 已登入邏輯：從 Redis 中獲取購物車
//	        String email = principal.getName();
//	        Integer mebId = mebService.findMemberByEmail(email).get().getMebId();
//	        cartItems = cartDetailsSvc.getCart(mebId);
//	    } else {
//	        // 未登入邏輯：從 Session 中獲取購物車
//	        cartItems = (List<CartDetailsVO>) session.getAttribute("cart");
//	        if (cartItems == null) {
//	            cartItems = new ArrayList<>();
//	        }
//	    }
//
//	    return ResponseEntity.ok(cartItems);
//	}






}
