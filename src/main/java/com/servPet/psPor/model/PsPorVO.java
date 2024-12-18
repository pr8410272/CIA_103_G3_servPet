package com.servPet.psPor.model;


import javax.persistence.*;

import com.servPet.ps.model.PsVO;
import lombok.*;

//import com.pet_groomer.model.PetGroomerVO;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "PET_SITTER_PORTFOLIO")
public class PsPorVO implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    @Id // 主鍵
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 使用自增的方式來生成主鍵
    @Column(name = "PIC_ID")
    private Integer picId;


//    @ManyToOne
//    @JoinColumn(name = "PS_ID") // 外來鍵
//    private PsVO psVO;
private Integer psId;
    
    @Lob
    @Column(name = "PS_SVC_PIC")
    private byte[] psSvcPic;

// public void setPgSvcPic(byte[] buf) {
//
// }

}