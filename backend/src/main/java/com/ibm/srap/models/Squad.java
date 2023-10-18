package com.ibm.srap.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity @Table(name="squads")
public class Squad extends HistoricalEntity {
	
	@Id @GeneratedValue
	@Column(name="id")
	private Integer id;
	
	@Column(name="name")
	private String name;
	
	@Column(name="rating")
	private Integer rating;
	
	@Column(name="fll")
	private String fll;
	
	@Column(name="fp")
	private String fp;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="subdomain")
	private Subdomain subdomain;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="status")
	private Status status;

	
	public Integer getId() { return id; }

	public String getName() { return name; }
	public void setName(String n) { name = n; }

	public Integer getRating() { return rating; }
	public void setRating(Integer r) { rating = r; }
	
	public String getFll() { return fll; }
	public void setFll(String f) { fll = f; }
	
	public String getFp() { return fp; }
	public void setFp(String f) { fp = f; }

	public Subdomain getSubdomain() { return subdomain; }
	public void setSubdomain(Subdomain s) { subdomain = s; }
	
	public Status getStatus() { return status; }
	public void setStatus(Status s) { status = s; }
	
}
