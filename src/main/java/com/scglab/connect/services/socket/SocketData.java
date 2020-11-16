package com.scglab.connect.services.socket;

import com.scglab.connect.services.socket.SocketService.EventName;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class SocketData {
	
	private EventName eventName;
	private String companyId;
	private String roomId;
	private String token;
	private Object data;
	
}
