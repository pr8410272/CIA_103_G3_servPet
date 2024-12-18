package com.servPet.meb.model;

import org.springframework.web.multipart.MultipartFile;

public class MebDTO {
    private String mebName;
    private String mebPwd;
    private String mebMail;
    private String mebPhone;
    private String mebAddress;
    private String mebSex;
    private MultipartFile mebImg; // 使用 MultipartFile 接收圖片
	public String getMebName() {
		return mebName;
	}
	public void setMebName(String mebName) {
		this.mebName = mebName;
	}
	public String getMebPwd() {
		return mebPwd;
	}
	public void setMebPwd(String mebPwd) {
		this.mebPwd = mebPwd;
	}
	public String getMebMail() {
		return mebMail;
	}
	public void setMebMail(String mebMail) {
		this.mebMail = mebMail;
	}
	public String getMebPhone() {
		return mebPhone;
	}
	public void setMebPhone(String mebPhone) {
		this.mebPhone = mebPhone;
	}
	public String getMebAddress() {
		return mebAddress;
	}
	public void setMebAddress(String mebAddress) {
		this.mebAddress = mebAddress;
	}
	public String getMebSex() {
		return mebSex;
	}
	public void setMebSex(String mebSex) {
		this.mebSex = mebSex;
	}
	public MultipartFile getMebImg() {
		return mebImg;
	}
	public void setMebImg(MultipartFile mebImg) {
		this.mebImg = mebImg;
	}

    // Getters 和 Setters
}
