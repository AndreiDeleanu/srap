package com.ibm.srap.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity @Table(name="delegations")
public class Delegation extends HistoricalEntity {
	
	@Id @GeneratedValue
	@Column(name="id")
	private Integer id;
	
	@Column(name="delegator")
	private String delegator;
	
	@Column(name="delegate")
	private String delegate;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="user_role_id")
	private UserRole role;
	
	
	public Integer getId() { return id; }

	public String getDelegator() { return delegator; }
	public void setDelegator(String d) { delegator = d; }

	public String getDelegate() { return delegate; }
	public void setDelegate(String d) { delegate = d; }

	public UserRole getUserRole() { return role; }
	public void setUserRole(UserRole r) { role = r; }

}
