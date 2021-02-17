-- 메뉴
insert into link_menu(id, company_id, name) values (1, '1', '요금');
insert into link_menu(id, company_id, name) values (2, '1', '수납');
insert into link_menu(id, company_id, name) values (3, '1', '예약');
insert into link_menu(id, company_id, name) values (4, '1', '해택');
insert into link_menu(id, company_id, name) values (5, '1', '기타');
insert into link_menu(id, company_id, name) values (6, '1', '나의도시가스');
insert into link_menu(id, company_id, name) values (7, '1', '전화발신');

-- 요금
insert into link_detail(company_id, menu_id, link_protocol, link_text, link_url, enable) values ('1', 1, 'app', '요금상세', 'pathgasapp://1/bills?activeIndex=0', 1);
insert into link_detail(company_id, menu_id, link_protocol, link_text, link_url, enable) values ('1', 1, 'app', '간편요금조회', 'pathgasapp://1/bills/simple?activeIndex=0', 1);
insert into link_detail(company_id, menu_id, link_protocol, link_text, link_url, enable) values ('1', 1, 'app', '실시간요금계산', 'pathgasapp://1/charge?activeIndex=0', 1);
insert into link_detail(company_id, menu_id, link_protocol, link_text, link_url, enable) values ('1', 1, 'app', '자가검침 신청', 'pathgasapp://1/indications/add', 1);
insert into link_detail(company_id, menu_id, link_protocol, link_text, link_url, enable) values ('1', 1, 'app', '자가검침 입력', 'pathgasapp://1/indications', 1);
insert into link_detail(company_id, menu_id, link_protocol, link_text, link_url, enable) values ('1', 1, 'app', '자가검침 정보 및 이력', 'pathgasapp://1/indications', 1);
insert into link_detail(company_id, menu_id, link_protocol, link_text, link_url, enable) values ('1', 1, 'app', '자가검침 해지', 'pathgasapp://1/indications/cancel', 1);
insert into link_detail(company_id, menu_id, link_protocol, link_text, link_url, enable) values ('1', 1, 'app', '가스요금단가', 'pathgasapp://1/costs', 1);
insert into link_detail(company_id, menu_id, link_protocol, link_text, link_url, enable) values ('1', 1, 'app', '청구서관리', 'pathgasapp://1/billSendMethod', 1);
insert into link_detail(company_id, menu_id, link_protocol, link_text, link_url, enable) values ('1', 1, 'app', '전기요금 청구서 신청', 'pathgasapp://1/hanbill/add', 1);
insert into link_detail(company_id, menu_id, link_protocol, link_text, link_url, enable) values ('1', 1, 'app', '전기요금 청구서 조회', 'pathgasapp://1/hanbill', 1);
insert into link_detail(company_id, menu_id, link_protocol, link_text, link_url, enable) values ('1', 1, 'app', '월별조회', 'pathgasapp://1/bills?activeIndex=0', 1);
insert into link_detail(company_id, menu_id, link_protocol, link_text, link_url, enable) values ('1', 1, 'app', '연간조회', 'pathgasapp://1/bills?activeIndex=1', 1);
insert into link_detail(company_id, menu_id, link_protocol, link_text, link_url, enable) values ('1', 1, 'app', '미니태양광신청조회', 'pathgasapp://1/solarServiceSearch', 1);

-- 수납
insert into link_detail(company_id, menu_id, link_protocol, link_text, link_url, enable) values ('1', 2, 'app', '납부내역', 'pathgasapp://1/bills/summary?activeIndex=1', 1);
insert into link_detail(company_id, menu_id, link_protocol, link_text, link_url, enable) values ('1', 2, 'app', '요금납부-미납내역', 'pathgasapp://1/bills/summary?activeIndex=0', 1);
insert into link_detail(company_id, menu_id, link_protocol, link_text, link_url, enable) values ('1', 2, 'app', '실시간납부내역', 'pathgasapp://1/pays/realtime', 1);
insert into link_detail(company_id, menu_id, link_protocol, link_text, link_url, enable) values ('1', 2, 'app', '자동이체', 'pathgasapp://1/directDebits', 1);
insert into link_detail(company_id, menu_id, link_protocol, link_text, link_url, enable) values ('1', 2, 'app', '과오납환불신청', 'pathgasapp://1/refunds/add', 1);
insert into link_detail(company_id, menu_id, link_protocol, link_text, link_url, enable) values ('1', 2, 'app', '도시가스경감요청서', 'pathgasapp://1/weakDiscount', 1);
insert into link_detail(company_id, menu_id, link_protocol, link_text, link_url, enable) values ('1', 2, 'app', '분할납부확약서', 'pathgasapp://1/promises', 1);
insert into link_detail(company_id, menu_id, link_protocol, link_text, link_url, enable) values ('1', 2, 'app', '요금단가', 'pathgasapp://1/costs', 1);
insert into link_detail(company_id, menu_id, link_protocol, link_text, link_url, enable) values ('1', 2, 'app', '납부유예신청', 'pathgasapp://1/payDelayDetail', 1);

-- 예약
insert into link_detail(company_id, menu_id, link_protocol, link_text, link_url, enable) values ('1', 3, 'app', '방문예약_캘린더', 'pathgasapp://1/reservations', 1);
insert into link_detail(company_id, menu_id, link_protocol, link_text, link_url, enable) values ('1', 3, 'app', '방문예약_전입', 'pathgasapp://1/reservations/add?type=IN', 1);
insert into link_detail(company_id, menu_id, link_protocol, link_text, link_url, enable) values ('1', 3, 'app', '방문예약_전출', 'pathgasapp://1/reservations/add?type=OUT', 1);
insert into link_detail(company_id, menu_id, link_protocol, link_text, link_url, enable) values ('1', 3, 'app', '방문예약_안전점검', 'pathgasapp://1/reservations/add?type=SAFE', 1);
insert into link_detail(company_id, menu_id, link_protocol, link_text, link_url, enable) values ('1', 3, 'app', '방문예약_연소기철거교체신청', 'pathgasapp://1/reservations/add?type=TRANSFER', 1);
insert into link_detail(company_id, menu_id, link_protocol, link_text, link_url, enable) values ('1', 3, 'app', '방문예약_배관계량기점검', 'pathgasapp://1/reservations/add?type=CHECK', 1);
insert into link_detail(company_id, menu_id, link_protocol, link_text, link_url, enable) values ('1', 3, 'app', '방문예약_공급중지', 'pathgasapp://1/reservations/add?type=STOP', 1);

-- 혜택
insert into link_detail(company_id, menu_id, link_protocol, link_text, link_url, enable) values ('1', 4, 'app', '캐시조회', 'pathgasapp://1/cash?activeIndex=0', 1);
insert into link_detail(company_id, menu_id, link_protocol, link_text, link_url, enable) values ('1', 4, 'app', '캐시적립', 'pathgasapp://1/cash?activeIndex=1', 1);
insert into link_detail(company_id, menu_id, link_protocol, link_text, link_url, enable) values ('1', 4, 'app', '캐시사용', 'pathgasapp://1/cash?activeIndex=2', 1);
insert into link_detail(company_id, menu_id, link_protocol, link_text, link_url, enable) values ('1', 4, 'app', '가스락이벤트', 'pathgasapp://1/cash/promotion', 1);
insert into link_detail(company_id, menu_id, link_protocol, link_text, link_url, enable) values ('1', 4, 'app', '적립가능캐시-프로모션', 'pathgasapp://1/cashBenefitList', 1);


-- 기타
insert into link_detail(company_id, menu_id, link_protocol, link_text, link_url, enable) values ('1', 5, 'app', '복지할인제도안내', 'pathgasapp://1/infos/3', 1);
insert into link_detail(company_id, menu_id, link_protocol, link_text, link_url, enable) values ('1', 5, 'app', '전국도시가스관할', 'pathgasapp://1/nationwide/area', 1);
insert into link_detail(company_id, menu_id, link_protocol, link_text, link_url, enable) values ('1', 5, 'app', '관할고객센터', 'pathgasapp://1/center/area', 1);
insert into link_detail(company_id, menu_id, link_protocol, link_text, link_url, enable) values ('1', 5, 'app', '공급중지보류신청', 'pathgasapp://1/stopholds', 1);
insert into link_detail(company_id, menu_id, link_protocol, link_text, link_url, enable) values ('1', 5, 'app', '보증보험 안내', 'pathgasapp://1/insurances', 1);
insert into link_detail(company_id, menu_id, link_protocol, link_text, link_url, enable) values ('1', 5, 'app', '보증보험 정보', 'pathgasapp://1/insurances', 1);
insert into link_detail(company_id, menu_id, link_protocol, link_text, link_url, enable) values ('1', 5, 'app', '보증보험 앱다운로드', 'pathgasapp://1/insurances', 1);
insert into link_detail(company_id, menu_id, link_protocol, link_text, link_url, enable) values ('1', 5, 'app', '이용약관', 'pathgasapp://1/terms', 1);
insert into link_detail(company_id, menu_id, link_protocol, link_text, link_url, enable) values ('1', 5, 'app', '개인정보처리방침', 'pathgasapp://1/terms', 1);
insert into link_detail(company_id, menu_id, link_protocol, link_text, link_url, enable) values ('1', 5, 'app', '이용안내_자가검침', 'pathgasapp://1/infos/1', 1);
insert into link_detail(company_id, menu_id, link_protocol, link_text, link_url, enable) values ('1', 5, 'app', '이용안내_요금산정방법', 'pathgasapp://1/infos/2', 1);
insert into link_detail(company_id, menu_id, link_protocol, link_text, link_url, enable) values ('1', 5, 'app', '이용안내_세금계산서발행', 'pathgasapp://1/infos/4', 1);
insert into link_detail(company_id, menu_id, link_protocol, link_text, link_url, enable) values ('1', 5, 'app', '이용안내_납부방법', 'pathgasapp://1/infos/5', 1);
insert into link_detail(company_id, menu_id, link_protocol, link_text, link_url, enable) values ('1', 5, 'app', '이용안내_체납중지', 'pathgasapp://1/infos/6', 1);
insert into link_detail(company_id, menu_id, link_protocol, link_text, link_url, enable) values ('1', 5, 'app', '이용안내_채무불이행등록', 'pathgasapp://1/infos/7', 1);
insert into link_detail(company_id, menu_id, link_protocol, link_text, link_url, enable) values ('1', 5, 'app', '자주하는질문', 'pathgasapp://1/faqs', 1);
insert into link_detail(company_id, menu_id, link_protocol, link_text, link_url, enable) values ('1', 5, 'app', '가스냄새신고', 'pathgasapp://1/report/gasSmell', 1);
insert into link_detail(company_id, menu_id, link_protocol, link_text, link_url, enable) values ('1', 5, 'app', '굴착공사신고', 'pathgasapp://1/report/forkcrane', 1);
insert into link_detail(company_id, menu_id, link_protocol, link_text, link_url, enable) values ('1', 5, 'app', '고객의소리', 'pathgasapp://1/vocs', 1);

-- 나의 도시가스
insert into link_detail(company_id, menu_id, link_protocol, link_text, link_url, enable) values ('1', 6, 'app', '사용계약번호 관리', 'pathgasapp://1/company', 1);
insert into link_detail(company_id, menu_id, link_protocol, link_text, link_url, enable) values ('1', 6, 'app', '안전점검 이력조회', 'pathgasapp://1/safechecks', 1);
insert into link_detail(company_id, menu_id, link_protocol, link_text, link_url, enable) values ('1', 6, 'app', '계량기교체 이력조회', 'pathgasapp://1/meters', 1);
insert into link_detail(company_id, menu_id, link_protocol, link_text, link_url, enable) values ('1', 6, 'app', '알림설정', 'pathgasapp://1/members/push', 1);
insert into link_detail(company_id, menu_id, link_protocol, link_text, link_url, enable) values ('1', 6, 'app', '사용계약번호 등록', 'pathgasapp://1/company/addN', 1);

-- 전화발신
insert into link_detail(company_id, menu_id, link_protocol, link_text, link_url, enable) values ('1', 7, 'app', '서울도시가스 콜센터', 'tel://1588-5788', 1);
insert into link_detail(company_id, menu_id, link_protocol, link_text, link_url, enable) values ('1', 7, 'app', '예스코 콜센터', 'tel://1544-3131', 1);
insert into link_detail(company_id, menu_id, link_protocol, link_text, link_url, enable) values ('1', 7, 'app', '귀뚜라미에너지 콜센터', 'tel://1670-4700', 1);
insert into link_detail(company_id, menu_id, link_protocol, link_text, link_url, enable) values ('1', 7, 'app', '대륜E&S 콜센터', 'tel://1566-6116', 1);
insert into link_detail(company_id, menu_id, link_protocol, link_text, link_url, enable) values ('1', 7, 'app', '삼천리 콜센터', 'tel://1544-3002', 1);
insert into link_detail(company_id, menu_id, link_protocol, link_text, link_url, enable) values ('1', 7, 'app', '인천도시가스 콜센터', 'tel://1600-0002', 1);
insert into link_detail(company_id, menu_id, link_protocol, link_text, link_url, enable) values ('1', 7, 'app', '코원에너지서비스 콜센터', 'tel://1599-3366', 1);