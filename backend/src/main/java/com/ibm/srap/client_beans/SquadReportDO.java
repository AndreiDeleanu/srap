package com.ibm.srap.client_beans;

import static com.ibm.srap.services.utils.SrapUtils.emptyIfNull;
import static com.ibm.srap.services.utils.SrapUtils.zeroIfNull;

public class SquadReportDO {
	
	private Integer reportId;
	private Integer squadId;
	private String squadName;
	private String squadStatus;
	private String fp;
	private String fll;
	private String sll;
	private String step;
	private Double calculatedRating;
	private String fllRating;
	private HistoryDO history;
	private String savedFp;
	private String savedFll;
	private String savedSll;
	
	
	public String getSll( ) {
		return emptyIfNull(sll);
	}
	public void setSll(String sll) {
		this.sll = sll;
	}
	public Integer getSquadId() { return zeroIfNull(squadId); }
	public void setSquadId(Integer id) { squadId = id; }
	
	public String getSquadName() { return emptyIfNull(squadName); }
	public void setSquadName(String name) { squadName = name; }
	
	public String getSquadStatus() { return emptyIfNull(squadStatus); }
	public void setSquadStatus(String stat) { squadStatus = stat; }
	
	public String getFp() { return emptyIfNull(fp); }
	public void setFp(String mail) { fp = mail; }
	
	public String getFll() { return emptyIfNull(fll); }
	public void setFll(String mail) { fll = mail; }
	
	public String getStep() { return emptyIfNull(step); }
	public void setStep(String name) { step = name; }
	
	public Double getCalculatedRating() { return zeroIfNull(calculatedRating); }
	public void setCalculatedRating(Double value) { calculatedRating = value; }
	
	public String getFllRating() { return emptyIfNull(fllRating); }
	public void setFllRating(String rating) { fllRating = rating; }
	
	public Integer getReportId() { return zeroIfNull(reportId); }
	public void setReportId(Integer id) { reportId = id; }
	
	public HistoryDO getHistory() { 
		if (history == null) history = new HistoryDO(); 
		return history; 
	}

	public void setHistory(HistoryDO obj) {
		history = obj;
	}
	
	public String getSavedFp() { return emptyIfNull(savedFp); }
	public void setSavedFp(String name) { savedFp = name; }

	public String getSavedFll() { return emptyIfNull(savedFll); }
	public void setSavedFll(String name) { savedFll = name; }

	public String getSavedSll() { return emptyIfNull(savedSll); }
	public void setSavedSll(String name) { savedSll = name; }

	
	
}
