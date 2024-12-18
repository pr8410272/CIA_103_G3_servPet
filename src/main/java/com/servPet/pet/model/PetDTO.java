package com.servPet.pet.model;

import org.springframework.web.multipart.MultipartFile;

public class PetDTO {

    private Integer petId;       // 寵物 ID
    private Integer mebId;       // 會員 ID
    private String petName;      // 寵物名稱
    private String petType;      // 寵物品種
    private MultipartFile petImageFile;  // 寵物照片文件

    // Constructors
    public PetDTO() {
    }

    public PetDTO(Integer petId, Integer mebId, String petName, String petType, MultipartFile petImageFile) {
        this.petId = petId;
        this.mebId = mebId;
        this.petName = petName;
        this.petType = petType;
        this.petImageFile = petImageFile;
    }

    // Getters and Setters
    public Integer getPetId() {
        return petId;
    }

    public void setPetId(Integer petId) {
        this.petId = petId;
    }

    public Integer getMebId() {
        return mebId;
    }

    public void setMebId(Integer mebId) {
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

    public MultipartFile getPetImageFile() { // 修正名稱
        return petImageFile;
    }

    public void setPetImageFile(MultipartFile petImageFile) { // 修正名稱
        this.petImageFile = petImageFile;
    }

    // toString method for debugging
    @Override
    public String toString() {
        return "PetDTO{" +
                "petId=" + petId +
                ", mebId=" + mebId +
                ", petName='" + petName + '\'' +
                ", petType='" + petType + '\'' +
                ", petImageFile='" + petImageFile + '\'' +
                '}';
    }
}
