package com.servPet.pdDetails.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
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
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Pattern;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.Transient;
import org.springframework.format.annotation.DateTimeFormat;

import com.servPet.cartDetails.model.CartDetailsVO;
import com.servPet.pdCateg.model.PdCategVO;
import com.servPet.pdImg.model.PdImgVO;
import com.servPet.pdoItem.model.PdoItemVO;

@Entity
@Table(name = "PRODUCT_DETAILS")
public class PdDetailsVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "PD_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer pdId;

    @Column(name = "PD_NAME")
    @NotEmpty(message = "產品名稱 請勿空白")
    private String pdName;

    @Column(name = "PD_DESCR")
    @NotEmpty(message = "產品描述 請勿空白")
    private String pdDescr;

    @Column(name = "PD_PRICE")
    @NotNull(message = "商品價格 請勿空白")
    @Min(value = 0L, message = "商品價格 不能小於{value}")
    @Max(value = 99999L, message = "商品價格 不能超過{value}")
    private Integer pdPrice;

    @Column(name = "PD_QTY")
    @NotNull(message = "商品數量 請勿空白")
    @Min(value = 0L, message = "商品數量 不能小於{value}")
    @Max(value = 99999L, message = "商品數量 不能超過{value}")
    private Integer pdQty;

    @Column(name = "PD_WEIGHT")
    private Double pdWeight;

    @Column(name = "PD_SIZE")
    private String pdSize;

    @Column(name = "PD_COLOR")
    private String pdColor;

    @Column(name = "CREATED_AT", updatable = false, nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @PastOrPresent(message = "創建日期 必須是今天或之前的日期")
    @CreationTimestamp
    private java.time.LocalDateTime createdAt;

    @Column(name = "UPDATED_AT", nullable = false, updatable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @PastOrPresent(message = "更新日期 必須是今天或之前的日期")
    private java.time.LocalDateTime updatedAt;

    @Column(name = "PD_STATUS", length = 1, nullable = false)
    @Pattern(regexp = "[0-3]", message = "商品狀態應為 0 (下架), 1 (上架中), 2 (停售), 3 (測試)")
    private String pdStatus;

    @ManyToOne
    @JoinColumn(name = "PD_CATEGORY", referencedColumnName = "PD_CATEGORY")
    @NotNull(message = "商品分類不能為空")
    private PdCategVO pdCategVO;

    @OneToMany(mappedBy = "pdDetailsVO", cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
    private Set<CartDetailsVO> cartDetailsVO = new HashSet<>();

    @OneToMany(mappedBy = "pdDetailsVO", cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
    private Set<PdImgVO> pdImgVO = new HashSet<>();

    @OneToMany(mappedBy = "pdDetailsVO", cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
    private Set<PdoItemVO> pdoItemVO = new HashSet<>();

    // Default constructor
    public PdDetailsVO() {}

    // Getters and Setters
    public Integer getPdId() {
        return pdId;
    }

    public void setPdId(Integer pdId) {
        this.pdId = pdId;
    }

    public String getPdName() {
        return pdName;
    }

    public void setPdName(String pdName) {
        this.pdName = pdName;
    }

    public String getPdDescr() {
        return pdDescr;
    }

    public void setPdDescr(String pdDescr) {
        this.pdDescr = pdDescr;
    }

    public Integer getPdPrice() {
        return pdPrice;
    }

    public void setPdPrice(Integer pdPrice) {
        this.pdPrice = pdPrice;
    }

    public Integer getPdQty() {
        return pdQty;
    }

    public void setPdQty(Integer pdQty) {
        this.pdQty = pdQty;
    }

    public Double getPdWeight() {
        return pdWeight;
    }

    public void setPdWeight(Double pdWeight) {
        this.pdWeight = pdWeight;
    }

    public String getPdSize() {
        return pdSize;
    }

    public void setPdSize(String pdSize) {
        this.pdSize = pdSize;
    }

    public String getPdColor() {
        return pdColor;
    }

    public void setPdColor(String pdColor) {
        this.pdColor = pdColor;
    }

    public java.time.LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(java.time.LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public java.time.LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(java.time.LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}

	public String getPdStatus() {
        return pdStatus;
    }

    public void setPdStatus(String pdStatus) {
        this.pdStatus = pdStatus;
    }

    public PdCategVO getPdCategVO() {
        return pdCategVO;
    }

    public void setPdCategVO(PdCategVO pdCategVO) {
        this.pdCategVO = pdCategVO;
    }

    public Set<CartDetailsVO> getCartDetailsVO() {
        return cartDetailsVO;
    }

    public void setCartDetailsVO(Set<CartDetailsVO> cartDetailsVO) {
        this.cartDetailsVO = cartDetailsVO;
    }

    public Set<PdImgVO> getPdImgVO() {
        return pdImgVO;
    }

    public void setPdImgVO(Set<PdImgVO> pdImgVO) {
        this.pdImgVO = pdImgVO;
    }

    public Set<PdoItemVO> getPdoItemVO() {
        return pdoItemVO;
    }

    public void setPdoItemVO(Set<PdoItemVO> pdoItemVO) {
        this.pdoItemVO = pdoItemVO;
    }

    // toString
    @Override
    public String toString() {
        return "PdDetailsVO{" +
                "pdId=" + pdId +
                ", pdName='" + pdName + '\'' +
                ", pdDescr='" + pdDescr + '\'' +
                ", pdPrice=" + pdPrice +
                ", pdQty=" + pdQty +
                ", pdWeight=" + pdWeight +
                ", pdSize='" + pdSize + '\'' +
                ", pdColor='" + pdColor + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", pdStatus='" + pdStatus + '\'' +
                '}';
    }

    // equals and hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PdDetailsVO that = (PdDetailsVO) o;
        return Objects.equals(pdId, that.pdId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pdId);
    }
    
}
    
