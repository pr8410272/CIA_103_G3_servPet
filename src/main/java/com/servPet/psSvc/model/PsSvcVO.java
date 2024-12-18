
package com.servPet.psSvc.model;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@ToString

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "PET_SITTER_SERVICE")
public class PsSvcVO implements Serializable {


    @EmbeddedId
    private PsSvcId psSvcId;

    @Column(name = "SVC_PRICE")
    private Integer svcPrice;

    //保母的服務項目 一個保母有多個服務項目

//    @OneToMany(mappedBy = "id.psSvcItemVO")
//    private Set<PsOddVO> psOddVO = new HashSet<>();

    public PsSvcId getPsSvcId() {
        return psSvcId;
    }

    public void setPsSvcId(PsSvcId psSvcId) {
        this.psSvcId = psSvcId;
    }

    public Integer getSvcPrice() {
        return svcPrice;
    }

    public void setSvcPrice(Integer svcPrice) {
        this.svcPrice = svcPrice;
    }


    // equals and hashCode methods

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PsSvcVO psSvcVO = (PsSvcVO) o;
        return Objects.equals(psSvcId, psSvcVO.psSvcId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(psSvcId);
    }
}
