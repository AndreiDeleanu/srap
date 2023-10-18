package com.ibm.srap.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "threshold")
public class Threshold extends HistoricalEntity {

	@Id
	@GeneratedValue
	@Column(name = "id")
	private Integer id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "domain_id")
	private Domain domain;

	@Column(name = "quarter")
	private String quarter;

	@Column(name = "year")
	private String year;

	@Column(name = "sat_threshold")
	private Integer satThreshold;

	@Column(name = "marginal_threshold")
	private Integer marginalThreshold;

	public Integer getId() {
		return id;
	}

	public Domain getDomain() {
		return domain;
	}

	public void setDomain(Domain domain) {
		this.domain = domain;
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

	public Integer getSatThreshold() {
		return satThreshold;
	}

	public void setSatThreshold(Integer satThreshold) {
		this.satThreshold = satThreshold;
	}

	public Integer getMarginalThreshold() {
		return marginalThreshold;
	}

	public void setMarginalThreshold(Integer marginalThreshold) {
		this.marginalThreshold = marginalThreshold;
	}

}
