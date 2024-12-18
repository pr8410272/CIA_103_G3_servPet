package com.servPet.admin.model;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import java.util.Arrays;
import java.util.Objects;

import javax.persistence.Column;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Builder;


/*
 * 註1: classpath必須有javax.persistence-api-x.x.jar 
 * 註2: Annotation可以添加在屬性上，也可以添加在getXxx()方法之上
 */

@Entity  //要加上@Entity才能成為JPA的一個Entity類別
@Table(name = "admin")
@Builder
public class AdminVO implements java.io.Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "ADMIN_ID")  // 對應資料庫的 admin_id 欄位
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // 自動生成ID
    private Integer adminId;  // 屬性名稱改為 admin_id，符合Java命名慣例
    
    @Column(name = "ADMIN_NAME")  // 對應資料庫的 admin_name 欄位
    @Size(min = 2, max = 30, message = "請設定長度在{min}到{max}之間的名稱")
    private String adminName;  // 屬性名稱改為 admin_name，符合Java命名慣例
    
    @Column(name = "UPFILES")
//	@NotEmpty(message="員工照片: 請上傳照片") --> 由EmpController.java 第60行處理錯誤信息
    private byte[] upFiles;  // 若資料表中有存檔案，對應到資料庫的 UPFILES 欄位
    
    @Column(name = "ADMIN_ROLE")
    @NotNull(message = "請選擇管理員身分")
    private String adminRole;  // 管理員身分，對應資料庫的 admin_role
    
    @Column(name = "ADMIN_ACC_STATUS")
    @NotNull(message = "請選擇管理員狀態")
    private String adminAccStatus; // 預設為 "啟用"，管理員帳號狀態，對應資料庫的 admin_acc_status
    
    @Column(name = "ADMIN_ACC")
    @Size(min = 2, max = 12, message = "請設定長度在{min}到{max}之間的帳號")
    private String adminAcc;  // 管理員帳號，對應資料庫的 admin_acc
    
    @Column(name = "ADMIN_PWD")
    @Size(min = 8, max = 12, message = "請設定長度在{min}到{max}之間的密碼")
    private String adminPwd;  // 管理員密碼，對應資料庫的 admin_pwd

	public Integer getAdminId() {
		return adminId;
	}

	public void setAdminId(Integer adminId) {
		this.adminId = adminId;
	}

	public String getAdminName() {
		return adminName;
	}

	public void setAdminName(String adminName) {
		this.adminName = adminName;
	}

	public byte[] getUpFiles() {
		return upFiles;
	}

	public void setUpFiles(byte[] upFiles) {
		this.upFiles = upFiles;
	}

	public String getAdminRole() {
		return adminRole;
	}

	public void setAdminRole(String adminRole) {
		this.adminRole = adminRole;
	}

	public String getAdminAccStatus() {
		return adminAccStatus;
	}

	public void setAdminAccStatus(String adminAccStatus) {
		this.adminAccStatus = adminAccStatus;
	}

	public String getAdminAcc() {
		return adminAcc;
	}

	public void setAdminAcc(String adminAcc) {
		this.adminAcc = adminAcc;
	}

	public String getAdminPwd() {
		return adminPwd;
	}

	public void setAdminPwd(String adminPwd) {
		this.adminPwd = adminPwd;
	}

	@Override
	public String toString() {
		return "AdminVO [adminId=" + adminId + ", adminName=" + adminName + ", upFiles=" + Arrays.toString(upFiles)
				+ ", adminRole=" + adminRole + ", adminAccStatus=" + adminAccStatus + ", adminAcc=" + adminAcc
				+ ", adminPwd=" + adminPwd + ", getAdminId()=" + getAdminId() + ", getAdminName()=" + getAdminName()
				+ ", getUpFiles()=" + Arrays.toString(getUpFiles()) + ", getAdminRole()=" + getAdminRole()
				+ ", getAdminAccStatus()=" + getAdminAccStatus() + ", getAdminAcc()=" + getAdminAcc()
				+ ", getAdminPwd()=" + getAdminPwd() + ", getClass()=" + getClass() + ", hashCode()=" + hashCode()
				+ ", toString()=" + super.toString() + "]";
	}
	
	

	public AdminVO(Integer adminId,
			@Size(min = 2, max = 30, message = "請設定長度在{min}到{max}之間的名稱") String adminName, byte[] upFiles,
			@NotNull(message = "請選擇管理員身分") String adminRole, 
			@NotNull(message = "請選擇管理員狀態") String adminAccStatus,
			@Size(min = 2, max = 12, message = "請設定長度在{min}到{max}之間的帳號") String adminAcc,
			@Size(min = 8, max = 12, message = "請設定長度在{min}到{max}之間的密碼") String adminPwd) {
		super();
		this.adminId = adminId;
		this.adminName = adminName;
		this.upFiles = upFiles;
		this.adminRole = adminRole;
		this.adminAccStatus = adminAccStatus;
		this.adminAcc = adminAcc;
		this.adminPwd = adminPwd;
	}

	public AdminVO() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(upFiles);
		result = prime * result + Objects.hash(adminAcc, adminAccStatus, adminId, adminName, adminPwd, adminRole);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AdminVO other = (AdminVO) obj;
		return Objects.equals(adminAcc, other.adminAcc) && Objects.equals(adminAccStatus, other.adminAccStatus)
				&& Objects.equals(adminId, other.adminId) && Objects.equals(adminName, other.adminName)
				&& Objects.equals(adminPwd, other.adminPwd) && Objects.equals(adminRole, other.adminRole)
				&& Arrays.equals(upFiles, other.upFiles);
	}
    
    
}

