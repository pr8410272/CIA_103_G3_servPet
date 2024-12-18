package com.servPet.psCompl.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Objects;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

import com.servPet.meb.model.MebVO;
import com.servPet.ps.model.PsVO;

import lombok.Builder;
/**
 * 實體類別對應資料表 PET_SITTER_COMPLAINT
 * 
 * 註1: classpath必須有javax.persistence-api-x.x.jar
 * 註2: Annotation 可以添加在屬性上，也可以添加在 getXxx() 方法上
 * @param <PetSitter>
 */

@Entity  // 表示該類別是一個 JPA 實體
@Table(name = "PET_SITTER_COMPLAINT")  // 對應資料表 PET_SITTER_COMPLAINT
@Builder
public class PsComplVO<PetSitter> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PS_COMPL_ID")  // 對應資料庫的 PS_COMPL_ID 欄位
    private Integer psComplId;  // 檢舉單編號

    @ManyToOne
    @JoinColumn(name = "PS_ID", referencedColumnName = "PS_ID", insertable=false, updatable=false, nullable = false)
    private PsVO psVO;  // 代表保母編號，對應 `PET_SITTER` 表格

    @Column(name = "PS_ID")
    private Integer psId;  // 只保存 psId，而不需要加載 PsVO
    
    @ManyToOne
    @JoinColumn(name = "MEB_ID", referencedColumnName = "MEB_ID",insertable=false, updatable=false, nullable = false)
    private MebVO mebVO;  // 代表會員編號，對應 `MEMBER` 表格
    
    @Column(name = "MEB_ID")
    private Integer mebId;  // 只保存 mebId，而不需要加載 MebVO

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "PS_COMPL_DATE")  // 對應資料庫的 PS_COMPL_DATE 欄位
    private LocalDateTime psComplDate;  // 檢舉日期，使用 LocalDateTime 處理日期時間

    @Column(name = "PS_COMPL_RESULT")  // 對應資料庫的 PS_COMPL_RESULT 欄位
    @Size(max = 255, message = "檢舉回覆不能超過 255 個字元")
    private String psComplResult;  // 檢舉處理結果

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "PS_COMPL_RES_DATE")  // 對應資料庫的 PS_COMPL_RES_DATE 欄位
    private LocalDateTime psComplResDate;  // 檢舉處理日期，使用 LocalDateTime 處理日期時間

    @Column(name = "PS_COMPL_DES")  // 對應資料庫的 PS_COMPL_DES 欄位
    @NotEmpty(message = "請填寫檢舉描述")
    @Size(max = 255, message = "描述不能超過 255 個字元")
    private String psComplDes;  // 檢舉描述

    @Column(name = "PS_COMPL_STATUS")  // 對應資料庫的 PS_COMPL_STATUS 欄位
    @Size(min = 1, max = 1)
    private String psComplStatus = "0";  // 案件處理狀態，預設值為 0: 未處理

    @Lob
    @Column(name = "PS_COMPL_UPFILES_1")  // 對應資料庫的 PS_COMPL_UPFILES_1 欄位
    private byte[] psComplUpfiles1;  // 檢舉圖片1，使用 byte[] 存儲 BLOB 類型資料

    @Lob
    @Column(name = "PS_COMPL_UPFILES_2")  // 對應資料庫的 PS_COMPL_UPFILES_2 欄位
    private byte[] psComplUpfiles2;  // 檢舉圖片2，使用 byte[] 存儲 BLOB 類型資料

    @Lob
    @Column(name = "PS_COMPL_UPFILES_3")  // 對應資料庫的 PS_COMPL_UPFILES_3 欄位
    private byte[] psComplUpfiles3;  // 檢舉圖片3，使用 byte[] 存儲 BLOB 類型資料

    @Lob
    @Column(name = "PS_COMPL_UPFILES_4")  // 對應資料庫的 PS_COMPL_UPFILES_4 欄位
    private byte[] psComplUpfiles4;  // 檢舉圖片4，使用 byte[] 存儲 BLOB 類型資料

	public Integer getPsComplId() {
		return psComplId;
	}

	public void setPsComplId(Integer psComplId) {
		this.psComplId = psComplId;
	}

	public PsVO getPsVO() {
		return psVO;
	}

	public void setPsVO(PsVO psVO) {
		this.psVO = psVO;
	}

	public Integer getPsId() {
		return psId;
	}

	public void setPsId(Integer psId) {
		this.psId = psId;
	}

	public MebVO getMebVO() {
		return mebVO;
	}

	public void setMebVO(MebVO mebVO) {
		this.mebVO = mebVO;
	}

	public Integer getMebId() {
		return mebId;
	}

	public void setMebId(Integer mebId) {
		this.mebId = mebId;
	}

	public LocalDateTime getPsComplDate() {
		return psComplDate;
	}

	public void setPsComplDate(LocalDateTime psComplDate) {
		this.psComplDate = psComplDate;
	}

	public String getPsComplResult() {
		return psComplResult;
	}

	public void setPsComplResult(String psComplResult) {
		this.psComplResult = psComplResult;
	}

	public LocalDateTime getPsComplResDate() {
		return psComplResDate;
	}

	public void setPsComplResDate(LocalDateTime psComplResDate) {
		this.psComplResDate = psComplResDate;
	}

	public String getPsComplDes() {
		return psComplDes;
	}

	public void setPsComplDes(String psComplDes) {
		this.psComplDes = psComplDes;
	}

	public String getPsComplStatus() {
		return psComplStatus;
	}
	
	public String getPreviousPsComplStatus() {
		return psComplStatus;
	}

	public void setPsComplStatus(String psComplStatus) {
		this.psComplStatus = psComplStatus;
	}
	
	public void setPreviousPsComplStatus(String psComplStatus) {
		this.psComplStatus = psComplStatus;
		
	}

	public byte[] getPsComplUpfiles1() {
		return psComplUpfiles1;
	}

	public void setPsComplUpfiles1(byte[] psComplUpfiles1) {
		this.psComplUpfiles1 = psComplUpfiles1;
	}

	public byte[] getPsComplUpfiles2() {
		return psComplUpfiles2;
	}

	public void setPsComplUpfiles2(byte[] psComplUpfiles2) {
		this.psComplUpfiles2 = psComplUpfiles2;
	}

	public byte[] getPsComplUpfiles3() {
		return psComplUpfiles3;
	}

	public void setPsComplUpfiles3(byte[] psComplUpfiles3) {
		this.psComplUpfiles3 = psComplUpfiles3;
	}

	public byte[] getPsComplUpfiles4() {
		return psComplUpfiles4;
	}

	public void setPsComplUpfiles4(byte[] psComplUpfiles4) {
		this.psComplUpfiles4 = psComplUpfiles4;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(psComplUpfiles1);
		result = prime * result + Arrays.hashCode(psComplUpfiles2);
		result = prime * result + Arrays.hashCode(psComplUpfiles3);
		result = prime * result + Arrays.hashCode(psComplUpfiles4);
		result = prime * result + Objects.hash(mebId, mebVO, psComplDate, psComplDes, psComplId, psComplResDate,
				psComplResult, psComplStatus, psId, psVO);
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
		PsComplVO other = (PsComplVO) obj;
		return Objects.equals(mebId, other.mebId) && Objects.equals(mebVO, other.mebVO)
				&& Objects.equals(psComplDate, other.psComplDate) && Objects.equals(psComplDes, other.psComplDes)
				&& Objects.equals(psComplId, other.psComplId) && Objects.equals(psComplResDate, other.psComplResDate)
				&& Objects.equals(psComplResult, other.psComplResult)
				&& Objects.equals(psComplStatus, other.psComplStatus)
				&& Arrays.equals(psComplUpfiles1, other.psComplUpfiles1)
				&& Arrays.equals(psComplUpfiles2, other.psComplUpfiles2)
				&& Arrays.equals(psComplUpfiles3, other.psComplUpfiles3)
				&& Arrays.equals(psComplUpfiles4, other.psComplUpfiles4) && Objects.equals(psId, other.psId)
				&& Objects.equals(psVO, other.psVO);
	}

	@Override
	public String toString() {
		return "PsComplVO [psComplId=" + psComplId + ", psVO=" + psVO + ", psId=" + psId + ", mebVO=" + mebVO
				+ ", mebId=" + mebId + ", psComplDate=" + psComplDate + ", psComplResult=" + psComplResult
				+ ", psComplResDate=" + psComplResDate + ", psComplDes=" + psComplDes + ", psComplStatus="
				+ psComplStatus + ", psComplUpfiles1=" + Arrays.toString(psComplUpfiles1) + ", psComplUpfiles2="
				+ Arrays.toString(psComplUpfiles2) + ", psComplUpfiles3=" + Arrays.toString(psComplUpfiles3)
				+ ", psComplUpfiles4=" + Arrays.toString(psComplUpfiles4) + ", getPsComplId()=" + getPsComplId()
				+ ", getPsVO()=" + getPsVO() + ", getPsId()=" + getPsId() + ", getMebVO()=" + getMebVO()
				+ ", getMebId()=" + getMebId() + ", getPsComplDate()=" + getPsComplDate() + ", getPsComplResult()="
				+ getPsComplResult() + ", getPsComplResDate()=" + getPsComplResDate() + ", getPsComplDes()="
				+ getPsComplDes() + ", getPsComplStatus()=" + getPsComplStatus() + ", getPsComplUpfiles1()="
				+ Arrays.toString(getPsComplUpfiles1()) + ", getPsComplUpfiles2()="
				+ Arrays.toString(getPsComplUpfiles2()) + ", getPsComplUpfiles3()="
				+ Arrays.toString(getPsComplUpfiles3()) + ", getPsComplUpfiles4()="
				+ Arrays.toString(getPsComplUpfiles4()) + ", hashCode()=" + hashCode() + ", getClass()=" + getClass()
				+ ", toString()=" + super.toString() + "]";
	}

	public PsComplVO(Integer psComplId, PsVO psVO, Integer psId, MebVO mebVO, Integer mebId, LocalDateTime psComplDate,
			@Size(max = 255, message = "檢舉回覆不能超過 255 個字元") String psComplResult, LocalDateTime psComplResDate,
			@NotEmpty(message = "請填寫檢舉描述") @Size(max = 255, message = "描述不能超過 255 個字元") String psComplDes,
			@Size(min = 1, max = 1) String psComplStatus, byte[] psComplUpfiles1, byte[] psComplUpfiles2,
			byte[] psComplUpfiles3, byte[] psComplUpfiles4) {
		super();
		this.psComplId = psComplId;
		this.psVO = psVO;
		this.psId = psId;
		this.mebVO = mebVO;
		this.mebId = mebId;
		this.psComplDate = psComplDate;
		this.psComplResult = psComplResult;
		this.psComplResDate = psComplResDate;
		this.psComplDes = psComplDes;
		this.psComplStatus = psComplStatus;
		this.psComplUpfiles1 = psComplUpfiles1;
		this.psComplUpfiles2 = psComplUpfiles2;
		this.psComplUpfiles3 = psComplUpfiles3;
		this.psComplUpfiles4 = psComplUpfiles4;
	}

	public PsComplVO() {
		super();
		// TODO Auto-generated constructor stub
	}

}
