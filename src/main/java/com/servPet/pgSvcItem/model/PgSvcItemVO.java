package com.servPet.pgSvcItem.model;
//美容服務總覽

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

@Entity
@Table(name = "PET_GROOMER_SERVICE_ITEM")
public class PgSvcItemVO implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	public PgSvcItemVO() {

	}

	@Id // 主鍵
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "SVC_ID")
	private Integer svcId;

	@Column(name = "SVC_DESCR", nullable = false)
	@NotEmpty(message = "請簡短描述服務項目")
//	@Pattern(regexp = "^[(\u4e00-\u9fa5)(a-zA-Z0-9_+&$@#())]{2,200}$", message = "服務簡介: 只能是中、英文字母、數字和+等指定符號, 且長度必須在2到200之間")
	private String svcDescr;

	@Column(name = "SVC_NAME", nullable = false)
	@NotEmpty(message = "請填寫服務項目名稱")
//	@Pattern(regexp = "^[(\u4e00-\u9fa5)(a-zA-Z0-9_+&$@#())]{2,10}$", message = "服務項目: 只能是中、英文字母、數字和+等指定符號, 且長度必須在2到10之間")
	private String svcName;

	@Column(name = "SVC_IS_DELETED", nullable = false)
	private String svcIsDeleted = "0";

	public String getSvcIsDeleted() {
		return svcIsDeleted;
	}

	public void setSvcIsDeleted(String svcIsDeleted) {
		this.svcIsDeleted = svcIsDeleted;
	}

	public Integer getSvcId() {
		return svcId;
	}

	public void setSvcId(Integer svcId) {
		this.svcId = svcId;
	}

	public String getSvcDescr() {
		return svcDescr;
	}

	public void setSvcDescr(String svcDescr) {
		this.svcDescr = svcDescr;
	}

	public String getSvcName() {
		return svcName;
	}

	public void setSvcName(String svcName) {
		this.svcName = svcName;
	}

}

