package com.ibm.srap.client_beans;

import static com.ibm.srap.services.utils.SrapUtils.*;

import java.util.ArrayList;
import java.util.List;

public class EditSquadReportDO {
	
	private Integer squadId;
	private Integer fllRating;
	private Double calculatedRating;
	private String fllComment;
	private String quarter;
	private String status;
	private Integer reportId;
	private String squadName;
	private String fpName;
	private String fllName;
	private List<ProcessRatingDO> processes;
	private HistoryDO history;
	
	
	public Integer getSquadId() { return zeroIfNull(squadId); }
	public void setSquadId(Integer id) { this.squadId = id; }
	
	public Integer getFllRating() { return fllRating; }
	public void setFllRating(Integer fllRating) { this.fllRating = fllRating; }
	
	public Double getCalculatedRating() { return zeroIfNull(calculatedRating); }
	public void setCalculatedRating(Double calculatedRating) { this.calculatedRating = calculatedRating; } 
	
	public String getFllComment() { return emptyIfNull(fllComment); }
	public void setFllComment(String fllComment) { this.fllComment = fllComment; }
	
	public String getQuarter() { return emptyIfNull(quarter); }
	public void setQuarter(String quarter) { this.quarter = quarter; }
	
	public List<ProcessRatingDO> getProcesses() { 
		if (processes == null) processes = new ArrayList<>();
		return processes;
	}
	public void setProcesses(List<ProcessRatingDO> processes) { this.processes = processes;	}
	
	public String getStatus() { return emptyIfNull(status); }
	public void setStatus(String status) { this.status = status; }
	
	public Integer getReportId() { return zeroIfNull(reportId); }
	public void setReportId(Integer reportId) {	this.reportId = reportId; }
	
	public String getSquadName() { return emptyIfNull(squadName); }
	public void setSquadName(String squadName) { this.squadName = squadName; }
	
	public String getFpName() { return emptyIfNull(fpName); }
	public void setFpName(String fpName) { this.fpName = fpName; }
	
	public String getFllName() { return emptyIfNull(fllName); }
	public void setFllName(String fllName) { this.fllName = fllName; }
	
	public HistoryDO getHistory() { 
		if (history == null) history = new HistoryDO(); 
		return history; 
	}
	public void setHistory(HistoryDO obj) {	history = obj; }
	
}
