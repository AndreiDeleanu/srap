package com.ibm.srap.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity @Table(name="processes")
public class Process extends HistoricalEntity {
	
	@Id @GeneratedValue
	@Column(name="id")
	private Integer id;
	
	@Column(name="name")
	private String name;
	
	@Column(name="risk_weight")
	private Integer riskWeight;
	
	@Column(name="description")
	private String description;
	
	@ManyToOne(fetch = FetchType.LAZY) 
	@JoinColumn(name="domain_id")
	private Domain domain;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="status_id")
	private Status status;

	
	public Integer getId() { return id; }

	public String getName() { return name; }
	public void setName(String n) { name = n; }

	public Integer getRiskWeight() { return riskWeight; }
	public void setRiskWeight(Integer w) { riskWeight = w; }
	
	public String getDescription() { return description; }
	public void setDescription(String d) { description = d;	}
	
	public Status getStatus() { return status; }
	public void setStatus(Status s) { status = s; }
	
	public Domain getDomain() { return domain; }
	public void setDomain(Domain d) { domain = d; }
	
}
