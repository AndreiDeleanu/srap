package com.ibm.srap.client_beans;

import java.util.ArrayList;
import java.util.List;

public class FpReportsTaskInfoDO {
	
	private List<Integer> squadReportsToSubmit;
	private List<String> fpsToNotify;


	public List<Integer> getSquadReportsToSubmit() { 
		if (squadReportsToSubmit == null) squadReportsToSubmit = new ArrayList<>();
		return squadReportsToSubmit; 
	}
	public void setSquadReportsToSubmit(List<Integer> rep) { squadReportsToSubmit = rep; }
	
	public List<String> getFpsToNotify() { 
		if (fpsToNotify == null) fpsToNotify = new ArrayList<>();
		return fpsToNotify;
	}
	public void setFpsToNotify(List<String> n) { fpsToNotify = n; }

}
