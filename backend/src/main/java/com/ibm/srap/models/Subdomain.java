package com.ibm.srap.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Proxy;

@Entity @Table(name="subdomains") @Proxy(lazy=false)
public class Subdomain extends HistoricalEntity {
	
	@Id @GeneratedValue
	@Column(name="id")
	private Integer id;
	
	@Column(name="name")
	private String name;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="domain")
	private Domain domain;
	
	@Column(name="sll")
	private String sll;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="status")
	private Status status;
	
	
	public Integer getId() { return id; }

	public String getName() { return name; }
	public void setName(String n) { name = n; }
	
	public Domain getDomain() {	return domain; }
	public void setDomain(Domain d) { domain = d; }

	public String getSll() { return sll; }
	public void setSll(String s) { sll = s; }

	public Status getStatus() { return status; }
	public void setStatus(Status s) { status = s; }

}
