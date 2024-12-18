package com.servPet.pdoItem.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.servPet.pdDetails.model.PdDetailsVO;
import com.servPet.pdo.model.PdoVO;

@Entity
@Table(name = "ORDER_ITEM_DETAILS")
public class PdoItemVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "PDO_ITEM_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer pdoItemId;

    @ManyToOne
    @JoinColumn(name = "PDO_ID", referencedColumnName = "PDO_ID")
    private PdoVO pdoVO;

    @ManyToOne
    @JoinColumn(name = "PD_ID", referencedColumnName = "PD_ID")
    private PdDetailsVO pdDetailsVO;

    @Column(name = "PD_QTY", nullable = false)
    private Integer pdQty;

    @Column(name = "PD_PRICE", nullable = false)
    private Integer pdPrice;

    @Column(name = "PD_TOTAL_PRICE", nullable = false)
    private Integer pdTotalPrice;

    // Default Constructor
    public PdoItemVO() {}

    // Full Constructor
    public PdoItemVO(Integer pdoItemId, PdoVO pdoVO, PdDetailsVO pdDetailsVO, Integer pdQty, Integer pdPrice, Integer pdTotalPrice) {
        this.pdoItemId = pdoItemId;
        this.pdoVO = pdoVO;
        this.pdDetailsVO = pdDetailsVO;
        this.pdQty = pdQty;
        this.pdPrice = pdPrice;
        this.pdTotalPrice = pdTotalPrice;
    }

    // Getters and Setters
    public Integer getPdoItemId() {
        return pdoItemId;
    }

    public void setPdoItemId(Integer pdoItemId) {
        this.pdoItemId = pdoItemId;
    }

    public PdoVO getPdoVO() {
        return pdoVO;
    }

    public void setPdoVO(PdoVO pdoVO) {
        this.pdoVO = pdoVO;
    }

    public PdDetailsVO getPdDetailsVO() {
        return pdDetailsVO;
    }

    public void setPdDetailsVO(PdDetailsVO pdDetailsVO) {
        this.pdDetailsVO = pdDetailsVO;
    }

    public Integer getPdQty() {
        return pdQty;
    }

    public void setPdQty(Integer pdQty) {
        this.pdQty = pdQty;
    }

    public Integer getPdPrice() {
        return pdPrice;
    }

    public void setPdPrice(Integer pdPrice) {
        this.pdPrice = pdPrice;
    }

    public Integer getPdTotalPrice() {
        return pdTotalPrice;
    }

    public void setPdTotalPrice(Integer pdTotalPrice) {
        this.pdTotalPrice = pdTotalPrice;
    }

    // toString Method
    @Override
    public String toString() {
        return "PdoItemVO{" +
                "pdoItemId=" + pdoItemId +
                ", pdoVO=" + (pdoVO != null ? pdoVO.getPdoId() : null) +
                ", pdDetailsVO=" + (pdDetailsVO != null ? pdDetailsVO.getPdId() : null) +
                ", pdQty=" + pdQty +
                ", pdPrice=" + pdPrice +
                ", pdTotalPrice=" + pdTotalPrice +
                '}';
    }

    // equals and hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PdoItemVO pdoItemVO = (PdoItemVO) o;

        return pdoItemId != null ? pdoItemId.equals(pdoItemVO.pdoItemId) : pdoItemVO.pdoItemId == null;
    }

    @Override
    public int hashCode() {
        return pdoItemId != null ? pdoItemId.hashCode() : 0;
    }
}
