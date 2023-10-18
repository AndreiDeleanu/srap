package com.ibm.srap.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity @Table(name="user_roles")
public class UserRole extends HistoricalEntity {
	
	@Id @GeneratedValue
	@Column(name="id")
	private Integer id;
	
	@Column(name="user")
	private String userMail;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="role_id")
	private Role role;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="domain_id")
	private Domain domain;
	
	
	public Integer getId() { return id; }

	public Role getRole() { return role; }
	public void setRole(Role r) { role = r; }
	
	public String getUserMail() { return userMail; }
	public void setUserMail(String m) { userMail = m; }
	
	public Domain getDomain() { return domain; }
	public void setDomain(Domain d) { domain = d; }
	
}
