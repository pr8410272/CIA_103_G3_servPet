package com.servPet.pet.model;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

@Entity
@Table(name = "PET")
public class PetVO {

    @Id // 主鍵正確定義
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 自動生成策略
    @Column(name = "PET_ID")
    private Integer petId;

    @Column(name = "MEB_ID", nullable = false)
    private Integer mebId;

    @Column(name = "PET_NAME", nullable = false, length = 10)
    private String petName;

    @Column(name = "PET_TYPE", nullable = false, length = 10)
    private String petType;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "PET_IMG")
    private byte[] petImg;

    // Getters and Setters
    public Integer getPetId() {
        return petId;
    }

    public void setPetId(Integer petId) {
        this.petId = petId;
    }

    public Integer getMebId() { // 修改 Getter 名稱以對應欄位名稱
        return mebId;
    }

    public void setMebId(Integer mebId) { // 修改 Setter 名稱以對應欄位名稱
        this.mebId = mebId;
    }

    public String getPetName() {
        return petName;
    }

    public void setPetName(String petName) {
        this.petName = petName;
    }

    public String getPetType() {
        return petType;
    }

    public void setPetType(String petType) {
        this.petType = petType;
    }

    public byte[] getPetImg() {
        return petImg;
    }

    public void setPetImage(byte[] petImg) {
        this.petImg = petImg;
    }
}
