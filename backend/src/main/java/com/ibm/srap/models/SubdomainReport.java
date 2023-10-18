
package com.ibm.srap.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "subdomain_reports")
public class SubdomainReport extends HistoricalEntity {

	@Id
	@GeneratedValue
	@Column(name = "id")
	private Integer id;

	@Column(name = "comment")
	private String sllComment;

	@OneToOne
	@JoinColumn(name = "subdomain")
	private Subdomain subdomain;

	@Column(name = "fll_average_value")
	private Double fllAverage;

	@Column(name = "calculated_value")
	private Double calculatedValue;

	@Column(name = "quarter")
	private String quarter;

	@Column(name = "year")
	private String year;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "sll_override")
	private Rating sllOverride;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "status")
	private Status status;

	@Column(name = "current_sll")
	private String currentSll;

	public Integer getId() {
		return id;
	}

	public String getSllComment() {
		return sllComment;
	}

	public void setSllComment(String s) {
		sllComment = s;
	}

	public Subdomain getSubdomain() {
		return subdomain;
	}

	public void setSubdomain(Subdomain s) {
		subdomain = s;
	}

	public Double getFllAverage() {
		return fllAverage;
	}

	public void setFllAverage(Double d) {
		fllAverage = d;
	}

	public String getQuarter() {
		return quarter;
	}

	public void setQuarter(String q) {
		quarter = q;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String y) {
		year = y;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status s) {
		status = s;
	}

	public Double getCalculatedValue() {
		return calculatedValue;
	}

	public void setCalculatedValue(Double c) {
		calculatedValue = c;
	}

	public Rating getSllOverride() {
		return sllOverride;
	}

	public void setSllOverride(Rating o) {
		sllOverride = o;
	}

	public String getCurrentSll() {
		return currentSll;
	}

	public void setCurrentSll(String currentSll) {
		this.currentSll = currentSll;
	}

}