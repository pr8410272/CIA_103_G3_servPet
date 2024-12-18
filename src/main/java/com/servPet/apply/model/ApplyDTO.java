package com.servPet.apply.model;

public class ApplyDTO {
	
	private  byte[] psPic;//個人照
	
	private  byte[] pgPic;
	
	 private String pgArea ="0";//服務地區 (DEFAULT:0 0:北部 1:中部 2:南部 3:東部)
	 
	 private String psArea ="0";
	  
	 private String schDate="0000000";//預設每週服務日 DEFAULT:0000000  0: 休 1:可預約
	 
	 private String schTime="000";  //預設每日預約時間時間  (DEFAULT:000 早中晚 0:休 1:可預約)
	 
	 
	public String getSchTime() {
		return schTime;
	}

	public void setSchTime(String schTime) {
		this.schTime = schTime;
	}

	public byte[] getPsPic() {
		return psPic;
	}

	public void setPsPic(byte[] psPic) {
		this.psPic = psPic;
	}

	public byte[] getPgPic() {
		return pgPic;
	}

	public void setPgPic(byte[] pgPic) {
		this.pgPic = pgPic;
	}

	public String getPgArea() {
		return pgArea;
	}

	public void setPgArea(String pgArea) {
		this.pgArea = pgArea;
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
	 
	 
}
