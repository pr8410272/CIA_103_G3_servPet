package com.servPet.pgBooking.model;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

import com.servPet.meb.model.MebVO;
import com.servPet.pet.model.PetVO;
import com.servPet.pg.model.PgVO;

@Entity
@Table(name = "PET_GROOMER_ORDER")
public class PgBookingVO implements java.io.Serializable {
	private static final long serialVersionUID = 1L;

	public PgBookingVO() {

	}

	@Id // 主鍵
	@GeneratedValue(strategy = GenerationType.IDENTITY) // 使用自增的方式來生成主鍵
	@Column(name = "PGO_ID")
	private Integer pgoId;

	@ManyToOne
	@JoinColumn(name = "MEB_ID", nullable = false)
	private MebVO mebVO;

	@ManyToOne
	@JoinColumn(name = "PG_ID", nullable = false)
	private PgVO pgVO;

	@ManyToOne
	@JoinColumn(name = "PET_ID", nullable = false)
	private PetVO petVO;

	@Column(name = "PGSVC_ID", nullable = false)
	private Integer	pgSvcId;

	@Column(name = "BOOKING_DATE", nullable = false)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
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

	
	public MebVO getMebVO() {
		return mebVO;
	}

	public void setMebVO(MebVO mebVO) {
		this.mebVO = mebVO;
	}

	public PgVO getPgVO() {
		return pgVO;
	}

	public void setPgVO(PgVO pgVO) {
		this.pgVO = pgVO;
	}

	public PetVO getPetVO() {
		return petVO;
	}

	public void setPetVO(PetVO petVO) {
		this.petVO = petVO;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
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

}
