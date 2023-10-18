package com.ibm.srap.client_beans;

import java.util.ArrayList;
import java.util.List;

public class SLLReportsTaskInfoDO {
	
	private List<Integer> subdomainReportsToSubmit;
	private List<String> sllsToNotify;
	
	
	public List<Integer> getSubdomainReportsToSubmit() { 
		if (subdomainReportsToSubmit == null) subdomainReportsToSubmit = new ArrayList<>();
		return subdomainReportsToSubmit; 
	}
	public void setSubdomainReportsToSubmit(List<Integer> rep) { subdomainReportsToSubmit = rep;	}
	
	public List<String> getSllsToNotify() { 
		if (sllsToNotify == null) sllsToNotify = new ArrayList<>();
		return sllsToNotify; 
	}
	public void setSllsToNotify(List<String> n) { sllsToNotify = n;}

}
