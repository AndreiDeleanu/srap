package com.ibm.srap.models;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import com.ibm.srap.services.utils.Messages;

@MappedSuperclass
public class HistoricalEntity {
	
	@Column(name="UPDATED_TIME")
	private Timestamp updatedTime;
	
	@Column(name="CREATED_TIME")
	private Timestamp createdTime;
	
	@Column(name="CREATED_BY")
	private String createdBy;
	
	@Column(name="LAST_MODIFIED_BY")
	private String updatedBy;

	
	public Timestamp getUpdatedTime() { return updatedTime; }
	public void setUpdatedTime(Timestamp val) { updatedTime = val; }
	
	public Timestamp getCreatedTime() { return createdTime; }
	public void setCreatedTime(Timestamp val) { createdTime = val; }
	
	public String getCreatedBy() { return createdBy; }
	public void setCreatedBy(String name) { createdBy = name; }

	public String getUpdatedBy() { return updatedBy; }
	public void setUpdatedBy(String name) { updatedBy = name; }
	
	
	@PreUpdate
	private void saveUpdateTime() {
		setUpdatedTime(new Timestamp(System.currentTimeMillis()));
		if (getUpdatedBy() == null) setUpdatedBy(Messages.SYSTEM);
	}
	
	@PrePersist
	private void saveCreatedTime() {
		long now = System.currentTimeMillis();
		setUpdatedTime(new Timestamp(now));
		setCreatedTime(new Timestamp(now));
		if (getCreatedBy() == null) setCreatedBy(Messages.SYSTEM);
		if (getUpdatedBy() == null) setUpdatedBy(Messages.SYSTEM);
	}

}
