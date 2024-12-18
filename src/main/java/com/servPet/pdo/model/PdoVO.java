package com.servPet.pdo.model;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.servPet.meb.model.MebVO;
import com.servPet.pdoItem.model.PdoItemVO;

@Entity
@Table(name = "PRODUCT_ORDER")
public class PdoVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "PDO_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer pdoId;

    @ManyToOne
    @JoinColumn(name = "MEB_ID", nullable = false)
    private MebVO mebVO;

    @Column(name = "PDO_DATE", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date pdoDate;

    @Column(name = "PD_TOTAL_PRICE", nullable = false)
    private Integer pdTotalPrice;

    @Column(name = "PDO_UPDATE_TIME", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date pdoUpdateTime;

    @Column(name = "PDO_STATUS", nullable = false, length = 1)
    private String pdoStatus;

    @Column(name = "PAYMENT_STATUS", nullable = false, length = 1)
    private String paymentStatus;

    @Column(name = "SHIPPING_ADDR", nullable = false, length = 255)
    private String shippingAddr;

    @Column(name = "SHIPPING_METHOD", length = 1)
    private String shippingMethod;

    @Column(name = "PDO_REVIEW_RATE")
    private Integer pdoReviewRate;

    @Column(name = "PDO_REVIEW_COMM", length = 255)
    private String pdoReviewComm;

    @Column(name = "CREATED_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdTime;

    @Column(name = "SHIPPING_STATUS", length = 1)
    private String shippingStatus;

    @OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.EAGER, mappedBy = "pdoVO")
    private Set<PdoItemVO> pdoItemVO = new HashSet<>();

    // Default Constructor
    public PdoVO() {}

    // Full Constructor
    public PdoVO(Integer pdoId, MebVO mebVO, Date pdoDate, Integer pdTotalPrice, Date pdoUpdateTime, String pdoStatus,
                 String paymentStatus, String shippingAddr, String shippingMethod, Integer pdoReviewRate, String pdoReviewComm,
                 Date createdTime, String shippingStatus, Set<PdoItemVO> pdoItemVO) {
        this.pdoId = pdoId;
        this.mebVO = mebVO;
        this.pdoDate = pdoDate;
        this.pdTotalPrice = pdTotalPrice;
        this.pdoUpdateTime = pdoUpdateTime;
        this.pdoStatus = pdoStatus;
        this.paymentStatus = paymentStatus;
        this.shippingAddr = shippingAddr;
        this.shippingMethod = shippingMethod;
        this.pdoReviewRate = pdoReviewRate;
        this.pdoReviewComm = pdoReviewComm;
        this.createdTime = createdTime;
        this.shippingStatus = shippingStatus;
        this.pdoItemVO = pdoItemVO;
    }

    // Getters and Setters
    public Integer getPdoId() {
        return pdoId;
    }

    public void setPdoId(Integer pdoId) {
        this.pdoId = pdoId;
    }

    public MebVO getMebVO() {
        return mebVO;
    }

    public void setMebVO(MebVO mebVO) {
        this.mebVO = mebVO;
    }

    public Date getPdoDate() {
        return pdoDate;
    }

    public void setPdoDate(Date pdoDate) {
        this.pdoDate = pdoDate;
    }

    public Integer getPdTotalPrice() {
        return pdTotalPrice;
    }

    public void setPdTotalPrice(Integer pdTotalPrice) {
        this.pdTotalPrice = pdTotalPrice;
    }

    public Date getPdoUpdateTime() {
        return pdoUpdateTime;
    }

    public void setPdoUpdateTime(Date pdoUpdateTime) {
        this.pdoUpdateTime = pdoUpdateTime;
    }

    public String getPdoStatus() {
        return pdoStatus;
    }

    public void setPdoStatus(String pdoStatus) {
        this.pdoStatus = pdoStatus;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getShippingAddr() {
        return shippingAddr;
    }

    public void setShippingAddr(String shippingAddr) {
        this.shippingAddr = shippingAddr;
    }

    public String getShippingMethod() {
        return shippingMethod;
    }

    public void setShippingMethod(String shippingMethod) {
        this.shippingMethod = shippingMethod;
    }

    public Integer getPdoReviewRate() {
        return pdoReviewRate;
    }

    public void setPdoReviewRate(Integer pdoReviewRate) {
        this.pdoReviewRate = pdoReviewRate;
    }

    public String getPdoReviewComm() {
        return pdoReviewComm;
    }

    public void setPdoReviewComm(String pdoReviewComm) {
        this.pdoReviewComm = pdoReviewComm;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public String getShippingStatus() {
        return shippingStatus;
    }

    public void setShippingStatus(String shippingStatus) {
        this.shippingStatus = shippingStatus;
    }

    public Set<PdoItemVO> getPdoItemVO() {
        return pdoItemVO;
    }

    public void setPdoItemVO(Set<PdoItemVO> pdoItemVO) {
        this.pdoItemVO = pdoItemVO;
    }

    // toString Method
    @Override
    public String toString() {
        return "PdoVO{" +
                "pdoId=" + pdoId +
                ", mebVO=" + (mebVO != null ? mebVO.getMebId() : null) +
                ", pdoDate=" + pdoDate +
                ", pdTotalPrice=" + pdTotalPrice +
                ", pdoUpdateTime=" + pdoUpdateTime +
                ", pdoStatus='" + pdoStatus + '\'' +
                ", paymentStatus='" + paymentStatus + '\'' +
                ", shippingAddr='" + shippingAddr + '\'' +
                ", shippingMethod='" + shippingMethod + '\'' +
                ", pdoReviewRate=" + pdoReviewRate +
                ", pdoReviewComm='" + pdoReviewComm + '\'' +
                ", createdTime=" + createdTime +
                ", shippingStatus='" + shippingStatus + '\'' +
                '}';
    }

    // equals and hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PdoVO pdoVO = (PdoVO) o;

        return pdoId != null ? pdoId.equals(pdoVO.pdoId) : pdoVO.pdoId == null;
    }

    @Override
    public int hashCode() {
        return pdoId != null ? pdoId.hashCode() : 0;
    }
}
