package com.ibm.srap.client_beans;

import static com.ibm.srap.services.utils.SrapUtils.emptyIfNull;

import java.sql.Timestamp;

import io.swagger.annotations.ApiModelProperty;

public class DeadlineDO {
	
	@ApiModelProperty(notes = "The database generated deadline ID")
	private Integer id;
	@ApiModelProperty(notes = "The domain ID for which deadlines are assigned")
	private Integer domainId;
	@ApiModelProperty(notes = "The quarter formatted as #QYYYY")
	private String quarter;
	@ApiModelProperty(notes = "The FP submission deadline, as Timestamp in milliseconds")
	private Timestamp fpSubmitDeadline;
	@ApiModelProperty(notes = "The FLL signoff deadline, as Timestamp in milliseconds")
	private Timestamp fllSignoffDeadline;
	@ApiModelProperty(notes = "The SLL rollup deadline, as Timestamp in milliseconds")
	private Timestamp sllRollupDeadline;
	
	
	public Integer getId() { return id; }
	public void setId(Integer i) { id = i; }
	
	public Integer getDomainId() { return domainId; }
	public void setDomainId(Integer d) { domainId = d; }
	
	public String getQuarter() { return emptyIfNull(quarter); }
	public void setQuarter(String q) { quarter = q; }
	
	public Timestamp getFpSubmitDeadline() { return fpSubmitDeadline; }
	public void setFpSubmitDeadline(Timestamp fp) { fpSubmitDeadline = fp; }
	
	public Timestamp getFllSignoffDeadline() { return fllSignoffDeadline; }
	public void setFllSignoffDeadline(Timestamp fll) { fllSignoffDeadline = fll; }
	
	public Timestamp getSllRollupDeadline() { return sllRollupDeadline; }
	public void setSllRollupDeadline(Timestamp sll) { sllRollupDeadline = sll; }
	
	

}
