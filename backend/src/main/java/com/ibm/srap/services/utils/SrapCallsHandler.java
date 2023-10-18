package com.ibm.srap.services.utils;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.ibm.srap.client_beans.DefaultResponse;
import com.ibm.srap.client_beans.OperationResult;

@Component
public class SrapCallsHandler {
	
	/**
	 * An automatic handler of SRAP update or delete client-server calls
	 * 
	 * @param operation The operation result object from a service call
	 * @return Custom response to the client, depending on the operation result
	 */
	public ResponseEntity<DefaultResponse> handleOperationResponse(OperationResult operation) {
		if (operation.isSuccessful()) {
			return ResponseEntity.ok(new DefaultResponse(operation.getMessage()));
		} else {
			return ResponseEntity.badRequest().body(new DefaultResponse(operation.getMessage()));
		}
	}

}
