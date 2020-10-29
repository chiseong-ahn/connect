package com.scglab.connect.services.external;

import java.util.Calendar;

import com.scglab.connect.constant.Constant;
import com.scglab.connect.services.chat.ChatRoomRepository;
import com.scglab.connect.services.common.auth.Contract;
import com.scglab.connect.services.common.auth.User;

import lombok.RequiredArgsConstructor;

public class ExternalScg implements External {
	
	@Override
	public int isWorking() {
		int iswork = 1;
		
		Calendar cal = Calendar.getInstance();
		
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH) + 1;
		int day = cal.get(Calendar.DAY_OF_MONTH);
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		int min = cal.get(Calendar.MINUTE);
		int sec = cal.get(Calendar.SECOND);
		System.out.println("현재 시각은 " + year + "년도 " + month + "월 " + day + "일 " + hour + "시 " + min + "분 " + sec + "초입니다.");
		
		if(hour < 9 || hour > 17 || (hour == 17 && min > 30)) {
			// 근무 외 시간.(9시 이전, 17시 30분 이후)
			iswork = 2;
		
		}else{
			if(false) {	// 휴일 또는 근무 외 시 (MIS를 통해 확인)	
				iswork = 2;
				
			}else {		// 근무일자일 경우
				if(hour == 12) {
					// 점심시간.
					iswork = 3;
				}else {
					
				}
			}
		}
		
		return iswork;
	}

	@Override
	public User login(String id, String passwd) {
		User user = new User();
		user.setCid(1);
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
