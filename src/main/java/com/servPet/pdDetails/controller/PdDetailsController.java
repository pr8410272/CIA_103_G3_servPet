package com.servPet.pdDetails.controller;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.servPet.admin.model.AdminVO;
import com.servPet.adminPer.model.AdminPerService;
import com.servPet.adminPer.model.AdminPerVO;
import com.servPet.pdCateg.model.PdCategService;
import com.servPet.pdCateg.model.PdCategVO;
import com.servPet.pdDetails.model.PdDetailsService;
import com.servPet.pdDetails.model.PdDetailsVO;
import com.servPet.pdImg.model.PdImgRepository;
import com.servPet.pdImg.model.PdImgService;
import com.servPet.pdImg.model.PdImgVO;

@Controller
@RequestMapping("/pdDetails")
public class PdDetailsController {

	@Autowired
	PdDetailsService pdDetailsSvc;

	@Autowired
	PdCategService pdCategSvc;

	@Autowired
	PdImgService pdImgSvc;

	@Autowired
	PdImgRepository pdImgRepository;
	
	@Autowired
	AdminPerService adminPerSvc;	
	
	
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
        Set<Integer> allowedFunctionIds = adminPer.stream()
            .map(adminPerVO -> adminPerVO.getFncVO().getFncId())
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
        Set<Integer> allowedFunctionIds = adminPer.stream()
            .map(adminPerVO -> adminPerVO.getFncVO().getFncId())
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
	
	
	//=======================
	
	
   @GetMapping("productsDashboard")
	public String select_page(Model model, HttpSession session) {
	   SaveSession(model, session);
		return "back_end/productsDashboard";
	}

	// 顯示新增商品頁面
	@GetMapping("addProducts")
	public String addProducts(ModelMap model, HttpSession session) {
		SaveSession(model, session);
		PdDetailsVO pdDetailsVO = new PdDetailsVO();
		model.addAttribute("pdDetailsVO", pdDetailsVO);
		return "back_end/addProducts";
	}

	// 處理單一或多個商品新增
	@PostMapping("insertOrBatchInsert")
	public String insertProduct(@Valid PdDetailsVO pdDetailsVO, BindingResult result, ModelMap model,
			@RequestParam("images") MultipartFile[] images, HttpSession session) {
		SaveSession(model, session);

		// 如果表單有錯誤，返回到新增商品的頁面，提示錯誤
		if (result.hasErrors()) {
			model.addAttribute("errorMessage", "請確認所有必填欄位正確填寫");
			return "back_end/addProducts"; // 回到新增商品頁面
		}

		// 將表單數據轉發到下一個處理層（如 Service 層或其他處理）
		// Controller 僅負責表單驗證與頁面邏輯，不執行實際的業務操作
		try {
			// 呼叫服務層方法處理商品與圖片新增
			pdDetailsSvc.addPdDetails(pdDetailsVO, images);
		} catch (IOException e) {
			model.addAttribute("errorMessage", "圖片處理時發生錯誤：" + e.getMessage());
			return "back_end/addProducts"; // 回到新增商品頁面
		}

		// 成功提交後重導到顯示所有商品的頁面，或者其他指定頁面
		List<PdDetailsVO> list = pdDetailsSvc.getAll();
		model.addAttribute("pdDetailsList", list);
		model.addAttribute("success", "- (新增成功)");
		return "redirect:/pdDetails/listAllProducts";
	}
	
	@GetMapping("listAllProducts")
	public String listAllProducts(Model model, HttpSession session) {
		SaveSession(model, session);
	    // 使用 service 的 getAll 方法取得所有產品
	    List<PdDetailsVO> list = pdDetailsSvc.getAll();
	    List<PdCategVO> categoryList = pdCategSvc.getAll();
//	    List<PdDetailsVO> list = pdDetailsSvc.getAllProductsWithCategory();
	    model.addAttribute("pdDetailsList", list); // 將資料傳給前端
	    model.addAttribute("categories", categoryList);
	    return "back_end/listAllProducts"; // 返回對應的 Thymeleaf 頁面
	}
	
	@GetMapping("listAllProductsbyStatus")
	public String listAllProducts2(Model model, HttpSession session) {
		SaveSession(model, session);
		// 調用 Service 層方法，獲取按狀態分組的產品數據
	    Map<String, List<PdDetailsVO>> groupedByStatus = pdDetailsSvc.getProductsGroupedByStatus();
	    List<PdCategVO> categoryList = pdCategSvc.getAll();
//	    List<PdDetailsVO> list = pdDetailsSvc.getAllProductsWithCategory();
	    model.addAttribute("groupedByStatus", groupedByStatus); // 將資料傳給前端
	    model.addAttribute("categories", categoryList);
	    return "back_end/listAllProductsbyStatus"; // 返回對應的 Thymeleaf 頁面
	}

	@PostMapping("getOne_For_Update")
	public String getOne_For_Update(@RequestParam("pdId") Integer pdId, ModelMap model , HttpSession session) {
		SaveSession(model, session);
		// 查詢商品資訊，包括圖片和分類 -- 使用這個的getOneProduct方法取出【單一】商品資料
		PdDetailsVO pdDetailsVO = pdDetailsSvc.getOneProduct(pdId);

		// 準備下拉選單的商品類別列表 -- 使用這個的getAll方法取出【所有】商品類別
		List<PdCategVO> categoryList = pdCategSvc.getAll();

		// 確保圖片集合正確傳遞
		model.addAttribute("pdDetailsVO", pdDetailsVO); // 商品基本資訊
		model.addAttribute("categoryList", categoryList); // 類別下拉選單資料
		model.addAttribute("productImages", pdDetailsVO.getPdImgVO()); // 商品圖片集合 -- 因為已經得到【單一】商品資料所以可以再取出其圖片

		return "back_end/update_pdDetails_input";
	}

	@PostMapping("update")
	public String update(@Valid PdDetailsVO pdDetailsVO, BindingResult result, ModelMap model,
			@RequestParam("upFiles") MultipartFile[] parts,
			@RequestParam(value = "imgIdsToDelete", required = false) List<Integer> imgIdsToDelete, HttpSession session) throws IOException {
		SaveSession(model, session);
		
		// 1. 處理輸入錯誤
		if (result.hasErrors()) {
			model.addAttribute("pdDetailsVO", pdDetailsVO);
			return "back_end/update_pdDetails_input";
		}

		// 2. 調用服務層邏輯
		pdDetailsSvc.updateProductWithImages(pdDetailsVO, parts, imgIdsToDelete);

		// 3. 準備返回
		model.addAttribute("success", "商品資料修改成功！");
		return "redirect:/pdDetails/listAllProducts";
	}

	@GetMapping("/getProductImages")
	public ResponseEntity<List<Integer>> getProductImages(@RequestParam("pdId") Integer pdId) {
		PdDetailsVO pdDetailsVO = new PdDetailsVO(); // 創建空的 PdDetailsVO 物件
		pdDetailsVO.setPdId(pdId); // 設置 pdId

		List<PdImgVO> images = pdImgRepository.findByPdDetailsVO(pdDetailsVO);
		if (images == null || images.isEmpty()) {
			return ResponseEntity.ok(Collections.emptyList());
		}
		List<Integer> imgIds = images.stream().map(PdImgVO::getPdImgId).collect(Collectors.toList());
		return ResponseEntity.ok(imgIds);
	}

//	/*
//	 * 第一種作法 Method used to populate the List Data in view. 如 : <form:select
//	 * path="deptno" id="deptno" items="${deptListData}" itemValue="deptno"
//	 * itemLabel="dname" />
//	 */
	@ModelAttribute("pdCategList")
	protected List<PdCategVO> referenceListData() {
		// DeptService deptSvc = new DeptService();
		List<PdCategVO> list = pdCategSvc.getAll();
		return list;
	}

//	去除BindingResult中某個欄位的FieldError紀錄
	public BindingResult removeFieldError(PdDetailsVO pdDetailsVO, BindingResult result, String removedFieldname) {
		List<FieldError> errorsListToKeep = result.getFieldErrors().stream()
				.filter(fieldname -> !fieldname.getField().equals(removedFieldname)).collect(Collectors.toList());
		result = new BeanPropertyBindingResult(pdDetailsVO, "pdDetailsVO");
		for (FieldError fieldError : errorsListToKeep) {
			result.addError(fieldError);
		}
		return result;
	}

	@GetMapping("/allProductsHomepage")
	public String showProducts(Model model) {
		// 從服務層獲取所需數據
		List<PdDetailsVO> products = pdDetailsSvc.getAllProductsWithDefaultImages(); // 獲取商品列表及圖片映射
		
	    // 過濾 pdStatus == 1 的商品
	    List<PdDetailsVO> filteredProducts = products.stream()
	            .filter(productStatus -> "1".equals(productStatus.getPdStatus())) // 過濾條件
	            .collect(Collectors.toList());
		
		List<PdCategVO> categoryList = pdCategSvc.getAll(); // 獲取所有分類
		Map<Integer, Integer> defaultImages = pdDetailsSvc.getDefaultImagesMap(products); // 從服務層獲取預設圖片映射

		// 將數據添加到模型中
		model.addAttribute("pdDetailsList", filteredProducts);
		model.addAttribute("defaultImages", defaultImages);
		model.addAttribute("categoryList", categoryList); // 添加分類列表

//		return "front_end/pdDetails/allProductsHomepage";
		return "front_end/allProductsHomepage";
	}
	
	@GetMapping("/getProductDetails")
	@ResponseBody
	public Map<String, Object> getProductDetails(@RequestParam("pdId") Integer pdId) {
	    // 1. 取得單一商品資訊
	    PdDetailsVO pdDetailsVO = pdDetailsSvc.getOneProduct(pdId);

	    // 2. 取得該商品的所有圖片
	    List<PdImgVO> productImages = pdImgSvc.getImagesByProductId(pdId);

	    // 3. 將商品資訊與圖片列表組合為 JSON 格式返回
	    Map<String, Object> response = new HashMap<>();
	    response.put("pdName", pdDetailsVO.getPdName());
	    response.put("pdDescr", pdDetailsVO.getPdDescr());
	    response.put("pdWeight", pdDetailsVO.getPdWeight());
	    response.put("pdSize", pdDetailsVO.getPdSize());
	    response.put("pdColor", pdDetailsVO.getPdColor());
	    response.put("images", productImages.stream()
	            .map(PdImgVO::getPdImgId) // 只返回圖片的 ID，方便生成圖片 URL
	            .collect(Collectors.toList()));

	    return response;
	}

}