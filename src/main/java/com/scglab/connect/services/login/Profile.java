package com.scglab.connect.services.login;

import java.io.Serializable;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class Profile implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private int id;
	private String companyId;
	private String roomId;
	private int isAdmin;
	private int isMember;
	private int isCustomer;
	private String loginName;
	private long speakerId;
	private String name;
	private String sessionId;
	private boolean authenticated;
	private Map<String, Object> companyUseConfigJson;
	private int noReadCount;
	private int	endDays;
	private int state;
	private int roomState;
	private String endDate;
}
