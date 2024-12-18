package com.servPet.psSvcItem.model;

import com.servPet.psOrder.model.PsOrderVO;
import com.servPet.psSvc.model.PsSvcVO;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.io.Serial;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;


@NoArgsConstructor

@Entity
@Table(name = "PET_SITTER_SERVICE_ITEM")

public class PsSvcItemVO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SVC_ID", updatable = false)
    private Integer svcId;

    //一張訂單會有多個編號 服務項目編號多對一
//    @ManyToOne
//    @JoinColumn(name = "PSO_ID", nullable = false) // 服務項目對應的訂單 ID，設為非空
//    private PsOrderVO psOrderVO;



    //一個保母會有多個服務項目


    @Column(name = "SVC_DESCR")
    private String svcDescr;

    @NotEmpty(message = "服務名稱請勿空白")
    @Column(name = "SVC_NAME")
    private String svcName;

    public PsSvcItemVO(Integer svcId, String svcDescr, String svcName) {
        this.svcId = svcId;
        this.svcDescr = svcDescr;
        this.svcName = svcName;
    }

    public Integer getSvcId() {
        return svcId;
    }

    public void setSvcId(Integer svcId) {
        this.svcId = svcId;
    }

    public String getSvcDescr() {
        return svcDescr;
    }

    public void setSvcDescr(String svcDescr) {
        this.svcDescr = svcDescr;
    }

    public String getSvcName() {
        return svcName;
    }

    public void setSvcName(String svcName) {
        this.svcName = svcName;
    }
}
