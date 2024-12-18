package com.servPet.ntfy.model;

import javax.persistence.*;
//import javax.validation.constraints.NotEmpty;
//import javax.validation.constraints.NotNull;

import com.servPet.meb.model.MebVO;

import java.sql.Timestamp;
//import java.time.LocalDateTime;

@Entity
@Table(name = "ntfy_mgmt")  
public class NtfyVO implements java.io.Serializable {
	private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  
    @Column(name = "NTFY_MGMT_ID") 
    private Integer ntfyMgmtId;

    @ManyToOne
    @JoinColumn(name = "MEB_ID", referencedColumnName = "MEB_ID")  
    private MebVO mebVO;
    
    @Column(name = "TITLE", nullable = false, length = 100) 
//    @NotEmpty(message="此欄不能空白")
    private String title;

    
    @Column(name = "CONTENT", nullable = false) 
//    @NotEmpty(message="此欄不能空白")
    private String content;

    @Column(name = "DATE", nullable = false)  
//    @NotNull(message="請選擇新增日期")
    private Timestamp  date;  

    
    @Column(name = "STATUS", nullable = false) 
    private Integer status;



	public NtfyVO() {
		super();
	}

	public NtfyVO(MebVO mebVO) {
		this.mebVO = mebVO;
	}
	
	public MebVO getMebVO() {
	    return mebVO;
	}

	public void setMebVO(MebVO mebVO) {
	    this.mebVO = mebVO;
	}


	public Integer getNtfyMgmtId() {
		return ntfyMgmtId;
	}


	public void setNtfyMgmtId(Integer ntfyMgmtId) {
		this.ntfyMgmtId = ntfyMgmtId;
	}


	public String getTitle() {
		return title;
	}


	public void setTitle(String title) {
		this.title = title;
	}


	public String getContent() {
		return content;
	}


	public void setContent(String content) {
		this.content = content;
	}


	public Timestamp getDate() {
		return date;
	}


	public void setDate(Timestamp date) {
		this.date = date;
	}


	public Integer getStatus() {
		return status;
	}


	public void setStatus(Integer status) {
		this.status = status;
	}


	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	

}
    
    



