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

@Entity @Table(name="subdomain_process_assessments")
public class SubdomainProcessAssessment extends HistoricalEntity {

	@Id @GeneratedValue
	@Column(name="id")
	private Integer id;
	
	@OneToOne @JoinColumn(name="subdomain_report")
	private SubdomainReport report;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="process")
	private Process process;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="average")
	private Rating average;
	
	
	public Integer getId() { return id; }

	public SubdomainReport getReport() { return report; }
	public void setReport(SubdomainReport r) { report = r; }

	public Process getProcess() { return process; }
	public void setProcess(Process p) { process = p; }
	
	public Rating getAverage() { return average; }
	public void setAverage(Rating r) { average = r; }

}
