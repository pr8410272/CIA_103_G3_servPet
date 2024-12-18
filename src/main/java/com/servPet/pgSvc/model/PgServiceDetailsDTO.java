package com.servPet.pgSvc.model;

public class PgServiceDetailsDTO {
	private Integer pgSvcId;
	private Integer pgId;
	private Integer svcId;
	private String svcType;
	private Integer svcPrice;
	private String svcName;
	private String svcDescr;
	private String svcIsDeleted;

	public PgServiceDetailsDTO() {
	}
	

	
	public PgServiceDetailsDTO(Integer pgSvcId ,Integer svcId,String svcName, String svcType, Integer svcPrice) {
		this.pgSvcId = pgSvcId;
		this.svcId = svcId;
		this.svcName = svcName;
		this.svcType = svcType;
		this.svcPrice = svcPrice;
	
	}


	// 構造函數
	public PgServiceDetailsDTO(PgSvcVO pgSvc, String svcName, String svcDescr) {
		this.pgSvcId = pgSvc.getPgSvcId();
		this.pgId = pgSvc.getPgId();
		this.svcId = pgSvc.getSvcId();
		this.svcType = pgSvc.getSvcType();
		this.svcPrice = pgSvc.getSvcPrice();
		this.svcName = svcName;
		this.svcDescr = svcDescr;
	}

	public Integer getPgSvcId() {
		return pgSvcId;
	}

	public void setPgSvcId(Integer pgSvcId) {
		this.pgSvcId = pgSvcId;
	}

	public Integer getPgId() {
		return pgId;
	}

	public void setPgId(Integer pgId) {
		this.pgId = pgId;
	}

	public Integer getSvcId() {
		return svcId;
	}

	public void setSvcId(Integer svcId) {
		this.svcId = svcId;
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

	public String getSvcName() {
		return svcName;
	}

	public void setSvcName(String svcName) {
		this.svcName = svcName;
	}

	public String getSvcDescr() {
		return svcDescr;
	}

	public void setSvcDescr(String svcDescr) {
		this.svcDescr = svcDescr;
	}

	public String getSvcIsDeleted() {
		return svcIsDeleted;
	}

	public void setSvcIsDeleted(String svcIsDeleted) {
		this.svcIsDeleted = svcIsDeleted;
	}

	@Override
	public String toString() {
		return "PgServiceDetailsDTO [pgId=" + pgId + ", svcId=" + svcId + ", svcType=" + svcType + ", svcPrice="
				+ svcPrice + ", svcName=" + svcName + ", svcDescr=" + svcDescr + "]";
	}

}

//public PgServiceDetailsDTO(Integer pgSvcId, Integer pgId, Integer svcId, String svcType, Integer svcPrice,
//String svcName, String svcDescr, String svcIsDeleted) {
//super();
//this.pgSvcId = pgSvcId;
//this.pgId = pgId;
//this.svcId = svcId;
//this.svcType = svcType;
//this.svcPrice = svcPrice;
//this.svcName = svcName;
//this.svcDescr = svcDescr;
//this.svcIsDeleted = svcIsDeleted;
//}

