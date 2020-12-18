package com.scglab.connect.services.socket;

import java.io.Serializable;
import java.util.Map;

import com.scglab.connect.services.socket.SocketService.EventName;
import com.scglab.connect.services.socket.SocketService.Target;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class SocketData implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private EventName eventName;
	private String companyId;
	private String roomId;
	private Target target;
	private String sessionId;
	private Map<String, Object> data;
	
}
