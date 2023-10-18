package com.ibm.srap.client_beans;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class DefaultResponse {
	
	private String message;
	private IdNameDO data;
	
	
	public DefaultResponse() {}
	
	public DefaultResponse(String messageToSet) { message = messageToSet; }
	
	public DefaultResponse(IdNameDO dataToRetrieve) { data = dataToRetrieve; }

	
	public String getMessage() { return message; }
	public void setMessage(String m) { message = m; }

	public IdNameDO getData() { return data; }
	public void setData(IdNameDO d) { data = d; }
	
	
	@Override
	public String toString() {
		return "Response [message=" + message + "]";
	}



}
