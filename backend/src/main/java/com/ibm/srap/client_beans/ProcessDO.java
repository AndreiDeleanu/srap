package com.ibm.srap.client_beans;

import static com.ibm.srap.services.utils.SrapUtils.*;

public class ProcessDO extends IdNameDO {
	
	private Integer weight;
	private String description;
	private String status;
	private Integer domainId;
	

	public Integer getWeight() { return weight; }
	public void setWeight(Integer w) { weight = w; }
	
	public String getDescription() { return emptyIfNull(description); }
	public void setDescription(String d) { description = d; }
	
	public String getStatus() { return emptyIfNull(status); }
	public void setStatus(String s) { status = s; }
	
	public Integer getDomainId() { return domainId;	}
	public void setDomainId(Integer d) { domainId = d; }

}
