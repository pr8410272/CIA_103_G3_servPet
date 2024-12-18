package com.servPet.ps.controller;

import java.io.IOException;
import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.servPet.meb.model.MebService;
import com.servPet.meb.model.MebVO;
import com.servPet.pet.model.PetRepository;
import com.servPet.pet.model.PetVO;
import com.servPet.ps.model.PsRepository;
import com.servPet.ps.model.PsService;
import com.servPet.ps.model.PsVO;
import com.servPet.psOrder.model.PsOrderRepository;
import com.servPet.psOrder.model.PsOrderService;
import com.servPet.psOrder.model.PsOrderVO;
import com.servPet.psPor.model.PsPorService;
import com.servPet.psSvc.model.PsSvcRepository;
import com.servPet.psSvc.model.PsSvcService;
import com.servPet.psSvc.model.PsSvcVO;
import com.servPet.psSvcItem.model.PsSvcItemService;
import com.servPet.psSvcItem.model.PsSvcItemVO;
import com.servPet.vtr.model.VtrService;
import com.servPet.vtr.model.VtrVO;

@Controller
@RequestMapping("/ps")
public class PsController {

	@Autowired
	private PsService psService;
	@Autowired
	private PsOrderRepository psOrderRepository;
	@Autowired
	private PsOrderService psOrderService;
	@Autowired
	private PsPorService psPorService;
	@Autowired
	private PsSvcRepository psSvcRepository;
	@Autowired
	private PsSvcService psSvcService;
	@Autowired
	private PsSvcItemService psSvcItemService;
	@Autowired
	private PsRepository psRepository;
	@Autowired
	private PetRepository petRepository;
	@Autowired
	private MebService mebService;
	@Autowired
	private VtrService vtrSvc;

	// 顯示保母列表
	@GetMapping("/psList")
	public String listPs(Model model) {
		List<PsVO> psList = psService.getAll();
		System.out.println("psList: " + psList);
		model.addAttribute("psList", psService.getAll());
		return "front_end/psList";
	}

//    //保母個人頁面(可編輯) 可用
//    @GetMapping("/profile/{psId}")
//    public String listOnePs(@PathVariable("psId") Integer psId, Model model) {
//        PsVO psVO = psService.getOnePs(psId);
//        model.addAttribute("psVO", psVO);
//
//
//
//
//        return "back_end/listOnePs";
//    }

	@GetMapping("/profile/{psId}")
	public String listOnePs(@PathVariable("psId") Integer psId, Model model) {
		// 獲取保母的詳細資訊
		PsVO psVO = psService.getOnePs(psId);
		model.addAttribute("psVO", psVO);

		// 獲取保母的圖片 ID 列表
		List<String> picIdList = psPorService.getPictureIdsByPsId(psId);
		model.addAttribute("picIdList", picIdList);

		// 獲取保母的服務項目列表
		List<PsSvcVO> svcList = psSvcService.getServicesByPsId(psId);
		model.addAttribute("svcList", svcList);

		return "back_end/listOnePs"; // 返回 Thymeleaf 頁面
	}

	// 會員看保母的個人頁面
	@GetMapping("/goodPs/{psId}")
	public String listOnePsMeb(@PathVariable("psId") Integer psId, Model model) {
		// 獲取保母的詳細資訊
		PsVO psVO = psService.getOnePs(psId);
		model.addAttribute("psVO", psVO);

		// 獲取保母的圖片 ID 列表
		List<String> picIdList = psPorService.getPictureIdsByPsId(psId);
		model.addAttribute("picIdList", picIdList);

		// 獲取保母的服務項目列表
		List<PsSvcVO> svcList = psSvcService.getServicesByPsId(psId);
		model.addAttribute("svcList", svcList);

		return "front_end/listOnePsMeb"; // 返回 Thymeleaf 頁面
	}

	@PostMapping("/edit/{psId}")
	public String showEditForm(@PathVariable("psId") Integer psId, Model model) {
		PsVO psVO = psService.getOnePs(psId);
		model.addAttribute("psVO", psVO);
		return "back_end/psEdit";
	}

	// 保存修改資料
	@PostMapping("/update")
	public String updatePs(@ModelAttribute("psVO") PsVO psVO) {
//        System.out.println(psVO.getSchDate());
		System.out.println("schDate = " + psVO.getSchDate());
		char[] schDate = { '0', '0', '0', '0', '0', '0', '0' };
		char[] schTime = { '0', '0', '0' };
		List<String> schDateList = Arrays.asList(psVO.getSchDate().split(","));
		List<String> schTimeList = Arrays.asList(psVO.getSchTime().split(","));
		// 將勾選的值標記為 '1'
		for (String day : schDateList) {
			int index = Integer.parseInt(day);
			schDate[index] = '1';
		}
		for (String day : schTimeList) {
			int index = Integer.parseInt(day);
			schTime[index] = '1';
		}
		System.out.println(new String(schDate));
		// 將 char array 轉為字串並設置到 psVO
		psVO.setSchDate(new String(schDate));
		psVO.setSchTime(new String(schTime));
//        System.out.println("schDate" + Arrays.toString(schDate));

		psService.update(psVO);

		return "redirect:/ps/profile/" + psVO.getPsId();
	}

//    // 處理多張作品集上傳
//    @PostMapping("/uploadpicture/{psId}")
//    public ResponseEntity<String> uploadPictures(@PathVariable Integer psId,
//                                                 @RequestParam("pictures") List<MultipartFile> files) {
//        try {
//            List<byte[]> pictures = new ArrayList<>();
//            for (MultipartFile file : files) {
//                pictures.add(file.getBytes());
//            }
//            psPorService.savePictures(psId, pictures);
//            return ResponseEntity.ok("圖片上傳成功！");
//        } catch (IOException e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("圖片上傳失敗！");
//        }
//    }

	// 處理多張作品擊上傳2
	@PostMapping("/uploadpicture/{psId}")
	public String uploadPictures(@PathVariable Integer psId, @RequestParam("pictures") List<MultipartFile> files,
			RedirectAttributes redirectAttributes) {
		try {
			List<byte[]> pictures = new ArrayList<>();
			for (MultipartFile file : files) {
				pictures.add(file.getBytes());
			}
			psPorService.savePictures(psId, pictures);
			redirectAttributes.addFlashAttribute("message", "圖片上傳成功！");
			redirectAttributes.addFlashAttribute("alertType", "success");
		} catch (IOException e) {
			redirectAttributes.addFlashAttribute("message", "圖片上傳失敗！");
			redirectAttributes.addFlashAttribute("alertType", "error");
		}
		return "redirect:/ps/profile/" + psId;
	}

	// 保母修改服務項目頁面
	@GetMapping("/psSvcItemlist/{psId}")
	public String listAllPsSvcItem(@PathVariable("psId") Integer psId, Model model) {
		List<PsSvcItemVO> list = psSvcItemService.getAll();
		model.addAttribute("list", list);
		System.out.println("list = " + list);
		model.addAttribute("psId", psId);
		return "back_end/psSvcEdit";
	}

	@PostMapping("/psSvcItem/update")
	@ResponseBody
	public Map<String, String> getSvcDescr(@RequestParam("svcId") Integer svcId) {
		System.out.println("Received svcId: " + svcId); // 調試輸出 svcId
		PsSvcItemVO svcItem = psSvcItemService.getSvcItemById(svcId); // 查詢數據
		Map<String, String> response = new HashMap<>();
		if (svcItem != null) {
			response.put("svcDescr", svcItem.getSvcDescr()); // 返回描述
		} else {
			response.put("svcDescr", "無描述");
		}
		System.out.println("Response: " + response); // 調試輸出返回數據
		return response;
	}

	// 更新保母的服務項目
	@PostMapping("/updateSvc")
	public String updatePsSvc(@ModelAttribute("psSvcVO") PsSvcVO psSvcVO, @RequestParam("psId") Integer psId,
			@RequestParam("svcId") List<Integer> svcIds, @RequestParam("svcPrice") List<Integer> svcPrices) {
		System.out.println("updatePsSvc: ");
		// 調用 Service 方法來更新保母的服務項目
		psSvcService.updateServicesForSitter(psId, svcIds, svcPrices);
		return "redirect:/ps/profile/" + psId;
	}

	// ===<< 將ps存於 session 中一起送出 >>===/
//    public String SaveSession(Model model, HttpSession session) {
//        PsVO ps = (PsVO) session.getAttribute("psVO");
//        model.addAttribute("psVO", new PsVO());
//        model.addAttribute("ps", ps);
//        return null;
//    }
	public String SaveSession(ModelMap model, HttpSession session) {
		PsVO ps = (PsVO) session.getAttribute("psVO");
		model.addAttribute("psVO", new PsVO());
		model.addAttribute("ps", ps);
		return null;
	}

	// 獲取保母封面照
	@GetMapping("/profileimg")
	public ResponseEntity<byte[]> getCoverImage(@RequestParam("psId") Integer psId, ModelMap model,
			HttpSession session) {
		SaveSession(model, session);
		byte[] imageData = psRepository.findPicById(psId);

		if (imageData != null && imageData.length > 0) {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.IMAGE_JPEG); // 或 MediaType.IMAGE_PNG
			return new ResponseEntity<>(imageData, headers, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

	}

// 進入訂單頁面
	@GetMapping("/psOrder/{psId}")
	public String showOrderPage(@PathVariable("psId") Integer psId, Model model, Principal principal) {
		// 從 Session 獲取會員 ID

		String email = principal.getName();
		Optional<MebVO> memberOptional = mebService.findMemberByEmail(email);
		MebVO member = memberOptional.get();

		Integer mebId = member.getMebId();
		if (mebId == null) {
			throw new IllegalStateException("會員未登入");
		}
//        Integer mebId = 2001; // 假設會員 ID

		// 獲取會員的寵物列表和保母的服務項目
		List<PetVO> petList = petRepository.findByMebId(mebId);
		List<PsSvcVO> serviceList = psSvcRepository.findByPsSvcId_PsVO_PsId(psId);

		// 獲取會員的餘額
//        MebVO member = mebService.getMemberById(mebId);
		Double bal = member.getBal();
		Integer userBalance = bal != null ? bal.intValue() : 0;

		// 獲取保母的 schDate 和 schTime
		PsVO psVO = psService.getOnePs(psId);
		String schDate = psVO.getSchDate(); // 例如 "0001111"
		String schTime = psVO.getSchTime(); // 例如 "011"

		// 將數據傳遞到前端
		model.addAttribute("petList", petList);
		model.addAttribute("serviceList", serviceList);
		model.addAttribute("psId", psId);
		model.addAttribute("mebId", mebId);
		model.addAttribute("userBalance", userBalance); // 傳遞餘額
		model.addAttribute("schDate", schDate); // 傳遞保母的可服務天數
		model.addAttribute("schTime", schTime); // 傳遞保母的可服務時段
//        model.addAttribute("disabledDates", new ObjectMapper().writeValueAsString(disabledDates));

		return "front_end/psOrder"; // 返回 Thymeleaf 頁面
	}

//更新訂單
	@PostMapping("/updateOrder")
	public String createOrder(@ModelAttribute PsOrderVO orderVO,
			@RequestParam("psVO.psId") Integer psId,
			Principal principal,
	         @RequestParam("petVO.petId") Integer petId,
			@RequestParam("bookingDate") String bookingDate, @RequestParam("bookingTime") String bookingTime,
			@RequestParam(value = "svcId", required = true) Integer svcId,
			@RequestParam(value = "svcPrice", required = false) Integer svcPrice, Model model) {

		String email = principal.getName();
		Optional<MebVO> memberOptional = mebService.findMemberByEmail(email);

		MebVO member = memberOptional.get();
		
		Integer mebId = member.getMebId();
		
		
		
		if (svcPrice == null || svcPrice <= 0) {
			throw new IllegalArgumentException("服務價格必須是正整數，請檢查表單輸入");
		}

		try {
//            // 使用 mebService 獲取會員資料
////            MebVO member = mebService.getMemberById(mebId);
//            if (member == null) {
//                model.addAttribute("errorMessage", "會員不存在，無法提交訂單");
//                return "redirect:/ps/psOrder/" + psId;
//            }

			// 檢查會員餘額是否足夠
			if (member.getBal() < svcPrice) {
				model.addAttribute("errorMessage", "餘額不足，無法提交訂單");
				return "redirect:/ps/psOrder/" + psId;
			}
			// 設置訂單其他值
			PsVO psVO = new PsVO();
			psVO.setPsId(psId);
			orderVO.setPsVO(psVO);

			MebVO mebVO = new MebVO();
			mebVO.setMebId(mebId); // 設定會員 ID
			orderVO.setMebVO(mebVO);

//			PetVO petVO = new PetVO();
//			petVO.setPetId(petId);
//			orderVO.setPetVO(petVO);
			
	        PetVO petVO = petRepository.findById(petId)
	                .orElseThrow(() -> new IllegalArgumentException("寵物不存在，請重新選擇"));
	
			orderVO.setPetVO(petVO);

			orderVO.setBookingDate(LocalDate.parse(bookingDate));
			orderVO.setBookingTime(bookingTime);
			orderVO.setSvcId(svcId);
			orderVO.setSvcPrice(svcPrice);
			orderVO.setBookingStatus("0"); // 預設未完成
			orderVO.setApprStatus("0"); // 預設未完成

			// 扣減會員餘額
			mebService.deductBalance(mebId, svcPrice.doubleValue());
			System.out.println(member.getBal());
			// 新增訂單
			psOrderService.createOrder(orderVO);

			// 新增交易紀錄 (可選)
			VtrVO record = new VtrVO();
			record.setMebId(mebId);
			record.setMoney((int) svcPrice.doubleValue()); // 設定為負值，表示扣款
			record.setTransactionType("扣款"); // 設定交易類型為 "1" (扣款)
			record.setCreateTime(LocalDateTime.now()); // 設定創建時間
			vtrSvc.addRecord(record);

			return "redirect:/ps/goodPs/" + psId;
		} catch (Exception e) {
			model.addAttribute("errorMessage", "新增訂單失敗：" + e.getMessage());
			e.printStackTrace();
			return "/front_end/psList";
		}
	}

	@GetMapping("/bookedSlots/{psId}")
	@ResponseBody
	public Map<String, List<String>> getBookedSlots(@ModelAttribute PsVO psVO, @PathVariable Integer psId) {
		List<PsOrderVO> orders = psOrderService.getOrdersByPsId(psId);

		Map<String, List<String>> bookedSlots = new HashMap<>();

		for (PsOrderVO order : orders) {
			String date = order.getBookingDate().toString();
			String time = order.getBookingTime(); // "morning", "afternoon", "evening"

			bookedSlots.putIfAbsent(date, new ArrayList<>());
			bookedSlots.get(date).add(time);
		}

		return bookedSlots;
	}

	@GetMapping("/picture/{picId}")
	public ResponseEntity<byte[]> getPicture(@PathVariable String picId) {
		byte[] imageBytes = psPorService.getPictureById(picId);
		return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(imageBytes);
	}

	// 刪除圖片
	@GetMapping("/deletePicture")
	public String deletePicture(@RequestParam("picId") Integer picId, @RequestParam("psId") Integer psId,
			RedirectAttributes redirectAttributes) {
		try {
			psPorService.deletePictureById(picId); // 呼叫服務層刪除圖片
			redirectAttributes.addFlashAttribute("message", "圖片已成功刪除！");
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("errorMessage", "圖片刪除失敗，請稍後再試！");
		}
		// 重定向回美容師詳細頁面

		return "redirect:/ps/profile/" + psId;
	}

}
