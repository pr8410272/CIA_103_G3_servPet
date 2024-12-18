package com.servPet.csIssue.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import com.servPet.admin.model.AdminVO;
import com.servPet.meb.model.MebVO;

import lombok.ToString;

@ToString(exclude = {"mebId", "adminId"})
@Entity
@Table(name = "CS_ISSUE")
public class CsIssueVO {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "CS_QA_ID")
	private Integer csQaId;

	// 多對一關係：問題屬於一個會員
	@ManyToOne
	@JoinColumn(name = "MEB_ID", nullable = false)
//    @Column(name = "MEB_ID", nullable = false)
	private MebVO mebId;

	// 多對一關係：問題屬於一個管理員
	@ManyToOne
	@JoinColumn(name = "ADMIN_ID")
//    @Column(name = "ADMIN_ID", nullable = true)
	private AdminVO adminId;

	@Column(name = "QA_CONTENT", nullable = false, length = 255)
	@NotEmpty(message = "諮詢內容: 請勿空白")
	@Size(min = 2, max = 225, message = "諮詢內容: 長度必需在2到225之間")
	private String qaContent;

	@Column(name = "QA_DATE", nullable = false)
	private LocalDateTime qaDate;

	@Column(name = "RE_CONTENT", length = 255)
	@Size(min = 2, max = 225, message = "諮詢內容: 長度必需在2到225之間")
	private String reContent;

	@Column(name = "RE_DATE")
	private LocalDateTime reDate;

	@Column(name = "QA_STA", nullable = false, length = 3)
	@NotEmpty(message = "狀態: 請勿空白")
	private String qaSta;

	public CsIssueVO() {
		super();
	}
	  public static final String STATUS_UNREAD = "0"; // 未讀
	    public static final String STATUS_READ = "1";  // 已讀
	    public static final String STATUS_UNPUBLISHED = "2"; // 下架

	public CsIssueVO(Integer csQaId, MebVO mebId, AdminVO adminId, String qaContent, LocalDateTime qaDate,
			String reContent, LocalDateTime reDate, String qaSta) {
		super();
		this.csQaId = csQaId;
		this.mebId = mebId;
		this.adminId = adminId;
		this.qaContent = qaContent;
		this.qaDate = qaDate;
		this.reContent = reContent;
		this.reDate = reDate;
		this.qaSta = qaSta;
	}

	public Integer getCsQaId() {
		return csQaId;
	}

	public void setCsQaId(Integer csQaId) {
		this.csQaId = csQaId;
	}

	public MebVO getMebId() {
		return mebId;
	}

	public void setMebId(MebVO mebId) {
		this.mebId = mebId;
	}

	public AdminVO getAdminId() {
		return adminId;
	}

	public void setAdminId(AdminVO adminId) {
		this.adminId = adminId;
	}

	public String getQaContent() {
		return qaContent;
	}

	public void setQaContent(String qaContent) {
		this.qaContent = qaContent;
	}

	public LocalDateTime getQaDate() {
		return qaDate;
	}

	public void setQaDate(LocalDateTime qaDate) {
		this.qaDate = qaDate;
	}

	public String getReContent() {
		return reContent != null ? reContent : "";
	}

	public void setReContent(String reContent) {
		this.reContent = reContent;
	}

	public LocalDateTime getReDate() {
		return reDate;
	}

	public void setReDate(LocalDateTime reDate) {
		this.reDate = reDate;
	}

	public String getQaSta() {
		return qaSta;
	}

	public void setQaSta(String qaSta) {
		this.qaSta = qaSta;
	}

//	@Override
//	public String toString() {
//	    return "CsIssueVO [csQaId=" + csQaId +
//	            ", qaContent=" + qaContent +
//	            ", qaDate=" + qaDate +
//	            ", reContent=" + reContent +
//	            ", reDate=" + reDate +
//	            ", qaSta=" + qaSta +
//	            ", mebId=" + (mebId != null ? mebId.getMebId() : "null") +
//	            ", adminId=" + (adminId != null ? adminId.getAdminId() : "null") +
//	            "]";
//	}
}
