package com.servPet.pgSvc.model;
// 美容師服務清單

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "PET_GROOMER_SERVICE")
public class PgSvcVO implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	public PgSvcVO() {
	}

	@Id // 主鍵
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "PGSVC_ID")
	private Integer pgSvcId;

	@Column(name = "PG_ID")
	private Integer pgId;

	@Column(name = "SVC_ID")
	private Integer svcId;
	
	@Column(name = "SVC_TYPE", nullable = false)
	@NotEmpty(message = "請選擇服務項目體型")
	private String svcType ;

	@Column(name = "SVC_PRICE", nullable = false)
	@NotNull(message = "價格請勿空白")
	private Integer svcPrice;
	

	public Integer getPgSvcId() {
		return pgSvcId;
	}

	public void setPgSvcId(Integer pgSvcId) {
		this.pgSvcId = pgSvcId;
	}

	public Integer getPgId() {
		return pgId;
	}

	public void setPgId(Integer pgId) {
		this.pgId = pgId;
	}

	public Integer getSvcId() {
		return svcId;
	}

	public void setSvcId(Integer svcId) {
		this.svcId = svcId;
	}
	
	public String getSvcType() {
		return svcType;
	}
	
	public void setSvcType(String svcType) {
		this.svcType = svcType;
	}


	public Integer getSvcPrice() {
		return svcPrice;
	}

	public void setSvcPrice(Integer svcPrice) {
		this.svcPrice = svcPrice;
	}

}
