package com.ibm.srap.client_beans;

import static com.ibm.srap.services.utils.SrapUtils.*;

public class SquadDO extends IdNameDO {
	
	private String fll;
	private String focalPoint;
	private String domainOwner;
	private Integer rating;
	private SubdomainDO subdomain;
	private String status;
	
	
	public String getFll() { return emptyIfNull(fll); }
	public void setFll(String f) { fll = f; }
	
	public String getFocalPoint() { return emptyIfNull(focalPoint); }
	public void setFocalPoint(String fp) { focalPoint = fp; }
	
	public String getDomainOwner() { return emptyIfNull(domainOwner); }
	public void setDomainOwner(String d) { domainOwner = d; }
	
	public Integer getRating() { return rating;	}
	public void setRating(Integer r) { rating = r; }
	
	public SubdomainDO getSubdomain() { return subdomain; }
	public void setSubdomain(SubdomainDO s) { subdomain = s; }
	
	public String getStatus() { return emptyIfNull(status); }
	public void setStatus(String s) { status = s; } 
	
}
