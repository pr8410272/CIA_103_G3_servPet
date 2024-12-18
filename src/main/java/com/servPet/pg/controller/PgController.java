package com.servPet.pg.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.servPet.meb.model.MebService;
import com.servPet.meb.model.MebVO;
import com.servPet.pg.model.PgRepository;
import com.servPet.pg.model.PgService;
import com.servPet.pg.model.PgVO;
import com.servPet.pgOrder.model.AvailableSlot;
import com.servPet.pgOrder.model.PgOrderService;
import com.servPet.pgPic.model.PgPicRepository;
import com.servPet.pgPic.model.PgPicService;
import com.servPet.pgSvc.model.PgServiceDetailsDTO;
import com.servPet.pgSvc.model.PgSvcService;

@Controller
@RequestMapping("/pg")
public class PgController {

	@Autowired
	private PgService pgSvc;

	@Autowired
	private PgPicService pgPicSvc;

	@Autowired
	private PgSvcService pgSvcSvc;
	
	@Autowired
	private MebService mebSvc;

	@Autowired
	private PgOrderService pgOrderSvc;

	@Autowired
	private PgRepository pgRepository;

	@Autowired
	private PgPicRepository pgPicRepository;

	// 公共方法來檢查 pg是否存在於 session 中
	private boolean isPgLoggedIn(HttpSession session) {
		return session.getAttribute("pgVO") != null;
	}

	// ===<< 將pg存於 session 中一起送出 >>===/
	public String SaveSession(Model model, HttpSession session) {
		PgVO pg = (PgVO) session.getAttribute("pgVO");
		model.addAttribute("pgVO", new PgVO());
		model.addAttribute("pg", pg);
		return null;
	}

	// ===<< 將pg存於 session 中一起送出 >>===/
	public String SaveSession(ModelMap model, HttpSession session) {
		PgVO pg = (PgVO) session.getAttribute("pgVO");
		model.addAttribute("pgVO", new PgVO());
		model.addAttribute("pg", pg);
		return null;
	}

	// 瀏覽單一美容師詳情
	@GetMapping("/listOnePg_back")
	public String listOnePgBack(@RequestParam("pgId") Integer pgId, ModelMap model, HttpSession session) {
		SaveSession(model, session);
		PgVO pgVO = pgSvc.getOnePg(Integer.valueOf(pgId));

		if (pgVO == null) {
			model.addAttribute("errorMessage", "查無資料");
			return "front_end/pg/select_page";
		}

		model.addAttribute("pgVO", pgVO);
		model.addAttribute("getOne_For_Display", "true"); 

		List<Integer> picId = pgPicRepository.getPictureIdsByPgId(pgId);
		byte[] picture = pgPicSvc.getPictureById(pgId);

		model.addAttribute("picId", picId);
		model.addAttribute("picture", picture);
		
		// 計算平均星數
	    int totalStars = pgVO.getTotalStars();
	    int ratingTimes = pgVO.getRatingTimes();
	    double averageStars = (ratingTimes > 0) ? (double) totalStars / ratingTimes : 0;
	    long roundedStars = Math.round(averageStars); // 四捨五入
	    
	    model.addAttribute("averageStars", roundedStars);

		// 獲取美容師的服務清單及詳細資料
		List<PgServiceDetailsDTO> serviceDetails = pgSvcSvc.getServicesWithDetails(pgId);
		// 按照服務名稱和簡介統整，避免重複項目
		Map<String, List<PgServiceDetailsDTO>> groupedServices = serviceDetails.stream()
				.collect(Collectors.groupingBy(dto -> dto.getSvcName()));

		model.addAttribute("groupedServices", groupedServices);

		return "back_end/pg/pgProfile";
	}

	// 獲取待修改美容師個人資訊
	@GetMapping("/getOne_For_Update")
	public String getOne_For_Update(@RequestParam("pgId") Integer pgId, ModelMap model, HttpSession session) {
		SaveSession(model, session);
		PgVO pgVO = pgSvc.getOnePg(Integer.valueOf(pgId));
		List<Integer> picId = pgPicRepository.getPictureIdsByPgId(pgId);
		byte[] picture = pgPicSvc.getPictureById(pgId);

		model.addAttribute("picId", picId);
		model.addAttribute("picture", picture);
		model.addAttribute("pgVO", pgVO);
		model.addAttribute("pgId", pgId);
		
		return "back_end/pg/updatePgDetail";
	}

	@PostMapping("/update")
	public String updatePg(@ModelAttribute("pgVO") PgVO pgVO,
			@RequestParam(value = "pgPicFile", required = false) MultipartFile pgPicFile, ModelMap model,
			HttpSession session) {
		SaveSession(model, session);
		// 如果有新上傳的圖片，更新 pgPic 欄位
		if (pgPicFile != null && !pgPicFile.isEmpty()) {
			try {
				// 轉換為 byte[] 並設置 pgPic
				byte[] pgPicBytes = pgPicFile.getBytes();
				pgVO.setPgPic(pgPicBytes);
			} catch (IOException e) {
				e.printStackTrace();
				return "error"; // 如果文件處理出錯，返回錯誤頁面
			}
		} else {
			// 如果沒有圖片變更，保留原本的 pgPic
			PgVO existingPgVO = pgSvc.getOnePg(pgVO.getPgId());
			if (existingPgVO != null && existingPgVO.getPgPic() != null) {
				pgVO.setPgPic(existingPgVO.getPgPic());
			}
		}

		System.out.println("schDate = " + pgVO.getSchDate());
		char[] schDate = { '0', '0', '0', '0', '0', '0', '0' };
		char[] schTime = { '0', '0', '0' };
		List<String> schDateList = Arrays.asList(pgVO.getSchDate().split(","));
		List<String> schTimeList = Arrays.asList(pgVO.getSchTime().split(","));
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
		pgVO.setSchDate(new String(schDate));
		pgVO.setSchTime(new String(schTime));

		pgSvc.updatePg(pgVO);
		return "redirect:/pg/getOne_For_Update?pgId=" + pgVO.getPgId();
	}

	// 瀏覽所有美容師
	@GetMapping
	public String selectPage(ModelMap model, HttpSession session) {
		SaveSession(model, session);
		// 獲取所有美容師資料
	    List<PgVO> pgList = pgSvc.getAllOnDuty();
	    
	 // 計算平均星等並生成星號字符串
	    Map<Integer, String> starDisplayMap = new HashMap<>();
	    for (PgVO pg : pgList) {
	        Integer pgId = pg.getPgId();
	        int averageStars;

	        // 計算平均星等
	        if (pg.getRatingTimes() == null || pg.getRatingTimes() == 0) {
	            averageStars = 0; // 無評價時星等設置為 0
	        } else {
	            averageStars = (int) Math.round((double) pg.getTotalStars() / pg.getRatingTimes());
	        }

	        // 生成星號字符串
	        StringBuilder stars = new StringBuilder();
	        for (int i = 0; i < Math.max(0, averageStars); i++) {
	            stars.append("⭐");
	        }

	        starDisplayMap.put(pgId, stars.toString());
	    }

	    
	    model.addAttribute("list", pgList);
	    model.addAttribute("starDisplayMap", starDisplayMap);
		return "front_end/pg/select_pg_page"; 
	}
	



	// 獲取美容師封面照
	@GetMapping("/profileimg")
	public ResponseEntity<byte[]> getCoverImage(@RequestParam("pgId") Integer pgId, ModelMap model,
			HttpSession session) {
		SaveSession(model, session);
		byte[] imageData = pgRepository.findPicById(pgId);

		if (imageData != null && imageData.length > 0) {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.IMAGE_JPEG); // 或 MediaType.IMAGE_PNG
			return new ResponseEntity<>(imageData, headers, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	// 瀏覽單一美容師詳情
	@GetMapping("/listOnePg")
	public String listOnePg(@RequestParam("pgId") Integer pgId,
			@RequestParam(value = "mebId", required = false) Integer mebId,
			ModelMap model, HttpSession session) {
			SaveSession(model, session);
		PgVO pgVO = pgSvc.getOnePg(Integer.valueOf(pgId));

		if (pgVO == null) {
			model.addAttribute("errorMessage", "查無資料");
			return "front_end/pg/select_pg_page";
		}
		MebVO member = mebSvc.getMemberById(mebId);

		model.addAttribute("pgVO", pgVO);
		model.addAttribute("member", member);
		model.addAttribute("getOne_For_Display", "true");

		List<Integer> picId = pgPicRepository.getPictureIdsByPgId(pgId);
		byte[] picture = pgPicSvc.getPictureById(pgId);

		model.addAttribute("picId", picId);
		model.addAttribute("picture", picture);

		// 獲取美容師的服務清單及詳細資料
		List<PgServiceDetailsDTO> serviceDetails = pgSvcSvc.getServicesWithDetails(pgId);
		// 按照服務名稱和簡介統整，避免重複項目
		Map<String, List<PgServiceDetailsDTO>> groupedServices = serviceDetails.stream()
				.collect(Collectors.groupingBy(dto -> dto.getSvcName()));

		model.addAttribute("groupedServices", groupedServices);
		
		
		// 計算平均星數
	    int totalStars = pgVO.getTotalStars();
	    int ratingTimes = pgVO.getRatingTimes();
	    double averageStars = (ratingTimes > 0) ? (double) totalStars / ratingTimes : 0;
	    long roundedStars = Math.round(averageStars); // 四捨五入
	    
	    model.addAttribute("averageStars", roundedStars);

		// 計算可預約時段
		LocalDate startDate = LocalDate.now();
		LocalDate endDate = startDate.plusWeeks(4); // 顯示未來四周
		List<AvailableSlot> availableSlots = pgOrderSvc.getAvailableSlots(pgVO, startDate, endDate);

		// 使用 ObjectMapper 將可預約時段序列化為 JSON
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());
		objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS); // 格式化為 ISO 標準

		try {
			String availableSlotsJson = objectMapper.writeValueAsString(availableSlots);
			model.addAttribute("availableSlotsJson", availableSlotsJson);
		} catch (JsonProcessingException e) {
			model.addAttribute("errorMessage", "無法序列化數據");
			return "error";
		}

		model.addAttribute("pgVO", pgVO);

		return "front_end/pg/listOnePg";
	}

	// 去除BindingResult中某個欄位的FieldError紀錄
	public BindingResult removeFieldError(PgVO pgVO, BindingResult result, String removedFieldname) {
		List<FieldError> errorsListToKeep = result.getFieldErrors().stream()
				.filter(fieldname -> !fieldname.getField().equals(removedFieldname)).collect(Collectors.toList());
		result = new BeanPropertyBindingResult(pgVO, "pgVO");
		for (FieldError fieldError : errorsListToKeep) {
			result.addError(fieldError);
		}
		return result;
	}

}
