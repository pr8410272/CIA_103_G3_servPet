package com.servPet.admin.model;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.servPet.admin.hibernateUtilCompositeQuery.HibernateUtil_CompositeQuery_admin;
import com.servPet.meb.model.MebRepository;
import com.servPet.meb.model.MebVO;
import com.servPet.pg.model.PgRepository;
import com.servPet.pg.model.PgVO;
import com.servPet.pgOrder.model.PgOrderRepository;
import com.servPet.pgOrder.model.PgOrderVO;
import com.servPet.ps.model.PsRepository;
import com.servPet.ps.model.PsVO;
import com.servPet.psOrder.model.PsOrderRepository;
import com.servPet.psOrder.model.PsOrderVO;


@Service("adminService")
public class AdminService {

    @Autowired
    AdminRepository adminrepository;

    @Autowired
    private SessionFactory sessionFactory;
    
    @Autowired
	MebRepository mebRepository;
    
	@Autowired
	PgRepository pgRepository;
	
	@Autowired
	PsRepository psRepository;
	
	@Autowired
	PgOrderRepository pgOrderRepository;
	
	@Autowired
	PsOrderRepository psOrderRepository;
	
	 @Autowired
	 private JavaMailSender mailSender;
	 
	 @Autowired
	    private TemplateEngine templateEngine;  // 注入 Thymeleaf 模板引擎

    // 添加管理員
    public void addAdmin(AdminVO adminVO) {
        adminrepository.save(adminVO);  // 直接保存管理員，無加密
    }

    // 更新管理員
    public void updateAdmin(AdminVO adminVO) {
        adminrepository.save(adminVO);  // 直接保存管理員，無加密
    }
    
    // 根據 adminId 查詢單一管理員
    public AdminVO getOneAdmin(Integer adminId) {
        Optional<AdminVO> optional = adminrepository.findById(adminId);
        return optional.orElse(null);  // 如果存在，返回該管理員，否則返回 null
    }

    // 查詢所有管理員資料
    public List<AdminVO> getAll() {
        return adminrepository.findAll();  // 返回所有管理員資料
    }

    // 複合查詢所有管理員資料
    public List<AdminVO> getAll(Map<String, String[]> map) {
        return HibernateUtil_CompositeQuery_admin.getAllC(map, sessionFactory.openSession());
    }

    // 管理員登錄
    public AdminVO login(String adminAcc, String adminPwd) {
        AdminVO admin = adminrepository.findByadminAcc(adminAcc);
        if (admin != null && adminPwd.equals(admin.getAdminPwd())) {  // 直接比對密碼
            return admin; // 驗證成功
        } else {
            return null; // 登入失敗
        }
    }
    
  //=======================<<會員>>=======================//
 // 更新會員狀態
    public void updateMebStatus(Integer mebId, String status) {
        MebVO mebVO = mebRepository.findMemberById(mebId);
        // 將傳入的 status 字符串轉換為 Integer
        mebVO.setMebStatus(status);
        mebRepository.save(mebVO);
    }
    
  //從meb資料庫中渲染圖片
    public ResponseEntity<byte[]> findMebImgByPsId(Integer mebId) {
        byte[] imageData = mebRepository.findMebImgByPsId(mebId);
        if (imageData != null && imageData.length > 0) {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG);  // 或 MediaType.IMAGE_PNG
            return new ResponseEntity<>(imageData, headers, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
  //=======================<< 美容師>>=======================//
  	// 更新美容師狀態
    public void updatePgStatus(Integer pgId, String status) {
        PgVO pgVO = pgRepository.findByPgId(pgId);
        pgVO.setPgStatus(status);
        pgRepository.save(pgVO);
    }
    
 // 更新 PS 的報告點數
    public void updatePgReportTimes(PgVO pgVO) {
        pgRepository.save(pgVO);
    }
    
    //從pg資料庫中渲染圖片
    public ResponseEntity<byte[]> getLicenseByPgId(Integer pgId) {
        byte[] imageData = pgRepository.findLicensesByPgId(pgId);
        if (imageData != null && imageData.length > 0) {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG);  // 或 MediaType.IMAGE_PNG
            System.out.println("Image Data Length: " + (imageData != null ? imageData.length : "No image found"));
            return new ResponseEntity<>(imageData, headers, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    //=======================<< 保母 >>=======================//
    	// 更新保母狀態
    public void updatePsStatus(Integer psId, String status) {
        PsVO psVO = psRepository.findByPsId(psId);  
        psVO.setPsStatus(status);
        psRepository.save(psVO);
    }
    
    	// 更新 PS 的報告點數
    public void updatePsReportTimes(PsVO psVO) {
        psRepository.save(psVO);
    }
    
    //從ps資料庫中渲染圖片
    public ResponseEntity<byte[]> getLicenseByPsId(Integer psId) {
        byte[] imageData = psRepository.findLicensesByPsId(psId);
        if (imageData != null && imageData.length > 0) {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG);  // 或 MediaType.IMAGE_PNG
            return new ResponseEntity<>(imageData, headers, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
  //=======================<< 美容師訂單 >>=======================//
    // 更新美容師訂單狀態
    public void updatePgOrderStatus(Integer pgoId, String status) {
        PgOrderVO pgOrderVO = pgOrderRepository.findByPgOrderId(pgoId);
        pgOrderVO.setApprStatus(status);
        pgOrderRepository.save(pgOrderVO);
    }
    
  //=======================<< 保母訂單 >>=======================//
    // 更新保母訂單狀態
    public void updatePsOrderStatus(Integer psOrderId, String status) {
        PsOrderVO psOrderVO = psOrderRepository.findByPsOrderId(psOrderId);
        psOrderVO.setApprStatus(status);
        psOrderRepository.save(psOrderVO);
    }
    
  //XXXXXXXXXXXXXXXXXXXXX< 會員停權通知 >XXXXXXXXXXXXXXXXXXXXX//
    public void sendSuspensionEmailMeb(MebVO meb) {
        try {
            // 使用 Thymeleaf 模板渲染郵件內容
            String templatePath = "back_end/admin/SuspensionEmailMeb"; // 你的 Thymeleaf 模板路徑

            // 使用 Thymeleaf 模板渲染郵件內容
            Context context = new Context();
            context.setVariable("mebName", meb.getMebName());  // 將會員名稱傳入模板
            context.setVariable("mebMail", meb.getMebMail());  // 如果需要郵件可以傳遞

            String content = templateEngine.process(templatePath, context); // 使用模板渲染內容

            // 設置發送郵件的收件人、主題及內容
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            String recipientEmail = meb.getMebMail();  // 確保從 MebVO 類中獲得正確的會員郵箱
            helper.setTo(recipientEmail);
            helper.setSubject("您的帳號已被停權");
            helper.setText(content, true);  // 使用渲染後的 HTML 內容

            // 發送郵件
            mailSender.send(message);

        } catch (MessagingException e) {
            e.printStackTrace();
            throw new RuntimeException("郵件發送失敗：" + e.getMessage(), e);
        }
    }
    
  //XXXXXXXXXXXXXXXXXXXXX< 美容師停權通知 >XXXXXXXXXXXXXXXXXXXXX//
    public void sendSuspensionEmailPg(PgVO pg) {
        try {
            // 使用 Thymeleaf 模板渲染郵件內容
            String templatePath = "back_end/admin/SuspensionEmailPg"; // 你的 Thymeleaf 模板路徑

            // 使用 Thymeleaf 模板渲染郵件內容
            Context context = new Context();
            context.setVariable("pgName", pg.getPgName());  // 將會員名稱傳入模板
            context.setVariable("pgEmail", pg.getPgEmail());  // 如果需要郵件可以傳遞

            String content = templateEngine.process(templatePath, context); // 使用模板渲染內容

            // 設置發送郵件的收件人、主題及內容
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            String recipientEmail = pg.getPgEmail();  // 確保從 MebVO 類中獲得正確的會員郵箱
            helper.setTo(recipientEmail);
            helper.setSubject("您的帳號已被停權");
            helper.setText(content, true);  // 使用渲染後的 HTML 內容

            // 發送郵件
            mailSender.send(message);

        } catch (MessagingException e) {
            e.printStackTrace();
            throw new RuntimeException("郵件發送失敗：" + e.getMessage(), e);
        }
    }
    //XXXXXXXXXXXXXXXXXXXXX< 保母停權通知 >XXXXXXXXXXXXXXXXXXXXX//
    public void sendSuspensionEmailPs(PsVO ps) {
        try {
            // 使用 Thymeleaf 模板渲染郵件內容
            String templatePath = "back_end/admin/SuspensionEmailPs"; // 你的 Thymeleaf 模板路徑

            // 使用 Thymeleaf 模板渲染郵件內容
            Context context = new Context();
            context.setVariable("psName", ps.getPsName());  // 將會員名稱傳入模板
            context.setVariable("psEmail", ps.getPsEmail());  // 如果需要郵件可以傳遞

            String content = templateEngine.process(templatePath, context); // 使用模板渲染內容

            // 設置發送郵件的收件人、主題及內容
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            String recipientEmail = ps.getPsEmail();  // 確保從 MebVO 類中獲得正確的會員郵箱
            helper.setTo(recipientEmail);
            helper.setSubject("您的帳號已被停權");
            helper.setText(content, true);  // 使用渲染後的 HTML 內容

            // 發送郵件
            mailSender.send(message);

        } catch (MessagingException e) {
            e.printStackTrace();
            throw new RuntimeException("郵件發送失敗：" + e.getMessage(), e);
        }
    }
}
