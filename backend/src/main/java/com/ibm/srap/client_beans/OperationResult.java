package com.ibm.srap.client_beans;

public class OperationResult {
	
	private boolean successful;
	private String message;
	private IdNameDO data;
	
	public OperationResult(boolean result, IdNameDO response) { this(result, null, response); }
	
	public OperationResult(boolean result, String msg) { this(result, msg, null); }
	
	public OperationResult(boolean result, String msg, IdNameDO jsonResponse) {
		successful = result;
		message = msg;
		data = jsonResponse;
	}

	
	public boolean isSuccessful() {	return successful; }
	public String getMessage() { return message; }
	public IdNameDO getData() { return data;	}
	
}
