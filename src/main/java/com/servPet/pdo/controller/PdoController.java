package com.servPet.pdo.controller;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.servPet.admin.model.AdminVO;
import com.servPet.adminPer.model.AdminPerService;
import com.servPet.adminPer.model.AdminPerVO;
import com.servPet.cartDetails.model.CartDetailsService;
import com.servPet.cartDetails.model.CartDetailsVO;
import com.servPet.meb.model.MebService;
import com.servPet.meb.model.MebVO;
import com.servPet.pdDetails.model.PdDetailsService;
import com.servPet.pdDetails.model.PdDetailsVO;
import com.servPet.pdo.model.PdoRepository;
import com.servPet.pdo.model.PdoService;
import com.servPet.pdo.model.PdoVO;

@Controller
@RequestMapping("/pdo")
public class PdoController {

	@Autowired
	PdoRepository repository;

	@Autowired
	PdoService pdoSvc;

	@Autowired
	PdDetailsService pdDetailsSvc;

	@Autowired
	CartDetailsService cartDetailsSvc;

	@Autowired
	AdminPerService adminPerSvc;

	@Autowired
	MebService mebSvc;

	// 公共方法來檢查 admin 是否存在於 session 中
	private boolean isAdminLoggedIn(HttpSession session) {
		if (isSessionExpired(session)) {
			return false;
		}
		return session.getAttribute("adminVO") != null;
	}

	private boolean isSessionExpired(HttpSession session) {
		// 可以嘗試獲取一個Session屬性，如果拋出異常，則認為Session已失效
		try {
			session.getAttribute("anyAttribute"); // 獲取任意一個屬性，用於觸發Session失效異常
			return false; // 如果沒有拋出異常，表示Session有效
		} catch (IllegalStateException e) {
			return true; // Session已失效
		}
	}

	// ===<< 將admin存於 session 中一起送出 >>===/
	public String SaveSession(Model model, HttpSession session) {
		if (!isAdminLoggedIn(session)) {
			return "back_end/login";
		}
		AdminVO admin = (AdminVO) session.getAttribute("adminVO");
		model.addAttribute("adminVO", new AdminVO());
		model.addAttribute("admin", admin);
		List<AdminPerVO> adminPer = adminPerSvc.getPerByAdminId(admin.getAdminId());

		// 只取出該管理員有權限的功能 ID
		Set<Integer> allowedFunctionIds = adminPer.stream().map(adminPerVO -> adminPerVO.getFncVO().getFncId())
				.collect(Collectors.toSet());
		model.addAttribute("allowedFunctionIds", allowedFunctionIds);

		return null;
	}

	// ===<< 將admin存於 session 中一起送出 >>===/
	public String SaveSession(ModelMap model, HttpSession session) {
		if (!isAdminLoggedIn(session)) {
			return "back_end/login";
		}
		AdminVO admin = (AdminVO) session.getAttribute("adminVO");
		model.addAttribute("adminVO", new AdminVO());
		model.addAttribute("admin", admin);
		List<AdminPerVO> adminPer = adminPerSvc.getPerByAdminId(admin.getAdminId());

		// 只取出該管理員有權限的功能 ID
		Set<Integer> allowedFunctionIds = adminPer.stream().map(adminPerVO -> adminPerVO.getFncVO().getFncId())
				.collect(Collectors.toSet());
		model.addAttribute("allowedFunctionIds", allowedFunctionIds);

		return null;
	}

	// ===========<< 導向addAdmin >>============/
	@GetMapping("addAdmin")
	public String addAdmin(Model model, HttpSession session) {
		SaveSession(model, session);
		return "back_end/admin/addAdmin";
	}

	// =======================

	@GetMapping("productOrderDashboard")
	public String select_page(Model model, HttpSession session) {
		SaveSession(model, session);
		return "back_end/productOrderDashboard";
	}

	@PostMapping("/pdOrderSubmitted")
	public String submitOrder(
//			@RequestParam("buyerAddress") String buyerAddress,
			@RequestParam("shippingMethod") String shippingMethod, @RequestParam("totalAmount") Integer totalAmount,
			Model model,Principal principal) {
        String email = principal.getName();
        Optional<MebVO> memberOptional = mebSvc.findMemberByEmail(email);
        MebVO mebVO = memberOptional.get();
        Integer mebId = mebVO.getMebId();
        String buyerAddress =mebVO.getMebAddress();
        System.out.println(buyerAddress);
		// 模擬會員 ID（實際應從登入用戶中獲取）
//		String userId = "defaultUser";
		
		// 驗證收件人地址和運輸方式
	    if (buyerAddress == null || buyerAddress.trim().isEmpty()) {
	        model.addAttribute("error", "收件人地址必填");
	        return "redirect:/cartDetails/shoppingCart"; // 回到原頁面
	    }
	    if (shippingMethod == null || shippingMethod.trim().isEmpty()) {
	        model.addAttribute("error", "請選擇運輸方式");
	        return "redirect:/cartDetails/shoppingCart"; // 回到原頁面
	    }

		// 從購物車 Service 獲取商品和數量
		List<CartDetailsVO> cartDetails = cartDetailsSvc.getCartDetails(mebId);
		if (cartDetails.isEmpty()) {
			throw new IllegalStateException("購物車為空，無法提交訂單");
		}

		// 提取商品 ID 和數量
		List<PdDetailsVO> productList = cartDetails.stream().map(CartDetailsVO::getPdDetailsVO)
				.collect(Collectors.toList());
		List<Integer> quantities = cartDetails.stream().map(CartDetailsVO::getQuantity).collect(Collectors.toList());

		// 調用 Service 方法處理訂單及訂單細項
		Integer pdoId = pdoSvc.createOrderWithItems(buyerAddress, shippingMethod, null, productList, quantities, principal);
		
		System.out.println("總金額: " + totalAmount);
		
		  // 清除 Redis 購物車資料
	    cartDetailsSvc.clearCart(mebId);
	    System.out.println("購物車已清空，訂單提交成功");

		// 傳遞訂單編號到前端
		return "redirect:/pdo/orderConfirmation?pdoId=" + pdoId;

	}

	@GetMapping("/orderConfirmation")
	public String orderConfirmation(@RequestParam("pdoId") Integer pdoId, Model model) {
		// 根據訂單 ID 查詢訂單資訊（如果需要）
		PdoVO pdo = pdoSvc.getOnePdo(pdoId);

		// 從訂單查詢會員信息
		MebVO member = pdoSvc.getMemberInfoByOrderId(pdoId);

		// 將訂單資訊傳遞到前端
		model.addAttribute("orderId", pdoId);
		model.addAttribute("pdo", pdo);
		model.addAttribute("mebName", member.getMebName());
		model.addAttribute("mebId", member.getMebId());
		model.addAttribute("bal", member.getBal());

		// 返回訂單確認頁面
		return "front_end/orderConfirmation"; // 確保該頁面存在
	}

	@PostMapping("/pdoBalUpdate")
	public String updateBalance(@RequestParam("orderId") Integer orderId, Principal principal, @RequestParam("totalPrice") Double totalPrice, Model model) {
		// 使用 mebService 直接扣減會員餘額
        String email = principal.getName();
        Optional<MebVO> memberOptional = mebSvc.findMemberByEmail(email);
        MebVO mebVO = memberOptional.get();
        Integer mebId = mebVO.getMebId();
			mebSvc.deductBalance(mebId, totalPrice);

			return "redirect:/pdo/pdoPaidSuccess?pdoId=" + orderId;
	}
	
	@GetMapping("/pdoPaidSuccess")
	public String pdoPaidSuccess(@RequestParam("pdoId") Integer pdoId, Model model) {
		// 更新訂單付款狀態為 1: 已付款
	    pdoSvc.updatePaymentStatus(pdoId, "1");
		
		// 根據訂單 ID 查詢訂單資訊（如果需要）
		PdoVO pdo = pdoSvc.getOnePdo(pdoId);

		// 從訂單查詢會員信息
		MebVO member = pdoSvc.getMemberInfoByOrderId(pdoId);

		// 將訂單資訊傳遞到前端
		model.addAttribute("orderId", pdoId);
		model.addAttribute("pdo", pdo);
		model.addAttribute("mebName", member.getMebName());
		model.addAttribute("mebId", member.getMebId());
		model.addAttribute("bal", member.getBal());
		
		// 返回訂單確認頁面
		return "front_end/pdoPaidSuccess"; // 確保該頁面存在
	}

	@GetMapping("listAllProductsOrders")
	public String listAllProductsOrders(Model model, HttpSession session) {
		SaveSession(model, session);
		// 使用 service 的 getAll 方法取得所有產品
		List<PdoVO> list = pdoSvc.getAll();
//	    List<PdDetailsVO> list = pdoSvc.getAllProductsWithCategory();
		model.addAttribute("pdoList", list); // 將資料傳給前端
		return "back_end/listAllProductsOrders"; // 返回對應的 Thymeleaf 頁面
	}

	@GetMapping("listAllProductsOrdersByPaymentStatus")
	public String listAllProductsOrdersByPaymentStatus(Model model, HttpSession session) {
		SaveSession(model, session);
		// 使用 service 的 getAll 方法取得所有產品
		List<PdoVO> list = pdoSvc.getAll();
//	    List<PdDetailsVO> list = pdoSvc.getAllProductsWithCategory();
		model.addAttribute("pdoList", list); // 將資料傳給前端
		return "back_end/listAllProductsOrdersByPaymentStatus"; // 返回對應的 Thymeleaf 頁面
	}

	@PostMapping("getOnePdo_For_Update")
	public String getOne_For_Update(@RequestParam("pdoId") Integer pdoId, ModelMap model, HttpSession session) {
		SaveSession(model, session);

		try {
			// 調用 Service 層獲取訂單資料
			PdoVO pdoVO = pdoSvc.getPdoForUpdate(pdoId);

			// 添加訂單資料到模型
			model.addAttribute("pdoVO", pdoVO);

			// 返回到更新頁面
			return "back_end/updateProductOrder";

		} catch (IllegalArgumentException e) {
			// 處理 Service 層拋出的錯誤
			model.addAttribute("errorMessage", e.getMessage());
			return "error_page"; // 返回錯誤頁面
		}
	}

	@PostMapping("updateOrder")
	public String updateOrder(@RequestParam("pdoId") Integer pdoId, @RequestParam("pdoStatus") String pdoStatus,
			@RequestParam("shippingAddr") String shippingAddr, @RequestParam("shippingMethod") String shippingMethod,
			@RequestParam("shippingStatus") String shippingStatus, ModelMap model, HttpSession session) {
		SaveSession(model, session);

		try {
			// 調用 Service 層執行訂單更新邏輯
			pdoSvc.updateOrder(pdoId, pdoStatus, shippingAddr, shippingMethod, shippingStatus);

			// 使用 service 的 getAll 方法取得所有產品
			List<PdoVO> list = pdoSvc.getAll();
//		    List<PdDetailsVO> list = pdoSvc.getAllProductsWithCategory();
			model.addAttribute("pdoList", list); // 將資料傳給前端
			// 返回成功後的頁面
			return "back_end/listAllProductsOrders"; // 重定向到列表頁面

		} catch (IllegalArgumentException e) {
			// 處理 Service 層的錯誤
			model.addAttribute("errorMessage", e.getMessage());
			return "error_page"; // 返回錯誤頁面
		}
	}

}