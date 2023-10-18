package com.ibm.srap.client_beans;

import static com.ibm.srap.services.utils.SrapUtils.*;

public class SquadUpdateDO {
	
	private Integer id;
	private String name;
	private String fp;
	private String fll;
	private Integer subdomainId;
	private String status;
	
	
	public Integer getId() { return id; }
	public void setId(Integer i) { id = i; }
	
	public String getName() { return emptyIfNull(name); }
	public void setName(String n) { name = n; }
	
	public String getFp() { return emptyIfNull(fp); }
	public void setFp(String f) { fp = f; }
	
	public String getFll() { return emptyIfNull(fll); }
	public void setFll(String f) { fll = f; }
	
	public Integer getSubdomainId() { return subdomainId; }
	public void setSubdomainId(Integer s) { subdomainId = s; }
	
	public String getStatus() { return emptyIfNull(status); }
	public void setStatus(String s) { status = s; } 

}
