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
@Table(name = "reports")
public class SquadReport extends HistoricalEntity {

	@Id
	@GeneratedValue
	@Column(name = "id")
	private Integer id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "status")
	private Status status;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "fll_rating")
	private Rating fllRating;

	@Column(name = "calculated_rating")
	private Double calculatedRating;

	@OneToOne
	@JoinColumn(name = "squad")
	private Squad squad;

	@Column(name = "comment")
	private String comment;

	@Column(name = "quarter")
	private String quarter;

	@Column(name = "year")
	private String year;

	@Column(name = "current_fp")
	private String currentFp;

	@Column(name = "current_fll")
	private String currentFll;

	@Column(name = "current_sll")
	private String currentSll;

	public Integer getId() {
		return id;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Rating getFllRating() {
		return fllRating;
	}

	public void setFllRating(Rating fllRating) {
		this.fllRating = fllRating;
	}

	public Double getCalculatedRating() {
		return calculatedRating;
	}

	public void setCalculatedRating(Double calculatedRating) {
		this.calculatedRating = calculatedRating;
	}

	public Squad getSquad() {
		return squad;
	}

	public void setSquad(Squad squad) {
		this.squad = squad;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
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

	public String getCurrentFp() {
		return currentFp;
	}

	public void setCurrentFp(String currentFp) {
		this.currentFp = currentFp;
	}

	public String getCurrentFll() {
		return currentFll;
	}

	public void setCurrentFll(String currentFll) {
		this.currentFll = currentFll;
	}

	public String getCurrentSll() {
		return currentSll;
	}

	public void setCurrentSll(String currentSll) {
		this.currentSll = currentSll;
	}

}
