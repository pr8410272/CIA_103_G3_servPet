package com.servPet.pgPic.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.servPet.pgPic.model.PgPicRepository;
import com.servPet.pgPic.model.PgPicService;

@Controller
@Validated
@RequestMapping("/pg")
public class PgPicController {

	@Autowired
	private PgPicService pgPicSvc;

	@Autowired
	private PgPicRepository pgPicRepository;

	// 刪除圖片
	@GetMapping("/deletePicture")
	public String deletePicture(@RequestParam("picId") Integer picId, @RequestParam("pgId") Integer pgId,
			RedirectAttributes redirectAttributes) {
		try {
			pgPicSvc.deletePictureById(picId); // 呼叫服務層刪除圖片
			redirectAttributes.addFlashAttribute("message", "圖片已成功刪除！");
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("errorMessage", "圖片刪除失敗，請稍後再試！");
		}
		// 重定向回美容師詳細頁面

		return "redirect:/pg/getOne_For_Update?pgId=" + pgId;
	}

	// 處理多張作品集上傳
	@PostMapping("/uploadpicture/{pgId}")
	public String uploadPictures(@PathVariable Integer pgId, @RequestParam("pictures") List<MultipartFile> files) {
		try {
			List<byte[]> pictures = new ArrayList<>();
			for (MultipartFile file : files) {
				pictures.add(file.getBytes());
			}
			pgPicSvc.savePictures(pgId, pictures);
			return "redirect:/pg/getOne_For_Update?pgId=" + pgId;
		} catch (IOException e) {
			return "redirect:/pg/getOne_For_Update?pgId=" + pgId;
		}
	}

	// 透過作品集ID找到圖檔
	@GetMapping("/picture/{picId}")
	public ResponseEntity<byte[]> getPicture(@PathVariable Integer picId) {
		byte[] picture = pgPicRepository.findPictureById(picId);

		if (picture == null) {
			return ResponseEntity.notFound().build();
		}

		return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(picture);
	}

}
