package com.servPet.fnc.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Builder;

/**
 * FncVO class corresponding to the FNC table in the database.
 */
@Entity  // Marks the class as a JPA entity
@Table(name = "FNC")  // Specifies the table name in the database
@Builder
public class FncVO implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @Id
    @Column(name = "FNC_ID")  // Corresponds to the FNC_ID column in the table
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // Automatically generates IDs
    private Integer fncId;  // Function ID, primary key
    
    @Column(name = "FNC_NAME")
    @NotEmpty(message = "請輸入功能名稱")  // Ensures the function name is not empty
    @Size(min = 2, max = 20, message = "功能名稱長度需在{min}到{max}之間")  // Validates the length of the function name
    private String fncName;  // Function name
    
    @Column(name = "FNC_DES")
    @NotEmpty(message = "請輸入功能描述")  // Ensures the function description is not empty
    @Size(min = 2, max = 255, message = "功能描述長度需在{min}到{max}之間")  // Validates the length of the function description
    private String fncDes;  // Function description
    
    @Column(name = "FNC_CRE_AT")
    @NotNull(message = "建立日期不能為空")  // Ensures creation date is not null
    private LocalDateTime fncCreAt;  // Creation date
    
    @Column(name = "FNC_UPD_AT")
    @NotNull(message = "更新日期不能為空")  // Ensures update date is not null
    private LocalDateTime fncUpdAt;  // Update date
    

	public Integer getFncId() {
		return fncId;
	}

	public void setFncId(Integer fncId) {
		this.fncId = fncId;
	}

	public String getFncName() {
		return fncName;
	}

	public void setFncName(String fncName) {
		this.fncName = fncName;
	}

	public String getFncDes() {
		return fncDes;
	}

	public void setFncDes(String fncDes) {
		this.fncDes = fncDes;
	}

	public LocalDateTime getFncCreAt() {
		return fncCreAt;
	}

	public void setFncCreAt(LocalDateTime fncCreAt) {
		this.fncCreAt = fncCreAt;
	}

	public LocalDateTime getFncUpdAt() {
		return fncUpdAt;
	}

	public void setFncUpdAt(LocalDateTime fncUpdAt) {
		this.fncUpdAt = fncUpdAt;
	}

	@Override
	public String toString() {
		return "FncVO [fncId=" + fncId + ", fncName=" + fncName + ", fncDes=" + fncDes + ", fncCreAt=" + fncCreAt
				+ ", fncUpdAt=" + fncUpdAt + ", getFncId()=" + getFncId() + ", getFncName()=" + getFncName()
				+ ", getFncDes()=" + getFncDes() + ", getFncCreAt()=" + getFncCreAt() + ", getFncUpdAt()="
				+ getFncUpdAt() + ", getClass()=" + getClass() + ", hashCode()=" + hashCode() + ", toString()="
				+ super.toString() + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(fncCreAt, fncDes, fncId, fncName, fncUpdAt);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FncVO other = (FncVO) obj;
		return Objects.equals(fncCreAt, other.fncCreAt) && Objects.equals(fncDes, other.fncDes)
				&& Objects.equals(fncId, other.fncId) && Objects.equals(fncName, other.fncName)
				&& Objects.equals(fncUpdAt, other.fncUpdAt);
	}

	public FncVO(Integer fncId,
			@NotEmpty(message = "請輸入功能名稱") @Size(min = 2, max = 20, message = "功能名稱長度需在{min}到{max}之間") String fncName,
			@NotEmpty(message = "請輸入功能描述") @Size(min = 2, max = 255, message = "功能描述長度需在{min}到{max}之間") String fncDes,
			@NotNull(message = "建立日期不能為空") LocalDateTime fncCreAt,
			@NotNull(message = "更新日期不能為空") LocalDateTime fncUpdAt) {
		super();
		this.fncId = fncId;
		this.fncName = fncName;
		this.fncDes = fncDes;
		this.fncCreAt = fncCreAt;
		this.fncUpdAt = fncUpdAt;
	}

	public FncVO() {
		super();
		// TODO Auto-generated constructor stub
	}
    
    
}