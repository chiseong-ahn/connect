package com.scglab.connect.services.external;

import com.scglab.connect.services.common.auth.Contract;
import com.scglab.connect.services.common.auth.User;

public class ExternalInc implements External {

	@Override
	public int isWorking() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public User login(String id, String passwd) {
		User user = new User();
		user.setCid(2);
		user.setEmpno(id);
		return user;
	}

	@Override
	public User getMemberInfo(int userno) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Contract getMemberContractDetail(int contractNo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int sendMinwon() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getHoliday() {
		// TODO Auto-generated method stub
		return 0;
	}


}
