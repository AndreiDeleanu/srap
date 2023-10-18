package com.ibm.srap.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Proxy;

@Entity @Table(name="ratings") @Proxy(lazy=false)
public class Rating extends HistoricalEntity {
	
	@Id @GeneratedValue
	@Column(name="id")
	private Integer id;
	
	@Column(name="name")
	private String name;
	
	@Column(name="value")
	private Integer value;
	
	
	public Integer getId() { return id; }

	public String getName() { return name; }
	public void setName(String n) { name = n; }
	
	public Integer getValue() { return value; }
	public void setValue(Integer value) { this.value = value; }

}
