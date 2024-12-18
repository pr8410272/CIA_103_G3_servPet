package com.servPet.pdFav.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.servPet.meb.model.MebVO;
import com.servPet.pdDetails.model.PdDetailsVO;

@Entity
@Table(name = "PRODUCT_FAVORITE")
public class PdFavVO implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "PD_FAV_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer pdFavId;

    // 關聯到 MEMBER 表格內的 MEB_ID 欄位
    @ManyToOne
    @JoinColumn(name = "MEB_ID")
    private MebVO mebVO;

    // 關聯到 Product_Details 表格內的 PD_ID 欄位
    @ManyToOne
    @JoinColumn(name = "PD_ID")
    private PdDetailsVO pdDetailsVO;

    public PdFavVO() {
    }

	public Integer getPdFavId() {
		return pdFavId;
	}

	public void setPdFavId(Integer pdFavId) {
		this.pdFavId = pdFavId;
	}

	public MebVO getMebVO() {
		return mebVO;
	}

	public void setMebVO(MebVO mebVO) {
		this.mebVO = mebVO;
	}

	public PdDetailsVO getPdDetailsVO() {
		return pdDetailsVO;
	}

	public void setPdDetailsVO(PdDetailsVO pdDetailsVO) {
		this.pdDetailsVO = pdDetailsVO;
	}
    
}
