package com.ibm.srap.models;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity @Table(name="deadlines")
public class Deadline extends HistoricalEntity {
	
	@Id @GeneratedValue
	@Column(name="id")
	private Integer id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="domain_id")
	private Domain domain;
	
	@Column(name="quarter")
	private String quarter;
	
	@Column(name="fp_submit_deadline")
	private Timestamp fpSubmitDeadline;
	
	@Column(name="fll_signoff_deadline")
	private Timestamp fllSignoffDeadline;
	
	@Column(name="sll_rollup_deadline")
	private Timestamp sllRollupDeadline;

	
	public Integer getId() { return id; }

	public Domain getDomain() { return domain; }
	public void setDomain(Domain d) { domain = d; }

	public String getQuarter() { return quarter; }
	public void setQuarter(String q) { quarter = q; }

	public Timestamp getFpSubmitDeadline() { return fpSubmitDeadline; }
	public void setFpSubmitDeadline(Timestamp fp) { fpSubmitDeadline = fp; }

	public Timestamp getFllSignoffDeadline() { return fllSignoffDeadline; }
	public void setFllSignoffDeadline(Timestamp fll) { fllSignoffDeadline = fll; }

	public Timestamp getSllRollupDeadline() { return sllRollupDeadline; }
	public void setSllRollupDeadline(Timestamp sll) { sllRollupDeadline = sll; }
	

}
