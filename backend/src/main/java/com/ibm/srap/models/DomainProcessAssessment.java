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

@Entity @Table(name="domain_process_assessments")
public class DomainProcessAssessment extends HistoricalEntity {
	
	@Id @GeneratedValue
	@Column(name="id")
	private Integer id;
	
	@OneToOne @JoinColumn(name="domain_report")
	private DomainReport report;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="process")
	private Process process;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="average")
	private Rating average;
	
	@Column(name="comment")
	private String comment;

	
	public Integer getId() { return id; }

	public DomainReport getReport() { return report; }
	public void setReport(DomainReport r) { report = r; }

	public Rating getAverage() { return average; }
	public void setAverage(Rating r) { average = r; }

	public String getComment() { return comment; }
	public void setComment(String c) { comment = c; }
	
	public Process getProcess() { return process; }
	public void setProcess(Process p) { process = p; }

}
