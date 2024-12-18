package com.servPet.pgBooking.model;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

public class PgBookingDTO {
	
	    private Integer pgId;          // 美容師 ID
	    private String pgName;         // 美容師姓名
	    private String petName;        // 寵物名字
	    private Integer svcId;         // 服務 ID
	    private String svcName;        // 服務名稱 - 新增
	    
	    @DateTimeFormat(pattern = "yyyy-MM-dd") 
	    private LocalDate bookingDate;      // 預約日期
	    private String bookingTime;    // 預約時間
	    private Integer pgoId;         // 預約 ID
	    private String bookingStatus;  // 預約狀態
	    private Integer star;          // 評價星數
	    private String ratingComment;  // 評價內容
	    
	    private String schDate;        // 美容師的工作日期安排
	    private String schTime;		  // 美容師的工作時間安排
	    
	    
	public Integer getPgId() {
		return pgId;
	}
	public void setPgId(Integer pgId) {
		this.pgId = pgId;
	}
	public String getPgName() {
		return pgName;
	}
	public void setPgName(String pgName) {
		this.pgName = pgName;
	}
	public String getPetName() {
		return petName;
	}
	public void setPetName(String petName) {
		this.petName = petName;
	}
	
	public LocalDate getBookingDate() {
		return bookingDate;
	}
	public void setBookingDate(LocalDate bookingDate) {
		this.bookingDate = bookingDate;
	}
	public String getBookingTime() {
		return bookingTime;
	}
	public void setBookingTime(String bookingTime) {
		this.bookingTime = bookingTime;
	}
	public Integer getPgoId() {
		return pgoId;
	}
	public void setPgoId(Integer pgoId) {
		this.pgoId = pgoId;
	}
	public String getBookingStatus() {
		return bookingStatus;
	}
	public void setBookingStatus(String bookingStatus) {
		this.bookingStatus = bookingStatus;
	}
	public Integer getStar() {
		return star;
	}
	public void setStar(Integer star) {
		this.star = star;
	}
	public String getRatingComment() {
		return ratingComment;
	}
	public void setRatingComment(String ratingComment) {
		this.ratingComment = ratingComment;
	}
	public Integer getSvcId() {
		return svcId;
	}
	public void setSvcId(Integer svcId) {
		this.svcId = svcId;
	}
	public String getSvcName() {
		return svcName;
	}
	public void setSvcName(String svcName) {
		this.svcName = svcName;
	}
	public String getSchDate() {
		return schDate;
	}
	public void setSchDate(String schDate) {
		this.schDate = schDate;
	}
	public String getSchTime() {
		return schTime;
	}
	public void setSchTime(String schTime) {
		this.schTime = schTime;
	}
	public PgBookingDTO() {
		super();
	}
	
	public PgBookingDTO(Integer pgId, String pgName, String schDate, String schTime) {
	    this.pgId = pgId;
	    this.pgName = pgName;
	    this.schDate = schDate;
	    this.schTime = schTime;
	}

	
}