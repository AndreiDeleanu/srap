package com.ibm.srap.client_beans;

import static com.ibm.srap.services.utils.SrapUtils.*;

public class DomainDO extends IdNameDO {

	private String owner;
	private String status;
	private String admin;
	private String help;

	
	public String getOwner() { return emptyIfNull(owner); }
	public void setOwner(String o) { owner = o; }
	
	public String getStatus() {	return emptyIfNull(status); }
	public void setStatus(String s) { status = s; }
	
	public String getAdmin() { return emptyIfNull(admin); }
	public void setAdmin(String a) { admin = a; }
	
	public String getHelp() { return emptyIfNull(help); }
	public void setHelp(String h) { help = h; }
	
}
