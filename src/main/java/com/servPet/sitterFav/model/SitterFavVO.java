package com.servPet.sitterFav.model;
	
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
	
	@Entity
	@Table(name = "PET_SITTER_FAVORITE")
	public class SitterFavVO implements java.io.Serializable {
	    private static final long serialVersionUID = 1L;
	
	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    @Column(name = "SITTER_FAVO_ID")
	    private Integer sitterFavoId;
	
	    @Column(name = "PS_ID", nullable = false)
	    private Integer psId;
	
	    @Column(name = "MEB_ID", nullable = false) 
	    private Integer mebId;
	
	   
	
	    public SitterFavVO() {}
	
	    public Integer getSitterFavoId() {
	        return sitterFavoId;
	    }
	
	    public void setSitterFavoId(Integer sitterFavoId) {
	        this.sitterFavoId = sitterFavoId;
	    }
	
	    public Integer getPsId() {
	        return psId;
	    }
	
	    public void setPsId(Integer psId) {
	        this.psId = psId;
	    }
	
	    public Integer getMebId() {
	        return mebId;
	    }
	
	    public void setMebId(Integer mebId) {
	        this.mebId = mebId;
	    }
	
	    @Override
	    public String toString() {
	        return "SitterFavVO [sitterFavoId=" + sitterFavoId + ", psId=" + psId + ", mebId=" + mebId + "]";
	    }
	}
