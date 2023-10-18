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

@Entity @Table(name="domains") @Proxy(lazy=false)
public class Domain extends HistoricalEntity {
	
	@Id @GeneratedValue
	@Column(name="id")
	private Integer id;
	
	@Column(name="name")
	private String name;
	
	@Column(name="owner")
	private String owner;
	
	@Column(name="admin")
	private String admin;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="status")
	private Status status;
	
	@Column(name="help")
	private String help;
	
	
	public Integer getId() { return id; }

	public String getName() { return name; }
	public void setName(String n) { name = n; }
	
	public String getOwner() { return owner; }
	public void setOwner(String o) { owner = o; }
	
	public String getAdmin() { return admin; }
	public void setAdmin(String a) { admin = a; }
	
	public Status getStatus() {	return status; }
	public void setStatus(Status s) { status = s; }

	public String getHelp() { return help; }
	public void setHelp(String h) { help = h; }

}
