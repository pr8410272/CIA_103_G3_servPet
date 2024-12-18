package com.servPet.psSvc.model;

import com.servPet.ps.model.PsVO;
import com.servPet.psSvcItem.model.PsSvcItemVO;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@ToString

@NoArgsConstructor

@Embeddable
public class PsSvcId implements Serializable {

    // Getters, setters, equals, hashCode methods
    @ManyToOne
    @JoinColumn(name = "PS_ID")
    private PsVO psVO;

    @ManyToOne
    @JoinColumn(name = "SVC_ID")
    private PsSvcItemVO psSvcItemVO;

    public PsSvcId(PsVO psVO, PsSvcItemVO psSvcItemVO) {
        this.psVO = psVO;
        this.psSvcItemVO = psSvcItemVO;
    }

    public PsVO getPsVO() {
        return psVO;
    }

    public void setPsVO(PsVO psVO) {
        this.psVO = psVO;
    }

    public PsSvcItemVO getPsSvcItemVO() {
        return psSvcItemVO;
    }

    public void setPsSvcItemVO(PsSvcItemVO psSvcItemVO) {
        this.psSvcItemVO = psSvcItemVO;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PsSvcId psSvcId = (PsSvcId) o;
        return Objects.equals(psVO, psSvcId.psVO) &&
                Objects.equals(psSvcItemVO, psSvcId.psSvcItemVO);
    }

    @Override
    public int hashCode() {
        return Objects.hash(psVO, psSvcItemVO);
    }
}