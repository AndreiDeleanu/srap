package com.ibm.srap.client_beans;

import static com.ibm.srap.services.utils.SrapUtils.*;

public class ProcessAssessmentDO extends IdNameDO {
	
	private String average;
	private String daComment;
	
	
	public String getAverage() { return emptyIfNull(average); }
	public void setAverage(String avg) { average = avg; }
	
	public String getDaComment() { return emptyIfNull(daComment); }
	public void setDaComment(String cmt) { daComment = cmt; }

}
