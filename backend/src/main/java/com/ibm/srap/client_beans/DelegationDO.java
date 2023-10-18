package com.ibm.srap.client_beans;

import static com.ibm.srap.services.utils.SrapUtils.*;

public class DelegationDO extends IdNameDO {
	
	private String delegator;
	private String delegate;
	private String role;
	
	
	public String getDelegator() { return emptyIfNull(delegator); }
	public void setDelegator(String d) { delegator = d; }
	
	public String getDelegate() { return emptyIfNull(delegate);	}
	public void setDelegate(String d) { delegate = d; }
	
	public String getRole() { return emptyIfNull(role);	}
	public void setRole(String r) { role = r; }

}
