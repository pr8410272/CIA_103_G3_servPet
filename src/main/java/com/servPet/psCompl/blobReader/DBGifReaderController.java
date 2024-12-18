package com.servPet.psCompl.blobReader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.*;
import java.util.Base64;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.sql.DataSource;

import com.servPet.pgCompl.model.PgComplVO;
import com.servPet.psCompl.model.PsComplService;
import com.servPet.psCompl.model.PsComplVO;

@Controller("psComplDBGifReaderController")
@RequestMapping("/psCompl")
public class DBGifReaderController {
	
	@Autowired
	PsComplService psComplSvc;
	
	/*
	 * This method will serve as listOneEmp.html , listAllEmp.html handler.
	 */
	@GetMapping("/PsDBGifReader")
	public void dBGifReader(@RequestParam String psComplId, @RequestParam int fileIndex, HttpServletResponse res) {
	    res.setContentType("image/gif");
	    try (ServletOutputStream out = res.getOutputStream()) {
	        PsComplVO psComplVO = psComplSvc.getOnePsCompl(Integer.valueOf(psComplId));
	        byte[] imageBytes = null;
	        
	        // 根據 fileIndex 決定返回哪張圖片
	        switch (fileIndex) {
	            case 1:
	                imageBytes = psComplVO.getPsComplUpfiles1();  // 獲取圖片1
	                break;
	            case 2:
	                imageBytes = psComplVO.getPsComplUpfiles2();  // 獲取圖片2
	                break;
	            case 3:
	                imageBytes = psComplVO.getPsComplUpfiles3();  // 獲取圖片3
	                break;
	            case 4:
	                imageBytes = psComplVO.getPsComplUpfiles4();  // 獲取圖片4
	                break;
	            default:
	                break;
	        }
	        
	        if (imageBytes != null) {
	            out.write(imageBytes);
	        } else {
	            // 如果圖片不存在，返回錯誤圖片或處理異常
	            byte[] errorImageBytes = Base64.getDecoder().decode("...");  // 你的錯誤圖片的 Base64 字符串
	            out.write(errorImageBytes);
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	        // 處理異常情況
	    }
	}
}