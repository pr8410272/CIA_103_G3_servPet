package com.servPet.event.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "event_ann")
public class EventVO {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "INF_ID")
    private Integer infId;
	
	@Column(name = "TITLE")
    private String title;
	
	@Column(name = "CONTENT")
    private String content;

    @Column(name = "CREATED", updatable = false)
    private Timestamp created;

    @Column(name = "UPDATED")
    private Timestamp updated;

    @Column(name = "INF_TYPE")
    private Integer infType;

   

    public EventVO() {
		super();
    }

    

	public EventVO(Integer infId, String title, String content, Timestamp created, Timestamp updated, Integer infType) {
		super();
		this.infId = infId;
		this.title = title;
		this.content = content;
		this.created = created;
		this.updated = updated;
		this.infType = infType;
	}

	
	
	public Integer getInfId() {
		return infId;
	}



	public void setInfId(Integer infId) {
		this.infId = infId;
	}



	public String getTitle() {
		return title;
	}



	public void setTitle(String title) {
		this.title = title;
	}



	public String getContent() {
		return content;
	}



	public void setContent(String content) {
		this.content = content;
	}



	public Timestamp getCreated() {
		return created;
	}



	public void setCreated(Timestamp created) {
		this.created = created;
	}



	public Timestamp getUpdated() {
		return updated;
	}



	public void setUpdated(Timestamp updated) {
		this.updated = updated;
	}



	public Integer getInfType() {
		return infType;
	}



	public void setInfType(Integer infType) {
		this.infType = infType;
	}



	@Override
    public String toString() {
        return "Event [infid=" + infId + ", title=" + title + ", content=" + content + ", created=" + created
                + ", updated=" + updated + ", inftype=" + infType + "]";
    }
}
