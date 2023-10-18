package com.ibm.srap.client_beans;

import static com.ibm.srap.services.utils.SrapUtils.*;

public class ProcessRatingDO {

	private Integer processId;
	private String processName;
	private Integer rating;
	private String comment;
	private String description;
	
	
	public Integer getProcessId() { return zeroIfNull(processId); }
	public void setProcessId(Integer id) { processId = id; }
	
	public Integer getRating() { return rating; }
	public void setRating(Integer r) { rating = r; }
	
	public String getComment() { return emptyIfNull(comment); }
	public void setComment(String c) { comment = c; }
	
	public String getProcessName() { return emptyIfNull(processName); }
	public void setProcessName(String n) { processName = n; }
	
	public String getDescription() { return emptyIfNull(description); }
	public void setDescription(String d) { description = d; }
	
}
