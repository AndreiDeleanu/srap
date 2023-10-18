package com.ibm.srap.client_beans;

import java.util.ArrayList;
import java.util.List;

public class FllReportsTaskInfoDO {
	
	private List<Integer> squadReportsToSign;
	private List<String> fllsToNotify;
	
	
	public List<Integer> getSquadReportsToSign() { 
		if (squadReportsToSign == null) squadReportsToSign = new ArrayList<>();
		return squadReportsToSign;
	}
	public void setSquadReportsToSign(List<Integer> rep) { squadReportsToSign = rep; }
	
	public List<String> getFllsToNotify() {
		if (fllsToNotify == null) fllsToNotify = new ArrayList<>();
		return fllsToNotify;
	}
	public void setFllsToNotify(List<String> n) { fllsToNotify = n; }

}
