package com.servPet.pgOrder.model;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "PET_GROOMER_ORDER")
public class PgOrderVO implements java.io.Serializable {
	private static final long serialVersionUID = 1L;

	public PgOrderVO() {

	}

	@Id // 主鍵
	@GeneratedValue(strategy = GenerationType.IDENTITY) // 使用自增的方式來生成主鍵
	@Column(name = "PGO_ID")
	private Integer pgoId;

	@Column(name = "MEB_ID", nullable = false)
	private Integer mebId;

	@Column(name = "PG_ID", nullable = false)
	private Integer pgId;

	@Column(name = "PET_ID", nullable = false)
	private Integer petId;

	@Column(name = "PGSVC_ID", nullable = false)
	private Integer pgSvcId;

	@Column(name = "BOOKING_DATE", nullable = false)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@NotNull(message = "請選擇要預約的日期")
	private LocalDate bookingDate;

	@Column(name = "BOOKING_TIME", nullable = false)
	private String bookingTime;

	@Column(name = "BOOKING_STATUS")
	private String bookingStatus;

	@Column(name = "APPR_STATUS")
	private String apprStatus;

	@Column(name = "SVC_PRICE", nullable = false)
	private Integer svcPrice;

	@Column(name = "STAR")
	private Integer star;

	@Column(name = "RATING_COMMENT")
	private String ratingComment;

	@Column(name = "SUS_POINT")
	private Integer susPoint;

	public Integer getPgoId() {
		return pgoId;
	}

	public void setPgoId(Integer pgoId) {
		this.pgoId = pgoId;
	}

	public Integer getMebId() {
		return mebId;
	}

	public void setMebId(Integer mebId) {
		this.mebId = mebId;
	}

	public Integer getPgId() {
		return pgId;
	}

	public void setPgId(Integer pgId) {
		this.pgId = pgId;
	}

	public Integer getPetId() {
		return petId;
	}

	public void setPetId(Integer petId) {
		this.petId = petId;
	}

	public Integer getPgSvcId() {
		return pgSvcId;
	}

	public void setPgSvcId(Integer pgSvcId) {
		this.pgSvcId = pgSvcId;
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

	public String getBookingStatus() {
		return bookingStatus;
	}

	public void setBookingStatus(String bookingStatus) {
		this.bookingStatus = bookingStatus;
	}

	public String getApprStatus() {
		return apprStatus;
	}

	public void setApprStatus(String apprStatus) {
		this.apprStatus = apprStatus;
	}

	public Integer getSvcPrice() {
		return svcPrice;
	}

	public void setSvcPrice(Integer svcPrice) {
		this.svcPrice = svcPrice;
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

	public Integer getSusPoint() {
		return susPoint;
	}

	public void setSusPoint(Integer susPoint) {
		this.susPoint = susPoint;
	}

	public PgOrderVO(Integer mebId, Integer pgId, Integer petId, Integer pgSvcId, LocalDate bookingDate,
			String bookingTime, Integer svcPrice) {
		super();
		this.mebId = mebId;
		this.pgId = pgId;
		this.petId = petId;
		this.pgSvcId = pgSvcId;
		this.bookingDate = bookingDate;
		this.bookingTime = bookingTime;
		this.svcPrice = svcPrice;
		this.bookingStatus = "0"; //預設未完成
		this.apprStatus = "0"; //預設處理中

	}

}
