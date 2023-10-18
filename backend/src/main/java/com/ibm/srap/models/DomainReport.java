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

@Entity @Table(name="domain_reports")
public class DomainReport extends HistoricalEntity {
	
	@Id @GeneratedValue
	@Column(name="id")
	private Integer id;
	
	@Column(name="comment")
	private String doComment;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="do_assessment")
	private Rating doRating;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="fll_average")
	private Rating fllRating;
	
	@Column(name="average_value")
	private Double fllAverage;
	
	@OneToOne @JoinColumn(name="domain")
	private Domain domain;
	
	@Column(name="quarter")
	private String quarter;
	
	@Column(name="year")
	private String year;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="projection")
	private Rating projection;
	
	
	public Integer getId() { return id; }
	
	public String getDoComment() { return doComment; }
	public void setDoComment(String doc) { doComment = doc; }
	
	public Rating getFllRating() { return fllRating; }
	public void setFllRating(Rating r) { fllRating = r; }
	
	public Domain getDomain() { return domain; }
	public void setDomain(Domain d) { domain = d; }
	
	public Rating getDoRating() { return doRating; }
	public void setDoRating(Rating r) { doRating = r; }
	
	public String getQuarter() { return quarter; }
	public void setQuarter(String q) { quarter = q; }
	
	public String getYear() { return year; }
	public void setYear(String y) { year = y; }
	
	public Double getFllAverage() { return fllAverage; }
	public void setFllAverage(Double a) { fllAverage = a; }

	public Rating getProjection() { return projection; }
	public void setProjection(Rating p) { projection = p; }

}
