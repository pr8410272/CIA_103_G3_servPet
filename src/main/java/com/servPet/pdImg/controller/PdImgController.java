package com.servPet.pdImg.controller;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.servPet.pdCateg.model.PdCategService;
import com.servPet.pdCateg.model.PdCategVO;
import com.servPet.pdDetails.model.PdDetailsService;
import com.servPet.pdDetails.model.PdDetailsVO;
import com.servPet.pdImg.model.PdImgService;

@Controller
@RequestMapping("/pdImg")
public class PdImgController {

	@Autowired
	PdDetailsService pdDetailsSvc;

	@Autowired
	PdCategService pdCategSvc;

	@Autowired
	PdImgService pdImgSvc;

	// 刪除圖片
    @DeleteMapping("/delete/{pdImgId}")
    public ResponseEntity<String> deleteImage(@PathVariable("pdImgId") Integer pdImgId) {
        try {
        	pdImgSvc.deleteImageById(pdImgId); // 調用刪除邏輯
            return ResponseEntity.ok("圖片刪除成功");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("刪除失敗");
        }
    }

}