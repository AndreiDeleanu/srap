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

@Entity @Table(name="process_assessments")
public class ProcessAssessment extends HistoricalEntity {
	
	@Id @GeneratedValue
	@Column(name="id")
	private Integer id;
	
	@OneToOne @JoinColumn(name="report")
	private SquadReport report;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="process")
	private Process process;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="rating")
	private Rating rating;
	
	@Column(name="weight")
	private Integer weight;
	
	@Column(name="comment")
	private String comment;

	
	public Integer getId() { return id; }

	public SquadReport getReport() {	return report; }
	public void setReport(SquadReport report) { this.report = report; }

	public Process getProcess() { return process; }
	public void setProcess(Process process) { this.process = process; }

	public Rating getRating() { return rating; }
	public void setRating(Rating rating) { this.rating = rating; }

	public Integer getWeight() { return weight; }
	public void setWeight(Integer weight) { this.weight = weight; }
	
	public String getComment() { return comment; }
	public void setComment(String comment) { this.comment = comment; }
	
}
