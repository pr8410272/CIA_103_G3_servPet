package com.servPet.pgFav.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.servPet.meb.model.MebVO;
import com.servPet.pg.model.PgVO;

@Entity
@Table(name = "PET_GROOMER_FAVORITE")
public class PgFavVO implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "PG_FAV_ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer pgFavId;
	
	// 關聯到 MEMBER 表格內的 MEB_ID 欄位
	@ManyToOne
	@JoinColumn(name = "MEB_ID", nullable = false)
	private MebVO mebVO;
	
	// 關聯到 PET_GROOMER_FAVORITE 表格內的 PG_ID 欄位
	@ManyToOne
	@JoinColumn(name = "PG_ID", nullable = false)
	private PgVO pgVO;

	public PgFavVO() {
	}

	public Integer getPgFavId() {
		return pgFavId;
	}

	public void setPgFavId(Integer pgFavId) {
		this.pgFavId = pgFavId;
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
}
