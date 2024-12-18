package com.servPet.apply.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

import com.servPet.meb.model.MebVO;



@Entity
@Table(name = "apply")
public class ApplyVO {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "APPLY_ID")
    private int applyId; 
	
	@ManyToOne
	@JoinColumn(name = "MEB_ID",referencedColumnName = "MEB_ID", nullable = false)
    private MebVO mebId; 
	
	@Column(name = "APPLY_CATE")
	@NotEmpty(message = "申請類別: 請勿空白")
    private String applyCate; // 申請類別 (0為寵物美容師, 1為寵物保母)
	
	@Lob
	@Column(name = "APPLY_PIC")
//	@NotNull(message = "證件照: 請記得提供證件照")
    private byte[] applyPic; // 證件照
	
	@Column(name = "APPLY_STATUS")
    private String applyStatus; // 申請狀態 (0-未審核, 1-審核通過, 2-審核不通過 )
	
	@Column(name = "APPLY_TIME")
    private LocalDateTime applyTime; // 申請時間
	
	@Column(name = "APPLY_UPDATE_TIME")
    private LocalDateTime applyUpdateTime; // 審核時間 (更新時間)
	
	@Column(name = "APPLY_REJECT_REASON")
    private String applyRejectReason; // 駁回原因 (僅在審核不通過時填寫)
	
	
    
    public ApplyVO() {
		super();
	}



	public ApplyVO(int applyId, MebVO mebId, @NotEmpty(message = "申請類別: 請勿空白") String applyCate,
			@NotEmpty(message = "證件照: 請記得提供證件照") byte[] applyPic, String applyStatus, LocalDateTime applyTime,
			LocalDateTime applyUpdateTime, String applyRejectReason) {
		super();
		this.applyId = applyId;
		this.mebId = mebId;
		this.applyCate = applyCate;
		this.applyPic = applyPic;
		this.applyStatus = applyStatus;
		this.applyTime = applyTime;
		this.applyUpdateTime = applyUpdateTime;
		this.applyRejectReason = applyRejectReason;
	}



	public int getApplyId() {
		return applyId;
	}



	public void setApplyId(int applyId) {
		this.applyId = applyId;
	}



	public MebVO getMebId() {
		return mebId;
	}



	public void setMebId(MebVO mebId) {
		this.mebId = mebId;
	}



	public String getApplyCate() {
		return applyCate;
	}



	public void setApplyCate(String applyCate) {
		this.applyCate = applyCate;
	}



	public byte[] getApplyPic() {
		return applyPic;
	}



	public void setApplyPic(byte[] applyPic) {
		this.applyPic = applyPic;
	}



	public String getApplyStatus() {
		return applyStatus;
	}



	public void setApplyStatus(String applyStatus) {
		this.applyStatus = applyStatus;
	}



	public LocalDateTime getApplyTime() {
		return applyTime;
	}



	public void setApplyTime(LocalDateTime applyTime) {
		this.applyTime = applyTime;
	}



	public LocalDateTime getApplyUpdateTime() {
		return applyUpdateTime;
	}



	public void setApplyUpdateTime(LocalDateTime applyUpdateTime) {
		this.applyUpdateTime = applyUpdateTime;
	}



	public String getApplyRejectReason() {
		return applyRejectReason;
	}



	public void setApplyRejectReason(String applyRejectReason) {
		this.applyRejectReason = applyRejectReason;
	}
    
	public void setMebId(int mebId) {
        MebVO member = new MebVO();
        member.setMebId(mebId); // 假设 MebVO 有 setMebId 方法
        this.mebId = member;
    }

	
}
