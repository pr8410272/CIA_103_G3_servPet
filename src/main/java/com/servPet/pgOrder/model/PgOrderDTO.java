package com.servPet.pgOrder.model;

import java.util.Arrays;

public class PgOrderDTO {

	private String mebName;
	private String petName;
	private String petType;
	private byte[] petImg;
	private String svcName;
	private Double bal;// 會員餘額
	private String svcType;// 適用體型
	private Integer svcPrice;// 項目金額
	private Integer pgSvcId;
	
	
	public PgOrderDTO() {
		// 無參數建構子
	}

	public PgOrderDTO(String mebName, String petName, String petType, byte[] petImg, String svcName, Double bal,
			String svcType, Integer svcPrice, Integer pgSvcId) {
		super();
		this.mebName = mebName;
		this.petName = petName;
		this.petType = petType;
		this.petImg = petImg;
		this.svcName = svcName;
		this.bal = bal;
		this.svcType = svcType;
		this.svcPrice = svcPrice;
		this.pgSvcId = pgSvcId;
	}

	public Double getBal() {
		return bal;
	}

	public void setBal(Double bal) {
		this.bal = bal;
	}

	public String getSvcType() {
		return svcType;
	}

	public void setSvcType(String svcType) {
		this.svcType = svcType;
	}

	public Integer getSvcPrice() {
		return svcPrice;
	}

	public void setSvcPrice(Integer svcPrice) {
		this.svcPrice = svcPrice;
	}

	public Integer getPgSvcId() {
		return pgSvcId;
	}

	public void setPgSvcId(Integer pgSvcId) {
		this.pgSvcId = pgSvcId;
	}

	// Getters and Setters
	public String getMebName() {
		return mebName;
	}

	public void setMebName(String mebName) {
		this.mebName = mebName;
	}

	public String getPetName() {
		return petName;
	}

	public void setPetName(String petName) {
		this.petName = petName;
	}

	public String getPetType() {
		return petType;
	}

	public void setPetType(String petType) {
		this.petType = petType;
	}

	public byte[] getPetImg() {
		return petImg;
	}

	public void setPetImg(byte[] petImg) {
		this.petImg = petImg;
	}

	public String getSvcName() {
		return svcName;
	}

	public void setSvcName(String svcName) {
		this.svcName = svcName;
	}

	@Override
	public String toString() {
		return "PgOrderDTO [mebName=" + mebName + ", petName=" + petName + ", petType=" + petType + ", petImg="
				+ Arrays.toString(petImg) + ", svcName=" + svcName + ", bal=" + bal + ", svcType=" + svcType
				+ ", svcPrice=" + svcPrice + ", pgSvcId=" + pgSvcId + "]";
	}

}
