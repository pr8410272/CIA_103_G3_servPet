package com.servPet.psOrder.model;

import javax.validation.constraints.NotEmpty;

import com.servPet.meb.model.MebVO;
import com.servPet.pet.model.PetVO;
import com.servPet.ps.model.PsVO;
import com.servPet.psSvcItem.model.PsSvcItemVO;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serial;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "PET_SITTER_ORDER")
public class PsOrderVO implements java.io.Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id // 主鍵
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 使用自增的方式來生成主鍵
    @Column(name = "PSO_ID")
    private Integer psoId;
    //一個會員會有多筆訂單
    @ManyToOne
    @JoinColumn(name = "MEB_ID") // 外來鍵
    private MebVO mebVO;
//    @Column(name = "MEB_ID")
//    private  Integer mebId;

    //一個保母會有多筆訂單
    @ManyToOne
    @JoinColumn(name = "PS_ID") // 外來鍵
    private PsVO psVO;
//    @Column(name = "PS_ID")
//    private Integer psId;

    //一筆訂單只有一隻寵物
    @ManyToOne
    @JoinColumn(name = "PET_ID") // 外來鍵，連接到 PetVO
    private PetVO petVO;
//    @Column(name = "PET_ID")
//    private Integer petId;

    //一筆訂單會有多個服務項目
//    @OneToMany(mappedBy = "psOrderVO", cascade = CascadeType.ALL)
//    private Set<PsSvcItemVO> serviceItems = new HashSet<>();


    @Column(name = "BOOKING_DATE", nullable = false)
// @Future(message="請選擇今日之後的日期")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "請選擇要預約的日期")
    private LocalDate bookingDate;

    @Column(name = "BOOKING_TIME", nullable = false)
    @NotEmpty(message = "請選擇要預約的時段")
    private String bookingTime;

    @Column(name = "BOOKING_STATUS")
    private String bookingStatus;



    @Column(name = "SVC_ID", nullable = false)
    @NotNull(message = "請選擇要預約的服務項目")
    private Integer svcId;


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

    public Integer getPsoId() {
        return psoId;
    }

    public void setPsoId(Integer psoId) {
        this.psoId = psoId;
    }


    public MebVO getMebVO() {
        return mebVO;
    }

    public void setMebVO(MebVO mebVO) {
        this.mebVO = mebVO;
    }

    public PsVO getPsVO() {
        return psVO;
    }

    public void setPsVO(PsVO psVO) {
        this.psVO = psVO;
    }

    public PetVO getPetVO() {
        return petVO;
    }

    public void setPetVO(PetVO petVO) {
        this.petVO = petVO;
    }

    public Integer getSvcId() {
        return svcId;
    }

    public void setSvcId(Integer svcId) {
        this.svcId = svcId;
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