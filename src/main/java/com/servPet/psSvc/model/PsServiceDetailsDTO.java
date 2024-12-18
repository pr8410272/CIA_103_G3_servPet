package com.servPet.psSvc.model;

import com.servPet.psSvc.model.PsSvcVO;

public class PsServiceDetailsDTO {

    private Integer psId;
    private Integer svcId;

    private Integer svcPrice;
    private String svcName;
    private String svcDescr;
    private String svcIsDeleted;

    public PsServiceDetailsDTO() {
    }

    public PsServiceDetailsDTO(Integer psId, Integer svcId, Integer svcPrice, String svcName, String svcDescr, String svcIsDeleted) {
        this.psId = psId;
        this.svcId = svcId;
        this.svcPrice = svcPrice;
        this.svcName = svcName;
        this.svcDescr = svcDescr;
        this.svcIsDeleted = svcIsDeleted;
    }

    public Integer getPsId() {
        return psId;
    }

    public void setPsId(Integer psId) {
        this.psId = psId;
    }

    public Integer getSvcId() {
        return svcId;
    }

    public void setSvcId(Integer svcId) {
        this.svcId = svcId;
    }



    public Integer getSvcPrice() {
        return svcPrice;
    }

    public void setSvcPrice(Integer svcPrice) {
        this.svcPrice = svcPrice;
    }

    public String getSvcName() {
        return svcName;
    }

    public void setSvcName(String svcName) {
        this.svcName = svcName;
    }

    public String getSvcDescr() {
        return svcDescr;
    }

    public void setSvcDescr(String svcDescr) {
        this.svcDescr = svcDescr;
    }

    public String getSvcIsDeleted() {
        return svcIsDeleted;
    }

    public void setSvcIsDeleted(String svcIsDeleted) {
        this.svcIsDeleted = svcIsDeleted;
    }

    @Override
    public String toString() {
        return "PgServiceDetailsDTO [psId=" + psId + ", svcId=" + svcId + ",  svcPrice="
                + svcPrice + ", svcName=" + svcName + ", svcDescr=" + svcDescr + "]";
    }

}