package com.servPet.ps.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.io.Serial;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "PET_SITTER")
public class PsVO implements java.io.Serializable {
	@Serial
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "PS_ID", nullable = false)
	private Integer psId;




	@Column(name = "PS_NAME",length = 50)
	@NotEmpty(message = "姓名請勿空白")
	@Pattern(regexp = "^[(\u4e00-\u9fa5)(a-zA-Z)]{2,50}$", message = "保母姓名: 只能是中、英文字母，且長度必需在2到50之間")
	private String psName;

	@Column(name = "PS_LICENSES")
	private byte[] psLicenses;

	@Lob
	@Column(name = "PS_PIC")
	private byte[] psPic;

	@Column(name = "PS_AREA", nullable = false, length = 1)
	@NotEmpty(message = "請選取可服務地區")
	private String psArea;

	@Column(name = "SCH_DATE")
	private String schDate;

	@Column(name = "SCH_TIME")
	private String schTime;

	@Column(name = "PS_STATUS")
	private String psStatus = "1";

	@Column(name = "PS_BIO", length = 300)
	private String psBio;

	@Column(name = "PS_PW", length = 20)
	private String psPw;

	@Column(name = "PS_EMAIL", nullable = false, length = 50)
	@NotEmpty(message = "請填寫聯絡信箱")
	@Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "聯絡信箱格式不正確")
	private String psEmail;

	@Column(name = "TOTAL_STARS", nullable = false)
	private Integer totalStars = 6;

	@Column(name = "RATING_TIMES", nullable = false)
	private Integer ratingTimes = 2;

	@Column(name = "REPORT_TIMES", nullable = false)
	private Integer reportTimes = 0;

	@Column(name = "APPROVAL_STATUS", nullable = false, length = 1)
	private String approvalStatus ;

	public PsVO(Integer psId) {
		this.psId = psId;
	}

	public Integer getPsId() {
		return psId;
	}

	public void setPsId(Integer psId) {
		this.psId = psId;
	}

	public String getPsName() {
		return psName;
	}

	public void setPsName(String psName) {
		this.psName = psName;
	}

	public byte[] getPsLicenses() {
		return psLicenses;
	}

	public void setPsLicenses(byte[] psLicenses) {
		this.psLicenses = psLicenses;
	}

	public byte[] getPsPic() {
		return psPic;
	}

	public void setPsPic(byte[] psPic) {
		this.psPic = psPic;
	}

	public String getPsArea() {
		return psArea;
	}

	public void setPsArea(String psArea) {
		this.psArea = psArea;
	}

	public String getSchDate() {
		return schDate;
	}

	public void setSchDate(String schDate) {
		this.schDate = schDate;
	}

	public String getSchTime() {
		return schTime;
	}

	public void setSchTime(String schTime) {
		this.schTime = schTime;
	}

	public String getPsStatus() {
		return psStatus;
	}

	public void setPsStatus(String psStatus) {
		this.psStatus = psStatus;
	}

	public String getPsBio() {
		return psBio;
	}

	public void setPsBio(String psBio) {
		this.psBio = psBio;
	}

	public String getPsPw() {
		return psPw;
	}

	public void setPsPw(String psPw) {
		this.psPw = psPw;
	}

	public String getPsEmail() {
		return psEmail;
	}

	public void setPsEmail(String psEmail) {
		this.psEmail = psEmail;
	}

	public Integer getTotalStars() {
		return totalStars;
	}

	public void setTotalStars(Integer totalStars) {
		this.totalStars = totalStars;
	}

	public Integer getRatingTimes() {
		return ratingTimes;
	}

	public void setRatingTimes(Integer ratingTimes) {
		this.ratingTimes = ratingTimes;
	}

	public Integer getReportTimes() {
		return reportTimes;
	}

	public void setReportTimes(Integer reportTimes) {
		this.reportTimes = reportTimes;
	}

	public String getApprovalStatus() {
		return approvalStatus;
	}

	public void setApprovalStatus(String approvalStatus) {
		this.approvalStatus = approvalStatus;
	}
}
