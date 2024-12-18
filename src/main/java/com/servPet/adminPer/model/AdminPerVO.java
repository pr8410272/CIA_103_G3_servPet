package com.servPet.adminPer.model;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

import com.servPet.admin.model.AdminVO;
import com.servPet.fnc.model.FncVO;

import lombok.Builder;

/**
 * AdminPerVO 對應到資料庫的 ADMIN_PER 表格。
 * 記錄管理員的權限。
 */
@Entity
@Table(name = "ADMIN_PER")
@Builder
public class AdminPerVO implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // 主鍵自動遞增
    private Integer adminPerId;  // 權限類別編號，從 26000 開始自動遞增

    @ManyToOne  // 透過 ManyToOne 關聯
    @JoinColumn(name = "ADMIN_ID", referencedColumnName = "ADMIN_ID")  // 參照 ADMIN 表格的 ADMIN_ID
    private AdminVO adminVO;  // 這是管理員 VO，對應資料庫的 ADMIN_ID

    @ManyToOne  // 透過 ManyToOne 關聯
    @JoinColumn(name = "FNC_ID", referencedColumnName = "FNC_ID")  // 參照 FNC 表格的 FNC_ID
    private FncVO fncVO;  // 這是權限功能 VO，對應資料庫的 FNC_ID

	public Integer getAdminPerId() {
		return adminPerId;
	}

	public void setAdminPerId(Integer adminPerId) {
		this.adminPerId = adminPerId;
	}

	public AdminVO getAdminVO() {
		return adminVO;
	}

	public void setAdminVO(AdminVO adminVO) {
		this.adminVO = adminVO;
	}

	public FncVO getFncVO() {
		return fncVO;
	}

	public void setFncVO(FncVO fncVO) {
		this.fncVO = fncVO;
	}

	@Override
	public int hashCode() {
		return Objects.hash(adminPerId, adminVO, fncVO);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AdminPerVO other = (AdminPerVO) obj;
		return Objects.equals(adminPerId, other.adminPerId) && Objects.equals(adminVO, other.adminVO)
				&& Objects.equals(fncVO, other.fncVO);
	}

	@Override
	public String toString() {
		return "AdminPerVO [adminPerId=" + adminPerId + ", adminVO=" + adminVO + ", fncVO=" + fncVO
				+ ", getAdminPerId()=" + getAdminPerId() + ", getAdminVO()=" + getAdminVO() + ", getFncVO()="
				+ getFncVO() + ", hashCode()=" + hashCode() + ", getClass()=" + getClass() + ", toString()="
				+ super.toString() + "]";
	}

	public AdminPerVO(Integer adminPerId, AdminVO adminVO, FncVO fncVO) {
		super();
		this.adminPerId = adminPerId;
		this.adminVO = adminVO;
		this.fncVO = fncVO;
	}

	public AdminPerVO() {
		super();
		// TODO Auto-generated constructor stub
	}

}