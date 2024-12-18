package com.servPet.meb.model;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//@Data // 自動生成 getter、setter、toString、equals 和 hashCode
//@NoArgsConstructor // 自動生成無參構造函數

@Entity
@Table(name = "MEMBER")
public class MebVO implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MEB_ID")
    private Integer mebId;

    @NotBlank(message = "名字不可為空")
    @Size(min = 2, max = 20, message = "名字長度應在 2 到 20 之間")
    @Column(name = "MEB_NAME", nullable = false, length = 20)
    private String mebName;

    @Email(message = "請輸入有效的電子郵件")
    @NotBlank(message = "電子郵件不可為空")
    @Column(name = "MEB_MAIL", nullable = false, unique = true)
    private String mebMail;

    @NotBlank(message = "密碼不可為空")
//    @Pattern(
//            regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d!@#$%^&*()_+]{6,16}$",
//            message = "密碼必須為長度6~16位，至少包含英文字母和數字，可包含特殊符號"
//        )
    @Column(name = "MEB_PWD", nullable = false)
    private String mebPwd;

    @Size(max = 5, message = "狀態長度不能超過 5 字")
    @Column(name = "MEB_STATUS", length = 5)
    private String mebStatus ="未認證";

    @Size(max = 100, message = "地址長度不能超過 100 字")
    @Column(name = "MEB_ADDRESS", length = 100)
    private String mebAddress;

    @Pattern(regexp = "^\\d{10,15}$", message = "電話號碼必須為 10-15 位數字")
    @Column(name = "MEB_PHONE", length = 15)
    private String mebPhone;

    @Size(max = 5, message = "性別長度不能超過 5 字")
    @Column(name = "MEB_SEX", length = 5)
    private String mebSex;

    @Column(name = "BAL", nullable = false)
    private Double bal = 0.0;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "MEB_IMG")
    private byte[] mebImg;

    

    public MebVO() {
        super();
        this.mebStatus = "未認證";
        this.bal = 0.0;
    }
	public Integer getMebId() {
		return mebId;
	}

	public void setMebId(Integer mebId) {
		this.mebId = mebId;
	}

	public String getMebName() {
		return mebName;
	}

	public void setMebName(String mebName) {
		this.mebName = mebName;
	}

	public String getMebMail() {
		return mebMail;
	}

	public void setMebMail(String mebMail) {
		this.mebMail = mebMail;
	}

	public String getMebPwd() {
		return mebPwd;
	}

	public void setMebPwd(String mebPwd) {
		this.mebPwd = mebPwd;
	}

	public String getMebStatus() {
		return mebStatus;
	}

	public void setMebStatus(String mebStatus) {
		this.mebStatus = mebStatus;
	}

	public String getMebAddress() {
		return mebAddress;
	}

	public void setMebAddress(String mebAddress) {
		this.mebAddress = mebAddress;
	}

	public String getMebPhone() {
		return mebPhone;
	}

	public void setMebPhone(String mebPhone) {
		this.mebPhone = mebPhone;
	}

	public String getMebSex() {
		return mebSex;
	}

	public void setMebSex(String mebSex) {
		this.mebSex = mebSex;
	}

	public Double getBal() {
		return bal;
	}

	public void setBal(Double bal) {
		this.bal = bal;
	}

	public byte[] getMebImg() {
		return mebImg;
	}

	public void setMebImg(byte[] mebImg) {
		this.mebImg = mebImg;
	}

	@Override
	public String toString() {
	    return "MebVO [mebId=" + mebId + ", mebName=" + mebName + ", mebMail=" + mebMail 
	            + ", mebStatus=" + mebStatus + ", mebAddress=" + mebAddress + ", mebPhone=" + mebPhone + ", mebSex="
	            + mebSex + ", bal=" + bal + "]";
	}

}
