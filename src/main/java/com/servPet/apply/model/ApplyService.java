package com.servPet.apply.model;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.servPet.meb.model.MebVO;
import com.servPet.pg.model.PgRepository;
import com.servPet.pg.model.PgVO;
import com.servPet.ps.model.PsRepository;
import com.servPet.ps.model.PsVO;



@Service("applyService")
public class ApplyService {
	
	

    @Autowired
    private ApplyRepository applyRepository;

    @Autowired
    private PsRepository psRepository;

    @Autowired
    private PgRepository pgRepository;
    
    @Autowired
    private JavaMailSender mailSender;


    // 保存申請
    public ApplyVO saveApply(ApplyVO apply) {
        validateApply(apply);

        apply.setApplyStatus(ApplyStatus.PENDING.getCode());
        apply.setApplyTime(LocalDateTime.now());
        return applyRepository.save(apply);
    }
    
  
	
	public List<ApplyVO> getByMember(MebVO mebId) {
        return applyRepository.getByMember(mebId);
    }

    // 檢查申請的合法性
    private void validateApply(ApplyVO apply) {
        if (apply.getMebId() == null) {
            throw new IllegalArgumentException("請登入會員");
        }
        if (apply.getApplyCate() == null || apply.getApplyCate().trim().isEmpty()) {
            throw new IllegalArgumentException("申請類別不能為空");
        }
        if (apply.getApplyPic() == null || apply.getApplyPic().length == 0) {
            throw new IllegalArgumentException("必須上傳證件照");
        }
    }

    // 更新申請狀態
    @Transactional
    public ApplyResultDTO<String> updateApplyStatus(Integer applyId, String status, String rejectReason) {
    	System.out.println("查詢申請記錄，ID: " + applyId);
    	ApplyVO apply = applyRepository.findById(applyId)
    	        .orElseThrow(() -> new IllegalArgumentException("申請記錄不存在"));
    	System.out.println("找到的申請記錄: " + apply);

        apply.setApplyStatus(status);

        if (ApplyStatus.REJECTED.getCode().equals(status)) {
        	 System.out.println("駁回狀態代碼: " + status);

        	 if (rejectReason == null || rejectReason.trim().isEmpty()) {
        		    rejectReason = "請填寫駁回原因"; // 提供硬性預設值
        		}

            apply.setApplyRejectReason(rejectReason);
            apply.setApplyUpdateTime(LocalDateTime.now());
            System.out.println("更新申請狀態為: " + status);
            applyRepository.save(apply);
            System.out.println("申請記錄已保存");

            sendRejectionMail(apply.getMebId().getMebMail(), rejectReason);//這邊沒正常做  所以@Transactional 判定是有問題  roll back
            System.out.println("有正常送郵件");
            return ApplyResultDTO.success(null, "申請已駁回");
        } else if (ApplyStatus.APPROVED.getCode().equals(status)) {
        	System.out.println("通過狀態代碼: " + ApplyStatus.APPROVED.getCode());
            apply.setApplyRejectReason(null);
            apply.setApplyUpdateTime(LocalDateTime.now());
            System.out.println("更新申請狀態為: " + status);
            applyRepository.save(apply);
            System.out.println("申請記錄已保存");


            // 发送通过邮件
            try {
                sendApprovalMail(apply);
                System.out.println("開始準備1發送郵件...");
                return ApplyResultDTO.success(null, "申請已通過，郵件發送成功");
            } catch (Exception e) {
            	System.out.println("開始準備發2送郵件...");
                return ApplyResultDTO.failure("申請已通過，但郵件發送失敗：" + e.getMessage());
            }
        }

        return ApplyResultDTO.failure("未知的申請狀態");
    }
    
    


    // 發送申請通過郵件
    private void sendApprovalMail(ApplyVO apply) {
        System.out.println("開始發送批准郵件到: " + apply.getMebId().getMebMail());
        try {
            String subject = "申請已通過";
            String to = apply.getMebId().getMebMail();
            String messageContent;
            System.out.println("正在處理郵件發送邏輯...");

            if ("0".equals(apply.getApplyCate())) {
                PgVO pg = createPgFromApply(apply);
                PgVO savedPg = pgRepository.save(pg);

                messageContent = String.format(
                    """
                    <html>
                    <head>
                        <style>
				        body {
				            font-family: Arial, sans-serif;
				            /* background: linear-gradient(135deg, #fef2e4, #f9e4c9); */
				            margin: 0;
				            padding: 20px;
				        }
				        .email-container {
				            max-width: 700px;
				            margin: auto;
				            background-color:  #FFF8EE;
				            border: none;
				            border-radius: 15px;
				            box-shadow: 0 5px 15px rgba(0, 0, 0, 0.2);
				            overflow: hidden;
				        }
				        .header {
				            background-color: #F8EAD3;
				            color: #333333;
				            text-align: center;
				            padding: 30px;
				            font-size: 28px;
				            font-weight: bold;
				            letter-spacing: 1px;
				            position: relative;
				            gap: 10px; 
				        }
				        .header img {
				            width: 40px; /* 調整腳印圖案大小 */
				            height: 40px;
				        }
				        .content {
				          
				            color: #333333;
				            text-align: center;
				        }
				        .content h2 {
				            color: #E74C3C;
				            font-size: 26px;
				            margin: 20px 0;
				        }
				        .content p {
				            margin: 15px 0;
				            font-size: 18px;
				             text-align: center;
				        }
				        .reason-box {
				           background-color:  #FFF8EE;
				            padding: 15px;
				            text-align: left;
				        }
				        .button {
				            display: inline-block;
				            margin-top: 20px;
				            padding: 12px 25px;
				            color: #ffffff;
				            background-color: #3498DB;
				            text-decoration: none;
				            border-radius: 5px;
				            font-size: 18px;
				            font-weight: bold;
				        }
				        .button:hover {
				            background-color: #2980B9;
				        }
				         
				         .footer {
				            background-color: #F8EAD3;
				            padding: 15px;
				            font-size: 14px;
				            color: #666666;
				        }
				        .footer p {
				            margin: 5px 0;
				        }
				    </style>
                    </head>
                    <body>
                        <div class="email-container">
                            <div class="header">
                		          申請審核結果通知
                		           <img src="https://drive.google.com/uc?export=view&id=1JXqYg1NMsB6gqTcEiHbyK70ZVNsnOY_0" alt="Google 雲端硬碟圖片" />
                            </div>
                            <div class="content">
                                <h2 style="color:#ff4000;">申請已通過審核</h2>
                                <p>您的申請已成功通過審核，感謝您的耐心等待！以下是您的帳號資訊：</p>
                                <div class="reason-box">
	                                <ul>
	                                    <li><strong>美容師帳號 (pgId)：</strong>%s</li>
	                                    <li><strong>密碼 (pgPw)：</strong>%s</li>
	                                </ul>
                		        </div>
                		       <img src="https://drive.google.com/uc?export=view&id=1Lb3pV_0TaIrMHqlGI1MmZjPPXQDAbU2R" 
								     alt="Google 雲端硬碟圖片2" 
								     style="width: 350px; height: auto;" />
								<p>感謝您使用我們的服務！</p>
						        <!-- Footer -->
						        <div class="footer">
						            <p>&copy; 寵寵唯跡 ServPet Forever 2024</p>
					
					    </div>
					</body>
                    </html>
                    """,
                    savedPg.getPgId(), savedPg.getPgPw()
                );
            } else if ("1".equals(apply.getApplyCate())) {
                PsVO ps = createPsFromApply(apply);
                PsVO savedPs = psRepository.save(ps);

                messageContent = String.format(
                    """
                    <html>
                    <head>
                        <style>
				        body {
				            font-family: Arial, sans-serif;
				            /* background: linear-gradient(135deg, #fef2e4, #f9e4c9); */
				            margin: 0;
				            padding: 20px;
				        }
				        .email-container {
				            max-width: 700px;
				            margin: auto;
				            background-color:  #FFF8EE;
				            border: none;
				            border-radius: 15px;
				            box-shadow: 0 5px 15px rgba(0, 0, 0, 0.2);
				            overflow: hidden;
				        }
				        .header {
				            background-color: #F8EAD3;
				            color: #333333;
				            text-align: center;
				            padding: 30px;
				            font-size: 28px;
				            font-weight: bold;
				            letter-spacing: 1px;
				            position: relative;
				            gap: 10px; 
				        }
				        .header img {
				            width: 40px; /* 調整腳印圖案大小 */
				            height: 40px;
				        }
				        .content {
				          
				            color: #333333;
				            text-align: center;
				        }
				        .content h2 {
				            color: #E74C3C;
				            font-size: 26px;
				            margin: 20px 0;
				        }
				        .content p {
				            margin: 15px 0;
				            font-size: 18px;
				             text-align: center;
				        }
				        .reason-box {
				           background-color:  #FFF8EE;
				            padding: 15px;
				            text-align: left;
				        }
				        .button {
				            display: inline-block;
				            margin-top: 20px;
				            padding: 12px 25px;
				            color: #ffffff;
				            background-color: #3498DB;
				            text-decoration: none;
				            border-radius: 5px;
				            font-size: 18px;
				            font-weight: bold;
				        }
				        .button:hover {
				            background-color: #2980B9;
				        }
				         
				         .footer {
				            background-color: #F8EAD3;
				            padding: 15px;
				            font-size: 14px;
				            color: #666666;
				        }
				        .footer p {
				            margin: 5px 0;
				        }
				          </style>
                    </head>
                    <body>
				    <div class="email-container">
				        <div class="header">
				            申請審核結果通知
				             <img src="https://drive.google.com/uc?export=view&id=1JXqYg1NMsB6gqTcEiHbyK70ZVNsnOY_0" alt="Google 雲端硬碟圖片" style="width:40px; height: auto;" >
				        </div>
				        <div class="content">
				            <h2 style="color:#ff4000;;">申請已通過審核</h2>
				            <p>您的申請已成功通過審核，感謝您的耐心等待！以下是您的帳號資訊：</p>
				            <div class="reason-box">
				            <ul>
				                <li><strong>保母帳號 (psId)：</strong>%s</li>
				                <li><strong>密碼 (psPw)：</strong>%s</li>
				            </ul>
                		 </div>				    		      
				            <img src="https://drive.google.com/uc?export=view&id=1Lb3pV_0TaIrMHqlGI1MmZjPPXQDAbU2R" 
								     alt="Google 雲端硬碟圖片2" 
								     style="width: 350px; height: auto;" />
                				<p>感謝您使用我們的服務！</p> <!-- Footer -->
					        <div class="footer">
					            <p>&copy; 寵寵唯跡 ServPet Forever 2024</p>
				
				    </div>
				</body>
                </html>
                   
                    """,
                    savedPs.getPsId(), savedPs.getPsPw()
                );
            } else {
                throw new IllegalArgumentException("未知的申請類別");
            }

            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(messageContent, true); // 使用 HTML 格式
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            System.out.println("郵件發送失敗，原因：" + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("郵件發送失敗：" + e.getMessage());
        }
    }

   



    // 發送申請駁回郵件
    private void sendRejectionMail(String to, String reason) {
        try {
            // 優化的郵件內容，加入 header、footer 和背景顏色
            String messageContent = """
                      <!DOCTYPE html>
                <html>
                <head>
                  <meta charset="UTF-8">
                  <title>申請審核結果通知</title>
                  <style>
                   body {
				            font-family: Arial, sans-serif;
				            /* background: linear-gradient(135deg, #fef2e4, #f9e4c9); */
				            margin: 0;
				            padding: 20px;
				        }
				        .email-container {
				            max-width: 700px;
				            margin: auto;
				            background-color:  #FFF8EE;
				            border: none;
				            border-radius: 15px;
				            box-shadow: 0 5px 15px rgba(0, 0, 0, 0.2);
				            overflow: hidden;
				        }
				        .header {
				            background-color: #F8EAD3;
				            color: #333333;
				            text-align: center;
				            padding: 30px;
				            font-size: 28px;
				            font-weight: bold;
				            letter-spacing: 1px;
				            position: relative;
				            gap: 10px; 
				        }
				        .header img {
				            width: 40px; /* 調整腳印圖案大小 */
				            height: 40px;
				        }
				        .content {
				          
				            color: #333333;
				            text-align: center;
				        }
				        .content h2 {
				            color: #E74C3C;
				            font-size: 26px;
				            margin: 20px 0;
				        }
				        .content p {
				            margin: 15px 0;
				            font-size: 18px;
				             text-align: center;
				        }
				        .reason-box {
				           background-color:  #FFF8EE;
				            padding: 15px;
				            text-align: left;
				        }
				        .button {
				            display: inline-block;
				            margin-top: 20px;
				            padding: 12px 25px;
				            color: #ffffff;
				            background-color: #3498DB;
				            text-decoration: none;
				            border-radius: 5px;
				            font-size: 18px;
				            font-weight: bold;
				        }
				        .button:hover {
				            background-color: #2980B9;
				        }
				         
				         .footer {
				            background-color: #F8EAD3;
				            padding: 15px;
				            font-size: 14px;
				            color: #666666;
				        }
				        .footer p {
				            margin: 5px 0;
				        }
                  </style>
                </head>
                <body>
                    <div class="email-container">
                        <div class="header">
                            申請審核結果通知
                             <img src="https://drive.google.com/uc?export=view&id=1JXqYg1NMsB6gqTcEiHbyK70ZVNsnOY_0" alt="Google 雲端硬碟圖片">
                        </div>
                        <div class="content">
                            <h2>很遺憾，您的申請未通過</h2>
                            <p>感謝您的耐心等待，以下是未通過的原因：</p>
                            <div class="reason-box">
                                <p><strong>未通過原因：</strong> %s</p>
                            </div>
                            <img src="https://drive.google.com/uc?export=view&id=1Lb3pV_0TaIrMHqlGI1MmZjPPXQDAbU2R" 
								     alt="Google 雲端硬碟圖片2" 
								     style="width: 350px; height: auto;" >
                            <p>感謝您使用我們的服務！</p>
                        </div>
                        <div class="footer">
                            <p>&copy; 寵寵唯跡 ServPet Forever 2024</p>
                        </div>
                    </div>
                </body>
                </html>
                    """.formatted(reason);

            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setTo(to);
            helper.setSubject("申請被駁回");
            helper.setText(messageContent, true); // 使用 HTML 格式
            mailSender.send(mimeMessage);
            System.out.println("駁回郵件已成功發送至：" + to);
        } catch (MessagingException e) {
            System.err.println("郵件發送失敗，收件人：" + to + "，原因：" + e.getMessage());
            throw new RuntimeException("郵件發送失敗：" + e.getMessage(), e);
        }
    }




    




    // 根據類型新增保母或美容師
    private void addSitterOrGroomer(ApplyVO apply) {
        switch (apply.getApplyCate()) {
            case "0": // 美容師
                PgVO groomer = createPgFromApply(apply);
                pgRepository.save(groomer);
                break;
            case "1": // 保母
                PsVO sitter = createPsFromApply(apply);
                psRepository.save(sitter);
                break;
            default:
                throw new IllegalArgumentException("未知的申請類別");
        }
    }

    // 將申請轉換為美容師
    private PgVO createPgFromApply(ApplyVO applyVO) {
        PgVO pgVO = new PgVO();
        pgVO.setPgName(applyVO.getMebId().getMebName());
        pgVO.setPgLicenses(applyVO.getApplyPic());
        pgVO.setPgEmail(applyVO.getMebId().getMebMail());
        pgVO.setPgPw(new SecureRandom().ints(10, 0, 62)
        	    .mapToObj(i -> "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789".charAt(i))
        	    .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
        	    .toString());
        pgVO.setApprovalStatus(ApplyStatus.APPROVED.getCode());
        pgVO.setPgArea("0");
        pgVO.setSchDate("0000000");
        pgVO.setSchTime("000");
        return pgVO;
    }
    
    


    // 將申請轉換為保母
    private PsVO createPsFromApply(ApplyVO applyVO) {
        PsVO psVO = new PsVO();
        psVO.setPsName(applyVO.getMebId().getMebName());
        psVO.setPsLicenses(applyVO.getApplyPic());
        psVO.setPsEmail(applyVO.getMebId().getMebMail());
        psVO.setPsPw(new SecureRandom().ints(10, 0, 62)
        	    .mapToObj(i -> "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789".charAt(i))
        	    .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
        	    .toString());
        psVO.setApprovalStatus(ApplyStatus.APPROVED.getCode());
        psVO.setPsArea("0");
        psVO.setSchDate("0000000");
        psVO.setSchTime("000");
        return psVO;
    }
    
    

    @Transactional
    public void processApplication(ApplyVO applyVO) {
        if (ApplyStatus.APPROVED.getCode().equals(applyVO.getApplyStatus())) {
            if ("0".equals(applyVO.getApplyCate())) { // 美容师
                PgVO pg = createPgFromApply(applyVO);
                pgRepository.save(pg);
            } else if ("1".equals(applyVO.getApplyCate())) { // 保母
                PsVO ps = createPsFromApply(applyVO);
                psRepository.save(ps);
            } else {
                throw new IllegalArgumentException("未知的申請類別");
            }
        } else {
            throw new IllegalStateException("只有已批准的申請才能處理");
        }
    }




    // 查詢申請
    public ApplyVO getApplyById(Integer applyId) {
        return applyRepository.findById(applyId)
                .orElseThrow(() -> new RuntimeException("申請不存在"));
    }

    // 查詢所有申請按降序
    public List<ApplyVO> getAllApplicationsSortedByIdDesc() {
        return applyRepository.findAll(Sort.by(Sort.Direction.DESC, "applyId"));
    }

    // 分頁查詢申請
    public Page<ApplyVO> getApplicationsByStatuses(List<String> statuses, Pageable pageable) {
        if (statuses == null || statuses.isEmpty()) {
            throw new IllegalArgumentException("狀態列表不能為空");
        }
        return applyRepository.findByApplyStatusIn(statuses, pageable);
    }
    
    
    
}
