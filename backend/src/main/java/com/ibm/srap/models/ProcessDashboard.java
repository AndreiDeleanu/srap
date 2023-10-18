package com.ibm.srap.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "process_dashboard")
public class ProcessDashboard {

	@Id
	@GeneratedValue
	@Column(name = "id")
	private Integer id;

	@Column(name = "domainId")
	private Integer domainId;

	@Column(name = "process_name")
	private String processName;

	@Column(name = "squad_name")
	private String squadName;

	@Column(name = "subdomain_name")
	private String subdomainName;

	@Column(name = "comment")
	private String comment;

	@Column(name = "rating")
	private Integer rating;

	@Column(name = "quarter")
	private String quarter;

	@Column(name = "year")
	private String year;

	@Column(name = "fp_email")
	private String fp_email;

	@Column(name = "sll_email")
	private String sll_email;

	@Column(name = "fll_email")
	private String fll_email;

	public Integer getId() {
		return id;
	}

	public Integer getDomainId() {
		return domainId;
	}

	public void setDomainId(Integer domainId) {
		this.domainId = domainId;
	}

	public String getProcessName() {
		return processName;
	}

	public void setProcessName(String processName) {
		this.processName = processName;
	}

	public String getSquadName() {
		return squadName;
	}

	public void setSquadName(String squadName) {
		this.squadName = squadName;
	}

	public String getSubdomainName() {
		return subdomainName;
	}

	public void setSubdomainName(String subdomainName) {
		this.subdomainName = subdomainName;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Integer getRating() {
		return rating;
	}

	public void setRating(Integer rating) {
		this.rating = rating;
	}

	public String getQuarter() {
		return quarter;
	}

	public void setQuarter(String quarter) {
		this.quarter = quarter;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
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
