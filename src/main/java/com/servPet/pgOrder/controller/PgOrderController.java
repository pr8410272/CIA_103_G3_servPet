package com.servPet.pgOrder.controller;

import java.security.Principal;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.servPet.meb.model.MebService;
import com.servPet.meb.model.MebVO;
import com.servPet.pet.model.PetService;
import com.servPet.pet.model.PetVO;
import com.servPet.pg.model.PgService;
import com.servPet.pg.model.PgVO;
import com.servPet.pgOrder.model.PgOrderDTO;
import com.servPet.pgOrder.model.PgOrderService;
import com.servPet.pgOrder.model.PgOrderVO;
import com.servPet.pgSvc.model.PgServiceDetailsDTO;
import com.servPet.pgSvc.model.PgSvcService;
import com.servPet.vtr.model.VtrService;
import com.servPet.vtr.model.VtrVO;

@Controller
@RequestMapping("/pg")
public class PgOrderController {

	@Autowired
	private PgOrderService pgOrderSvc;

	@Autowired
	private PgSvcService PgSvcSvc;

	@Autowired
	private PgService pgSvc;

	@Autowired
	private MebService mebSvc;

	@Autowired
	private PetService petSvc;
	
	@Autowired
	private VtrService vtrSvc;
	


	// 下單
	@PostMapping("/createOrder")
	public String createOrder(@RequestParam("pgId") Integer pgId,
			Principal principal,
//			@RequestParam("mebId") Integer mebId, 
			@RequestParam("petId") Integer petId, 
			@RequestParam("bookingDate") String bookingDate,
			@RequestParam("bookingTime") String bookingTime, 
			@RequestParam("pgSvcId") Integer pgSvcId,
			RedirectAttributes redirectAttributes) {

		// 驗證時段是否已被預約
		boolean isAvailable = pgOrderSvc.isSlotAvailable(pgId, Date.valueOf(bookingDate), bookingTime);
		if (!isAvailable) {
			redirectAttributes.addFlashAttribute("errorMessage", "該時段已被預約，請選擇其他時段！");
			return "redirect:/pg/listOnePg?pgId=" + pgId;
		}
		String email = principal.getName();
		Optional<MebVO> memberOptional = mebSvc.findMemberByEmail(email);

		MebVO member = memberOptional.get();
		
		Integer mebId = member.getMebId();
		// 驗證會員餘額
//		MebVO member = mebSvc.getMemberById(mebId);
		PgServiceDetailsDTO services = PgSvcSvc.getById(pgSvcId);

		if (member.getBal() < services.getSvcPrice().doubleValue()) {
			redirectAttributes.addFlashAttribute("errorMessage", "餘額不足，請先儲值後再進行預約！");
			return "redirect:/pg/listOnePg?pgId=" + pgId; // 導向紹輔的儲值金畫面
		}

		// 扣除餘額並保存訂單
		PgOrderVO order = new PgOrderVO(mebId, pgId, petId, pgSvcId, LocalDate.parse(bookingDate), bookingTime,
				services.getSvcPrice());
		pgOrderSvc.addPgOrder(order);
		
		// 新增交易紀錄
	    VtrVO record = new VtrVO();
	    record.setMebId(mebId);
	    record.setMoney(services.getSvcPrice()); // 設定金額等於服務價格
	    record.setTransactionType("扣款"); // 設定交易類型為 "1" (扣款)
	    record.setCreateTime(LocalDateTime.now()); // 設定創建時間為當前時間
	    vtrSvc.addRecord(record);

		mebSvc.deductBalance(mebId, services.getSvcPrice().doubleValue());
		redirectAttributes.addFlashAttribute("successMessage", "預約成功！");
		return "redirect:/pg/listOnePg?pgId=" + pgId;
	}

	// 進入下單頁面帶出預設及會員資料
	@GetMapping("/pgOrderForm")
	public String pgOrderForm(@RequestParam("pgId") Integer pgId,
Principal principal,
@RequestParam("bookingDate") String bookingDate, 
			@RequestParam("bookingTime") String bookingTime,
			Model model) {
		// 加載美容師基本資料
		PgVO pg = pgSvc.getOnePg(pgId);
		if (pg == null) {
			model.addAttribute("errorMessage", "找不到該美容師");
			return "error";
		}
        String email = principal.getName();
        Optional<MebVO> memberOptional = mebSvc.findMemberByEmail(email);
        // 加載會員資料
		MebVO member = memberOptional.get();
		Integer mebId = member.getMebId();
		List<PetVO> pets = petSvc.getPetsByMebId(member.getMebId());
		// 加載美容師可提供的服務
		List<PgServiceDetailsDTO> services = pgOrderSvc.getAvailableServicesByPgId(pgId);
		 // 將 svcType 轉換為中文
	    for (PgServiceDetailsDTO service : services) {
	        switch (service.getSvcType()) {
	            case "0":
	                service.setSvcType("迷你（3kg以下）");
	                break;
	            case "1":
	                service.setSvcType("小型（3-5kg）");
	                break;
	            case "2":
	                service.setSvcType("中小型（5-10kg）");
	                break;
	            case "3":
	                service.setSvcType("中型（10-16kg）");
	                break;
	            case "4":
	                service.setSvcType("中大型（16-22kg）");
	                break;
	            case "5":
	                service.setSvcType("大型（22-27kg）");
	                break;
	            case "6":
	                service.setSvcType("特大型（27kg以上）");
	                break;
	            default:
	                service.setSvcType("未知類型");
	        }
	    }

		// 初始化數據傳遞給頁面
		model.addAttribute("pg", pg);
		model.addAttribute("mebId", mebId);
		model.addAttribute("member", member);
		model.addAttribute("pets", pets);
		model.addAttribute("services", services);
		model.addAttribute("bookingDate", bookingDate);
		model.addAttribute("bookingTime", bookingTime);

		return "front_end/pg/pgOrder";
	}

	// 查看訂單詳情
	@GetMapping("/pgOrderDetails")
	public String pgOrderDetails(@RequestParam("pgoId") Integer pgoId, ModelMap model) {
		// 查詢訂單詳情
		PgOrderVO orderDetails = pgOrderSvc.getOrderDetails(pgoId);
		// 查詢其它訂單詳情
		PgOrderDTO otherDetails = pgOrderSvc.getOrderDetailsWithMemberAndPet(pgoId);

		if (orderDetails == null) {
			model.addAttribute("errorMessage", "找不到該訂單詳細資訊");
			return "back_end/pg/pgOrderManagement"; // 返回訂單管理頁面
		}

		// 將訂單詳細資訊添加到模型中
		model.addAttribute("orderDetails", orderDetails);
		
		 // 如果有圖片數據，轉換為 Base64 格式
	    if (otherDetails.getPetImg() != null) {
	        String petImgBase64 = Base64.getEncoder().encodeToString(otherDetails.getPetImg());
	        model.addAttribute("petImgBase64", petImgBase64);
	    }
		// 將其它訂單詳細資訊添加到模型中
		model.addAttribute("otherDetails", otherDetails);

		// 返回訂單詳情頁面
		return "back_end/pg/orderDetail";
	}

	// 連結美容師個人資訊瀏覽訂單列表
	@GetMapping("/pgOrderManagement")
	public String pgOrderManagement(@RequestParam("pgId") Integer pgId, ModelMap model) {
		// 查詢美容師的基本資料
		PgVO pgVO = pgSvc.getOnePg(pgId);

		// 查詢屬於該美容師的訂單
		List<PgOrderVO> orders = pgOrderSvc.findOrderByPgId(pgId);

		// 將美容師資料和訂單列表添加到模型中
		model.addAttribute("pgVO", pgVO);
		model.addAttribute("orders", orders);

		// 返回對應的視圖頁面
		return "back_end/pg/pgOrderManagement";
	}

}

