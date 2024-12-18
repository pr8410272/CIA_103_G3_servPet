package com.servPet.pgCompl.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Objects;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

import com.servPet.meb.model.MebVO;
import com.servPet.pg.model.PgVO;

import lombok.Builder;
/**
 * 實體類別對應資料表 PET_SITTER_COMPLAINT
 * 
 * 註1: classpath必須有javax.persistence-api-x.x.jar
 * 註2: Annotation 可以添加在屬性上，也可以添加在 getXxx() 方法上
 * @param <PetGroomer>
 */

@Entity  // 表示該類別是一個 JPA 實體
@Table(name = "PET_GROOMER_COMPLAINT")  // 對應資料表 PET_SITTER_COMPLAINT
@Builder
public class PgComplVO<PetGroomer> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PG_COMPL_ID")  // 對應資料庫的 PG_COMPL_ID 欄位
    private Integer pgComplId;  // 檢舉單編號

    @ManyToOne
    @JoinColumn(name = "PG_ID", referencedColumnName = "PG_ID", insertable=false, updatable=false, nullable = false)
    private PgVO pgVO;  // 代表保母編號，對應 `PET_GROOMER` 表格

    @Column(name = "PG_ID")
    private Integer pgId;  // 只保存 pgId，而不需要加載 PgVO
    
    @ManyToOne
    @JoinColumn(name = "MEB_ID", referencedColumnName = "MEB_ID",insertable=false, updatable=false, nullable = false)
    private MebVO mebVO;  // 代表會員編號，對應 `MEMBER` 表格
    
    @Column(name = "MEB_ID")
    private Integer mebId;  // 只保存 mebId，而不需要加載 MebVO

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "PG_COMPL_DATE")  // 對應資料庫的 PG_COMPL_DATE 欄位
    private LocalDateTime pgComplDate;  // 檢舉日期，使用 LocalDateTime 處理日期時間

    @Column(name = "PG_COMPL_RESULT")  // 對應資料庫的 PG_COMPL_RESULT 欄位
    @Size(max = 255, message = "檢舉回覆不能超過 255 個字元")
    private String pgComplResult;  // 檢舉處理結果

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "PG_COMPL_RES_DATE")  // 對應資料庫的 PG_COMPL_RES_DATE 欄位
    private LocalDateTime pgComplResDate;  // 檢舉處理日期，使用 LocalDateTime 處理日期時間

    @Column(name = "PG_COMPL_DES")  // 對應資料庫的 PG_COMPL_DES 欄位
    @NotEmpty(message = "請填寫檢舉描述")
    @Size(max = 255, message = "描述不能超過 255 個字元")
    private String pgComplDes;  // 檢舉描述

    @Column(name = "PG_COMPL_STATUS")  // 對應資料庫的 PG_COMPL_STATUS 欄位
    @Size(min = 1, max = 1)
    private String pgComplStatus = "0";  // 案件處理狀態，預設值為 0: 未處理

    @Lob
    @Column(name = "PG_COMPL_UPFILES_1")  // 對應資料庫的 PG_COMPL_UPFILES_1 欄位
    private byte[] pgComplUpfiles1;  // 檢舉圖片1，使用 byte[] 存儲 BLOB 類型資料

    @Lob
    @Column(name = "PG_COMPL_UPFILES_2")  // 對應資料庫的 PG_COMPL_UPFILES_2 欄位
    private byte[] pgComplUpfiles2;  // 檢舉圖片2，使用 byte[] 存儲 BLOB 類型資料

    @Lob
    @Column(name = "PG_COMPL_UPFILES_3")  // 對應資料庫的 PG_COMPL_UPFILES_3 欄位
    private byte[] pgComplUpfiles3;  // 檢舉圖片3，使用 byte[] 存儲 BLOB 類型資料

    @Lob
    @Column(name = "PG_COMPL_UPFILES_4")  // 對應資料庫的 PG_COMPL_UPFILES_4 欄位
    private byte[] pgComplUpfiles4;  // 檢舉圖片4，使用 byte[] 存儲 BLOB 類型資料

	public Integer getPgComplId() {
		return pgComplId;
	}

	public void setPgComplId(Integer pgComplId) {
		this.pgComplId = pgComplId;
	}

	public PgVO getPgVO() {
		return pgVO;
	}

	public void setPgVO(PgVO pgVO) {
		this.pgVO = pgVO;
	}

	public Integer getPgId() {
		return pgId;
	}

	public void setPgId(Integer pgId) {
		this.pgId = pgId;
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

	public LocalDateTime getPgComplDate() {
		return pgComplDate;
	}

	public void setPgComplDate(LocalDateTime pgComplDate) {
		this.pgComplDate = pgComplDate;
	}

	public String getPgComplResult() {
		return pgComplResult;
	}

	public void setPgComplResult(String pgComplResult) {
		this.pgComplResult = pgComplResult;
	}

	public LocalDateTime getPgComplResDate() {
		return pgComplResDate;
	}

	public void setPgComplResDate(LocalDateTime pgComplResDate) {
		this.pgComplResDate = pgComplResDate;
	}

	public String getPgComplDes() {
		return pgComplDes;
	}

	public void setPgComplDes(String pgComplDes) {
		this.pgComplDes = pgComplDes;
	}

	public String getPgComplStatus() {
		return pgComplStatus;
	}

	public void setPgComplStatus(String pgComplStatus) {
		this.pgComplStatus = pgComplStatus;
	}
	
	public void setPreviousPgComplStatus(String pgComplStatus) {
		this.pgComplStatus = pgComplStatus;
		
	}

	public byte[] getPgComplUpfiles1() {
		return pgComplUpfiles1;
	}

	public void setPgComplUpfiles1(byte[] pgComplUpfiles1) {
		this.pgComplUpfiles1 = pgComplUpfiles1;
	}

	public byte[] getPgComplUpfiles2() {
		return pgComplUpfiles2;
	}

	public void setPgComplUpfiles2(byte[] pgComplUpfiles2) {
		this.pgComplUpfiles2 = pgComplUpfiles2;
	}

	public byte[] getPgComplUpfiles3() {
		return pgComplUpfiles3;
	}

	public void setPgComplUpfiles3(byte[] pgComplUpfiles3) {
		this.pgComplUpfiles3 = pgComplUpfiles3;
	}

	public byte[] getPgComplUpfiles4() {
		return pgComplUpfiles4;
	}

	public void setPgComplUpfiles4(byte[] pgComplUpfiles4) {
		this.pgComplUpfiles4 = pgComplUpfiles4;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(pgComplUpfiles1);
		result = prime * result + Arrays.hashCode(pgComplUpfiles2);
		result = prime * result + Arrays.hashCode(pgComplUpfiles3);
		result = prime * result + Arrays.hashCode(pgComplUpfiles4);
		result = prime * result + Objects.hash(mebId, mebVO, pgComplDate, pgComplDes, pgComplId, pgComplResDate,
				pgComplResult, pgComplStatus, pgId, pgVO);
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
		PgComplVO other = (PgComplVO) obj;
		return Objects.equals(mebId, other.mebId) && Objects.equals(mebVO, other.mebVO)
				&& Objects.equals(pgComplDate, other.pgComplDate) && Objects.equals(pgComplDes, other.pgComplDes)
				&& Objects.equals(pgComplId, other.pgComplId) && Objects.equals(pgComplResDate, other.pgComplResDate)
				&& Objects.equals(pgComplResult, other.pgComplResult)
				&& Objects.equals(pgComplStatus, other.pgComplStatus)
				&& Arrays.equals(pgComplUpfiles1, other.pgComplUpfiles1)
				&& Arrays.equals(pgComplUpfiles2, other.pgComplUpfiles2)
				&& Arrays.equals(pgComplUpfiles3, other.pgComplUpfiles3)
				&& Arrays.equals(pgComplUpfiles4, other.pgComplUpfiles4) && Objects.equals(pgId, other.pgId)
				&& Objects.equals(pgVO, other.pgVO);
	}

	@Override
	public String toString() {
		return "PgComplVO [pgComplId=" + pgComplId + ", pgVO=" + pgVO + ", pgId=" + pgId + ", mebVO=" + mebVO
				+ ", mebId=" + mebId + ", pgComplDate=" + pgComplDate + ", pgComplResult=" + pgComplResult
				+ ", pgComplResDate=" + pgComplResDate + ", pgComplDes=" + pgComplDes + ", pgComplStatus="
				+ pgComplStatus + ", pgComplUpfiles1=" + Arrays.toString(pgComplUpfiles1) + ", pgComplUpfiles2="
				+ Arrays.toString(pgComplUpfiles2) + ", pgComplUpfiles3=" + Arrays.toString(pgComplUpfiles3)
				+ ", pgComplUpfiles4=" + Arrays.toString(pgComplUpfiles4) + ", getPgComplId()=" + getPgComplId()
				+ ", getPgVO()=" + getPgVO() + ", getPgId()=" + getPgId() + ", getMebVO()=" + getMebVO()
				+ ", getMebId()=" + getMebId() + ", getPgComplDate()=" + getPgComplDate() + ", getPgComplResult()="
				+ getPgComplResult() + ", getPgComplResDate()=" + getPgComplResDate() + ", getPgComplDes()="
				+ getPgComplDes() + ", getPgComplStatus()=" + getPgComplStatus() + ", getPgComplUpfiles1()="
				+ Arrays.toString(getPgComplUpfiles1()) + ", getPgComplUpfiles2()="
				+ Arrays.toString(getPgComplUpfiles2()) + ", getPgComplUpfiles3()="
				+ Arrays.toString(getPgComplUpfiles3()) + ", getPgComplUpfiles4()="
				+ Arrays.toString(getPgComplUpfiles4()) + ", hashCode()=" + hashCode() + ", getClass()=" + getClass()
				+ ", toString()=" + super.toString() + "]";
	}

	public PgComplVO(Integer pgComplId, PgVO pgVO, Integer pgId, MebVO mebVO, Integer mebId, LocalDateTime pgComplDate,
			@Size(max = 255, message = "檢舉回覆不能超過 255 個字元") String pgComplResult, LocalDateTime pgComplResDate,
			@NotEmpty(message = "請填寫檢舉描述") @Size(max = 255, message = "描述不能超過 255 個字元") String pgComplDes,
			@Size(min = 1, max = 1) String pgComplStatus, byte[] pgComplUpfiles1, byte[] pgComplUpfiles2,
			byte[] pgComplUpfiles3, byte[] pgComplUpfiles4) {
		super();
		this.pgComplId = pgComplId;
		this.pgVO = pgVO;
		this.pgId = pgId;
		this.mebVO = mebVO;
		this.mebId = mebId;
		this.pgComplDate = pgComplDate;
		this.pgComplResult = pgComplResult;
		this.pgComplResDate = pgComplResDate;
		this.pgComplDes = pgComplDes;
		this.pgComplStatus = pgComplStatus;
		this.pgComplUpfiles1 = pgComplUpfiles1;
		this.pgComplUpfiles2 = pgComplUpfiles2;
		this.pgComplUpfiles3 = pgComplUpfiles3;
		this.pgComplUpfiles4 = pgComplUpfiles4;
	}

	public PgComplVO() {
		super();
		// TODO Auto-generated constructor stub
	}

}
