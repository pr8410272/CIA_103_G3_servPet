package com.servPet.meb.model;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
//EmailService.java
@Service
public class EmailService {

 @Autowired
 private JavaMailSender mailSender;

 public void sendPasswordResetEmail(String to, String subject, String content) {
	    try {
	        MimeMessage message = mailSender.createMimeMessage();
	        MimeMessageHelper helper = new MimeMessageHelper(message, true);
	        helper.setTo(to);
	        helper.setSubject(subject);
	        helper.setText(content, true); // 支援 HTML
	        mailSender.send(message);
	    } catch (MessagingException e) {
	        System.err.println("郵件發送失敗：" + e.getMessage());
	        throw new RuntimeException("郵件發送失敗: " + e.getMessage());
	    }
	}

}
