package com.ibm.srap.client_beans;

import static com.ibm.srap.services.utils.SrapUtils.*;

import java.sql.Timestamp;

public class HistoryDO {

	private Timestamp createdTime;
	private Timestamp lastModifiedTime;
	private String createdBy;
	private String lastModifiedBy;
	
	
	public Timestamp getCreatedTime() { return zeroIfNull(createdTime); }
	public void setCreatedTime(Timestamp val) { createdTime = val; }
	
	public Timestamp getLastModifiedTime() { return zeroIfNull(lastModifiedTime); }
	public void setLastModifiedTime(Timestamp val) { lastModifiedTime = val; }
	
	public String getCreatedBy() { return emptyIfNull(createdBy); }
	public void setCreatedBy(String name) { createdBy = name; }
	
	public String getLastModifiedBy() { return emptyIfNull(lastModifiedBy); }
	public void setLastModifiedBy(String name) { lastModifiedBy = name; }
	
}
