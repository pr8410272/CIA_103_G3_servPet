package com.servPet.pdImg.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.servPet.pdDetails.model.PdDetailsVO;

@Entity
@Table(name = "PRODUCT_IMAGES")
public class PdImgVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "PD_IMG_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer pdImgId;

    @ManyToOne
    @JoinColumn(name = "PD_ID", nullable = false, referencedColumnName = "PD_ID")
    private PdDetailsVO pdDetailsVO;

    @Lob
    @Column(name = "PD_IMG", nullable = false)
    private byte[] pdImg; // 儲存圖片二進位數據

    @Column(name = "IMG_DESC", length = 255, nullable = false)
    @NotEmpty(message = "圖片描述 不能空白")
    private String imgDesc;

    @Column(name = "UPDATED_AT", nullable = false)
    @NotNull(message = "圖片上傳時間 不能為空")
    private Date updatedAt;

    // Default Constructor
    public PdImgVO() {}

    // Full Constructor
    public PdImgVO(Integer pdImgId, PdDetailsVO pdDetailsVO, byte[] pdImg, String imgDesc, Date updatedAt) {
        this.pdImgId = pdImgId;
        this.pdDetailsVO = pdDetailsVO;
        this.pdImg = pdImg;
        this.imgDesc = imgDesc;
        this.updatedAt = updatedAt;
    }

    // Getters and Setters
    public Integer getPdImgId() {
        return pdImgId;
    }

    public void setPdImgId(Integer pdImgId) {
        this.pdImgId = pdImgId;
    }

    public PdDetailsVO getPdDetailsVO() {
        return pdDetailsVO;
    }

    public void setPdDetailsVO(PdDetailsVO pdDetailsVO) {
        this.pdDetailsVO = pdDetailsVO;
    }

    public byte[] getPdImg() {
        return pdImg;
    }

    public void setPdImg(byte[] pdImg) {
        this.pdImg = pdImg;
    }

    public String getImgDesc() {
        return imgDesc;
    }

    public void setImgDesc(String imgDesc) {
        this.imgDesc = imgDesc;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    // toString Method
    @Override
    public String toString() {
        return "PdImgVO{" +
                "pdImgId=" + pdImgId +
                ", pdDetailsVO=" + (pdDetailsVO != null ? pdDetailsVO.getPdId() : null) +
                ", imgDesc='" + imgDesc + '\'' +
                ", updatedAt=" + updatedAt +
                '}';
    }

    // Equals and HashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PdImgVO pdImgVO = (PdImgVO) o;

        return pdImgId != null ? pdImgId.equals(pdImgVO.pdImgId) : pdImgVO.pdImgId == null;
    }

    @Override
    public int hashCode() {
        return pdImgId != null ? pdImgId.hashCode() : 0;
    }
}
