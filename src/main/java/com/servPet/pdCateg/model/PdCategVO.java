package com.servPet.pdCateg.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import com.servPet.pdDetails.model.PdDetailsVO;

@Entity
@Table(name = "PRODUCT_CATEGORY")
public class PdCategVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "PD_CATEGORY")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer pdCategory;

    @Column(name = "CATEGORY_NAME")
    private String categoryName;

    @OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER, mappedBy = "pdCategVO")
    @OrderBy("pdId asc")
    private Set<PdDetailsVO> pdDetails = new HashSet<>();

    // Default constructor
    public PdCategVO() {}

    // Constructor with fields
    public PdCategVO(Integer pdCategory, String categoryName, Set<PdDetailsVO> pdDetails) {
        this.pdCategory = pdCategory;
        this.categoryName = categoryName;
        this.pdDetails = pdDetails;
    }

    // Getters and Setters
    public Integer getPdCategory() {
        return pdCategory;
    }

    public void setPdCategory(Integer pdCategory) {
        this.pdCategory = pdCategory;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Set<PdDetailsVO> getPdDetails() {
        return pdDetails;
    }

    public void setPdDetails(Set<PdDetailsVO> pdDetails) {
        this.pdDetails = pdDetails;
    }

    // toString
    @Override
    public String toString() {
        return "PdCategVO{" +
                "pdCategory=" + pdCategory +
                ", categoryName='" + categoryName + '\'' +
                '}';
    }

    // equals
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PdCategVO pdCategVO = (PdCategVO) o;
        return Objects.equals(pdCategory, pdCategVO.pdCategory);
    }

    // hashCode
    @Override
    public int hashCode() {
        return Objects.hash(pdCategory);
    }
}
