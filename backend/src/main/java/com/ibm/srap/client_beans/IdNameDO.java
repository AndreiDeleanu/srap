package com.ibm.srap.client_beans;

import static com.ibm.srap.services.utils.SrapUtils.emptyIfNull;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_EMPTY)
public class IdNameDO {
	
	private Integer id;
	private String name;
	
	
	public Integer getId() { return id; }
	public void setId(Integer i) { id = i; }
	
	public String getName() { return emptyIfNull(name); }
	public void setName(String n) { name = n; }

}
