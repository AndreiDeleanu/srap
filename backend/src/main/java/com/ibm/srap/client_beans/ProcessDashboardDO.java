package com.ibm.srap.client_beans;

import static com.ibm.srap.services.utils.SrapUtils.*;

public class ProcessDashboardDO {

	private Integer id;
	private String processName;
	private String squadName;
	private String subdomainName;
	private String rating;
	private String comment;
	private String fp_email;
	private String sll_email;
	private String fll_email;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getProcessName() {
		return emptyIfNull(processName);
	}

	public void setProcessName(String processName) {
		this.processName = processName;
	}

	public String getSquadName() {
		return emptyIfNull(squadName);
	}

	public void setSquadName(String squadName) {
		this.squadName = squadName;
	}

	public String getSubdomainName() {
		return emptyIfNull(subdomainName);
	}

	public void setSubdomainName(String subdomainName) {
		this.subdomainName = subdomainName;
	}

	public String getRating() {
		return emptyIfNull(rating);
	}

	public void setRating(String rating) {
		this.rating = rating;
	}

	public String getComment() {
		return emptyIfNull(comment);
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getFp_email() {
		return fp_email;
	}

	public void setFp_email(String fp_email) {
		this.fp_email = fp_email;
	}

	public String getSll_email() {
		return sll_email;
	}

	public void setSll_email(String sll_email) {
		this.sll_email = sll_email;
	}

	public String getFll_email() {
		return fll_email;
	}

	public void setFll_email(String fll_email) {
		this.fll_email = fll_email;
	}

}
