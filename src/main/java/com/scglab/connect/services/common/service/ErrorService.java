package com.scglab.connect.services.common.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ErrorService {

	@Autowired private MessageHandler messageHandler;
	
	public void throwParameterError() {
		String reason = this.messageHandler.getMessage("error.params.type0");
		throw new RuntimeException(reason);
	}
	
	public void throwParameterErrorWithNames(String arg) {
		String[] args = new String[1];
		args[0] = arg;
		throwParameterErrorWithNames(args);
	}
	
	public void throwParameterErrorWithNames(String[] args) {
		String reason = this.messageHandler.getMessage("error.params.type1", args);
		throw new RuntimeException(reason);
	}
	
	public void throwError(String reason) {
		throw new RuntimeException(reason);
	}
}
