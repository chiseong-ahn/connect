-- company <--- comp : 회사
CREATE TABLE IF NOT EXISTS `company` (
  `id` varchar(10)  NOT NULL COMMENT 'PK',
  `create_date` timestamp NULL DEFAULT current_timestamp() COMMENT '생성일',
  `update_date` timestamp NULL DEFAULT current_timestamp() ON UPDATE current_timestamp() COMMENT '수정일',
  `update_member_id` bigint(5) unsigned DEFAULT NULL COMMENT '수정자 id(member table)',
  `name` varchar(255) NOT NULL COMMENT '회사명',
  `alias` varchar(255) DEFAULT NULL COMMENT '회사 별칭',
  `tel_number` varchar(255) DEFAULT NULL COMMENT '회사 전화번호',
  `fax_number` varchar(255) DEFAULT NULL COMMENT '회사 fax번호',
  `call_center_number` varchar(255) DEFAULT NULL COMMENT '콜센터번호',
  `email` varchar(255) DEFAULT NULL COMMENT '회사 이메일',
  `address` varchar(255) DEFAULT NULL COMMENT '회사 주소',
  `homepage` varchar(255) DEFAULT NULL COMMENT '회사 homepage',
  `use_config_json` varchar(1023) DEFAULT NULL COMMENT '회사별 설정 정보(json)',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='회사';


-- member <--- emp : 회원
CREATE TABLE IF NOT EXISTS `member` (
  `id` bigint(5) unsigned NOT NULL AUTO_INCREMENT COMMENT 'PK',
  `create_date` timestamp NULL DEFAULT current_timestamp() COMMENT '생성일',
  `update_date` timestamp NULL DEFAULT current_timestamp() ON UPDATE current_timestamp() COMMENT '수정일',
  `update_member_id` bigint(5) unsigned DEFAULT NULL COMMENT '수정자 id(member table)',
  `company_id` varchar(10) DEFAULT NULL COMMENT '회사 id(company table)',
  `is_admin` tinyint(1) NOT NULL DEFAULT 0 COMMENT '시스템관리자 여부',
  `auth_level` tinyint(1) unsigned NOT NULL DEFAULT 7 COMMENT '0:sys, 1:super, 2:admin, 3:mgr, 4:emp, 7:reader,9:guest',
  `login_name` varchar(255) NOT NULL COMMENT '로그인 name(직원번호)',
  `state` tinyint(1) unsigned NOT NULL DEFAULT 9 COMMENT '0:상담중,1:휴식,2:회의,3:콜중,5:퇴근,9:기타',
  `profile_image_id` bigint(5) unsigned DEFAULT NULL COMMENT '파일첨부 id(file_attach table)',
  `speaker_id` bigint(5) unsigned DEFAULT NULL COMMENT 'mesasge를 사용하는 1:1의 관계의 사용자 id(speaker는 customer, member와 같은 개념이므로 분류함 : speaker table)',
  `name` varchar(255) DEFAULT NULL COMMENT '직원 이름',
  `use_status` tinyint(1) NOT NULL DEFAULT 1 COMMENT '사용가능 여부',
  `password` varchar(255) DEFAULT NULL COMMENT '패스워드',
  `dept_name` varchar(255) DEFAULT NULL COMMENT '부서명',
  `position_name` varchar(255) DEFAULT NULL COMMENT '직급명',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='회원';


-- category_large <--- catelg : 카테고리 대분류
CREATE TABLE IF NOT EXISTS `category_large` (
  `id` bigint(5) unsigned NOT NULL AUTO_INCREMENT COMMENT 'PK',
  `create_date` timestamp NULL DEFAULT current_timestamp() COMMENT '생성일',
  `update_date` timestamp NULL DEFAULT current_timestamp() ON UPDATE current_timestamp() COMMENT '수정일',
  `update_member_id` bigint(5) unsigned DEFAULT NULL COMMENT '수정자 id(member table)',
  `company_id` varchar(10) NOT NULL COMMENT '회사 id(company table)',
  `name` varchar(255) DEFAULT NULL COMMENT '카테고리명',
  `minwon_code` varchar(255) DEFAULT NULL COMMENT '민원코드',
  `minwon_name` varchar(255) DEFAULT NULL COMMENT '민원코드명',
  `sort_index` INT DEFAULT NULL COMMENT '정렬 정보',
  `use_status` tinyint(1) NOT NULL DEFAULT 1 COMMENT '사용가능 여부',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='카테고리 대분류';


-- category_middle <--- catemd : 카테고리 중분류
CREATE TABLE IF NOT EXISTS `category_middle` (
  `id` bigint(5) unsigned NOT NULL AUTO_INCREMENT COMMENT 'PK',
  `create_date` timestamp NULL DEFAULT current_timestamp() COMMENT '생성일',
  `update_date` timestamp NULL DEFAULT current_timestamp() ON UPDATE current_timestamp() COMMENT '수정일',
  `update_member_id` bigint(5) unsigned DEFAULT NULL COMMENT '수정자 id(member table)',
  `company_id` varchar(10) NOT NULL COMMENT '회사 id(company table)',
  `name` varchar(255) DEFAULT NULL COMMENT '카테고리명',
  `category_large_id` bigint(5) unsigned DEFAULT NULL COMMENT '카테고리 대분류 id(category_large table)',
  `minwon_code` varchar(255) DEFAULT NULL COMMENT '민원코드',
  `minwon_name` varchar(255) DEFAULT NULL COMMENT '민원코드명',
  `sort_index` INT DEFAULT NULL COMMENT '정렬 정보',
  `use_status` tinyint(1) NOT NULL DEFAULT 1 COMMENT '사용가능 여부',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='카테고리 중분류';


-- category_small <--- catesm : 카테고리 소분류
CREATE TABLE IF NOT EXISTS `category_small` (
  `id` bigint(5) unsigned NOT NULL AUTO_INCREMENT COMMENT 'PK',
  `create_date` timestamp NULL DEFAULT current_timestamp() COMMENT '생성일',
  `update_date` timestamp NULL DEFAULT current_timestamp() ON UPDATE current_timestamp() COMMENT '수정일',
  `update_member_id` bigint(5) unsigned DEFAULT NULL COMMENT '수정자 id(member table)',
  `company_id` varchar(10) NOT NULL COMMENT '회사 id(company table)',
  `name` varchar(255) DEFAULT NULL COMMENT '카테고리명',
  `category_middle_id` bigint(5) unsigned DEFAULT NULL COMMENT '카테고리 대분류 id(category_middle table)',
  `minwon_code` varchar(255) DEFAULT NULL COMMENT '민원코드',
  `minwon_name` varchar(255) DEFAULT NULL COMMENT '민원코드명',
  `sort_index` INT DEFAULT NULL COMMENT '정렬 정보',
  `use_status` tinyint(1) NOT NULL DEFAULT 1 COMMENT '사용가능 여부',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='카테고리 소분류';


-- customer2 <--- customer : 고객
CREATE TABLE IF NOT EXISTS `customer2` (
  `id` bigint(5) unsigned NOT NULL AUTO_INCREMENT COMMENT 'PK',
  `create_date` timestamp NULL DEFAULT current_timestamp() COMMENT '생성일',
  `update_date` timestamp NULL DEFAULT current_timestamp() ON UPDATE current_timestamp() COMMENT '수정일',
  `update_member_id` bigint(5) unsigned DEFAULT NULL COMMENT '수정자 id(member table)',
  `gasapp_member_number` varchar(255) NOT NULL COMMENT '가스앱 회원번호',
  `name` varchar(255) DEFAULT NULL COMMENT '고객명',
  `tel_number` varchar(255) DEFAULT NULL COMMENT '핸드폰 번호',
  `speaker_id` bigint(5) unsigned DEFAULT NULL COMMENT 'mesasge를 사용하는 1:1의 관계의 사용자 id(speaker는 customer, member와 같은 개념이므로 분류함 : speaker table)',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='고객';


-- customer_company <--- new table : 고객 회사별 상세 정보
CREATE TABLE IF NOT EXISTS `customer_company` (
  `id` bigint(5) unsigned NOT NULL AUTO_INCREMENT COMMENT 'PK',
  `customer_id` bigint(5) unsigned NOT NULL COMMENT 'customer table id',
  `create_date` timestamp NULL DEFAULT current_timestamp() COMMENT '생성일',
  `update_date` timestamp NULL DEFAULT current_timestamp() ON UPDATE current_timestamp() COMMENT '수정일',
  `update_member_id` bigint(5) unsigned DEFAULT NULL COMMENT '수정자 id(member table)',
  `company_id` varchar(10) NOT NULL COMMENT '회사 id(company table)',
  `is_block` tinyint(1) NOT NULL DEFAULT 0 COMMENT '블록 여부',
  `block_type` varchar(255) DEFAULT NULL COMMENT 'block 사유(지정 안됨(해제), 욕설 및 비속어 사용, 업무 외 대화 시도, 모든 업무에 불만, 같은내용 지속적 반복, 과도한 의심, 무리한 업무진행 요구, 사장 등 상급자 찾음, 신고협박)',
  `block_date` timestamp NULL COMMENT '블록 날짜',
  `remark` varchar(255) DEFAULT NULL COMMENT '메모',
  `block_member_id` bigint(5) unsigned DEFAULT NULL COMMENT '블록을 설정한 회원 id(member table)',
  `room_id` bigint(5) unsigned DEFAULT NULL COMMENT '방 id(room table)',
  `speaker_id` bigint(5) unsigned DEFAULT NULL COMMENT 'mesasge를 사용하는 1:1의 관계의 사용자 id(speaker는 customer, member와 같은 개념이므로 분류함 : speaker table)',
  `swear_count` tinyint(1) unsigned NOT NULL DEFAULT 0 COMMENT '욕설 사용 회수',
  `insult_count` tinyint(1) unsigned NOT NULL DEFAULT 0 COMMENT '부적절한 멘트 회수',
  `state` tinyint(1) unsigned NOT NULL DEFAULT 0 COMMENT '사용하지 않는 것으로 판단됨',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='고객 회사별 상세 정보';


-- keyword2 <--- keyword : 키워드
CREATE TABLE IF NOT EXISTS `keyword2` (
  `id` bigint(5) unsigned NOT NULL AUTO_INCREMENT COMMENT 'PK',
  `create_date` timestamp NULL DEFAULT current_timestamp() COMMENT '생성일',
  `update_date` timestamp NULL DEFAULT current_timestamp() ON UPDATE current_timestamp() COMMENT '수정일',
  `update_member_id` bigint(5) unsigned DEFAULT NULL COMMENT '수정자 id(member table)',
  `company_id` varchar(10) NOT NULL COMMENT '회사 id(company table)',
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '타이틀',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='키워드';


-- manual <--- pdfmanual : 매뉴얼
CREATE TABLE IF NOT EXISTS `manual` (
  `id` bigint(5) unsigned NOT NULL AUTO_INCREMENT COMMENT 'PK',
  `create_date` timestamp NULL DEFAULT current_timestamp() COMMENT '생성일',
  `update_date` timestamp NULL DEFAULT current_timestamp() ON UPDATE current_timestamp() COMMENT '수정일',
  `update_member_id` bigint(5) unsigned DEFAULT NULL COMMENT '수정자 id(member table)',
  `company_id` varchar(10) NOT NULL COMMENT '회사 id(company table)',
  `manual_index` smallint(5) unsigned NOT NULL DEFAULT 1 COMMENT 'pdf 매뉴얼 사용 갯수',
  `page_number` smallint(5) unsigned NOT NULL DEFAULT 0 COMMENT 'page_number',
  `page_no` varchar(15) DEFAULT NULL COMMENT 'pdf pageno(회원 등록)',
  `page_code` varchar(7) DEFAULT NULL COMMENT 'page code',
  `title` varchar(255) DEFAULT NULL COMMENT '타이틀',
  `tags` varchar(255) DEFAULT NULL COMMENT 'page tags',
  `content` varchar(4096) DEFAULT NULL COMMENT 'page content',
  `pdf_image_path` varchar(255) DEFAULT NULL COMMENT '파일 다운로드 path',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='매뉴얼';


-- manual_favorite <--- pdfmanualfavorite : 매뉴얼 즐겨찾기
CREATE TABLE IF NOT EXISTS `manual_favorite` (
  `id` bigint(5) unsigned NOT NULL AUTO_INCREMENT COMMENT 'PK',
  `create_date` timestamp NULL DEFAULT current_timestamp() COMMENT '생성일',
  `update_date` timestamp NULL DEFAULT current_timestamp() ON UPDATE current_timestamp() COMMENT '수정일',
  `company_id` varchar(10) NOT NULL COMMENT '회사 id(company table)',
  `member_id` bigint(5) unsigned NOT NULL COMMENT '즐겨찾기한 회원 id(member table)',
  `manual_id` bigint(5) unsigned NOT NULL COMMENT '즐겨찾기한 매뉴얼 id(manual table)',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='매뉴얼 즐겨찾기';


-- template2 <--- template : 템플릿
CREATE TABLE IF NOT EXISTS `template2` (
  `id` bigint(5) unsigned NOT NULL AUTO_INCREMENT COMMENT 'PK',
  `create_date` timestamp NULL DEFAULT current_timestamp() COMMENT '생성일',
  `update_date` timestamp NULL DEFAULT current_timestamp() ON UPDATE current_timestamp() COMMENT '수정일',
  `update_member_id` bigint(5) unsigned DEFAULT NULL COMMENT '수정자 id(member table)',
  `member_id` bigint(5) unsigned DEFAULT NULL COMMENT '작성자 id(member table)',
  `company_id` varchar(10) NOT NULL COMMENT '회사 id(company table)',
  `category_small_id` bigint(5) unsigned DEFAULT NULL COMMENT '카테고리 소분류 id(category_small table)',
  `ask` varchar(1023) NOT NULL COMMENT '질문',
  `reply` varchar(1023) DEFAULT NULL COMMENT '답변',
  `link_url` varchar(255) DEFAULT NULL COMMENT 'link url',
  `link_protocol` varchar(10) DEFAULT NULL COMMENT '링크 프로토콜(web, app, tel)',
  `link_text` varchar(10) DEFAULT NULL COMMENT '링크 text(내부 링크시 사용 : table relation을 걸지는 않음)',
  `image_path` varchar(511) DEFAULT NULL COMMENT 'image path(사용 X)',
  `image_name` varchar(127) DEFAULT NULL COMMENT 'image name(사용 X)',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='템플릿';


-- auto_message <--- templateauto : 자동 메시지
CREATE TABLE IF NOT EXISTS `auto_message` (
  `id` bigint(5) unsigned NOT NULL AUTO_INCREMENT COMMENT 'PK',
  `create_date` timestamp NULL DEFAULT current_timestamp() COMMENT '생성일',
  `update_date` timestamp NULL DEFAULT current_timestamp() ON UPDATE current_timestamp() COMMENT '수정일',
  `update_member_id` bigint(5) unsigned DEFAULT NULL COMMENT '수정자 id(member table)',
  `company_id` varchar(10) NOT NULL COMMENT '회사 id(company table)',
  `type` tinyint(1) unsigned NOT NULL DEFAULT 0 COMMENT '0:신규대화인사말,1:배정지연,2:답변지연,3:상담 불가 시간 안내 메시지',
  `message` varchar(1023) NOT NULL COMMENT '멘트',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='자동 메시지';


-- template_favorite <--- templatefavorite : 템플릿 즐겨찾기
CREATE TABLE IF NOT EXISTS `template_favorite` (
  `id` bigint(5) unsigned NOT NULL AUTO_INCREMENT COMMENT 'PK',
  `create_date` timestamp NULL DEFAULT current_timestamp() COMMENT '생성일',
  `update_date` timestamp NULL DEFAULT current_timestamp() ON UPDATE current_timestamp() COMMENT '수정일',
  `member_id` bigint(5) unsigned NOT NULL COMMENT '즐겨찾기한 회원 id(member table)',
  `company_id` varchar(10) NOT NULL COMMENT '회사 id(company table)',
  `template_id` bigint(5) unsigned NOT NULL COMMENT '즐겨찾기한 템플릿 id(template table)',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='템플릿 즐겨찾기';


-- template_keyword <--- templatekeyword : 템플릿 키워드 맵핑 정보
CREATE TABLE IF NOT EXISTS `template_keyword` (
  `id` bigint(5) unsigned NOT NULL AUTO_INCREMENT COMMENT 'PK',
  `create_date` timestamp NULL DEFAULT current_timestamp() COMMENT '생성일',
  `update_date` timestamp NULL DEFAULT current_timestamp() ON UPDATE current_timestamp() COMMENT '수정일',
  `update_member_id` bigint(5) unsigned DEFAULT NULL COMMENT '수정자 id(member table)',
  `keyword_id` bigint(5) unsigned NOT NULL COMMENT '키워드 id(keyword table)',
  `company_id` varchar(10) NOT NULL COMMENT '회사 id(company table)',
  `template_id` bigint(5) unsigned NOT NULL COMMENT '템플릿 id(template table)',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='템플릿 키워드 맵핑 정보';


-- room <--- space : 방
CREATE TABLE IF NOT EXISTS `room` (
  `id` bigint(5) unsigned NOT NULL AUTO_INCREMENT COMMENT 'PK',
  `create_date` timestamp NULL DEFAULT current_timestamp() COMMENT '생성일',
  `update_date` timestamp NULL DEFAULT current_timestamp() ON UPDATE current_timestamp() COMMENT '수정일',
  `update_member_id` bigint(5) unsigned DEFAULT NULL COMMENT '수정자 id(member table)',
  `member_id` bigint(5) unsigned DEFAULT NULL COMMENT '담당 회원 id(member table)',
  `company_id` varchar(10) NOT NULL COMMENT '회사 id(company table)',
  `state` tinyint(1) unsigned NOT NULL DEFAULT 0 COMMENT '0:대기, 1:진행, 8:종료(방 최초 생성), 9:폐쇄 <--- (0:진행중,1:종료대기,2:종료,9:폐쇄)',
  `join_message_id` bigint(5) unsigned DEFAULT NULL COMMENT 'join 시작(또는 재시작) 메시지 id(chat_message table)',
  `chatid` int(10) unsigned DEFAULT NULL COMMENT '상담ID(기간계 연동)',
  `join_history_json` varchar(2047) DEFAULT NULL COMMENT 'join 이전 history(json)',
  `is_online` tinyint(1) NOT NULL DEFAULT 0 COMMENT '고객의 온라인 상태',
  `name` varchar(255) DEFAULT NULL COMMENT 'room 이름',
  `end_date` timestamp NULL DEFAULT NULL COMMENT '종료대기 및 종료한 시간',
  `last_member_id` bigint(5) unsigned DEFAULT NULL COMMENT '이전 담당 회원 id(member table)',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='방';


-- room_join_history <--- spacehist : 조인 히스토리(보통 종료 이력으로 사용함)
CREATE TABLE IF NOT EXISTS `room_join_history` (
  `id` bigint(5) unsigned NOT NULL AUTO_INCREMENT COMMENT 'PK',
  `create_date` timestamp NULL DEFAULT current_timestamp() COMMENT '생성일',
  `update_date` timestamp NULL DEFAULT current_timestamp() ON UPDATE current_timestamp() COMMENT '수정일',
  `update_member_id` bigint(5) unsigned DEFAULT NULL COMMENT '수정자 id(member table)',
  `member_id` bigint(5) unsigned DEFAULT NULL COMMENT '방의 담당 회원 id(member table)',
  `room_id` bigint(5) unsigned DEFAULT NULL COMMENT '방 id(room table)',
  `company_id` varchar(10) NOT NULL COMMENT '회사 id(company table)',
  `start_message_id` bigint(5) unsigned DEFAULT NULL COMMENT '조인 시작 시작 메시지 id(chat_message table)',
  `end_message_id` bigint(5) unsigned DEFAULT NULL COMMENT '상담 종료 메시지 id(chat_message table)',
  `end_date` timestamp NULL DEFAULT NULL COMMENT '상담 종료한 시간',
  `last_member_id` bigint(5) unsigned DEFAULT NULL COMMENT '이전 담당 회원 id(member table)',
  `category_small_id` bigint(5) unsigned DEFAULT NULL COMMENT '카테고리 소분류 id(category_small table)',
  `join_history_json` varchar(2047) DEFAULT NULL COMMENT 'join 이전 history(json)',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='조인 히스토리(보통 종료 이력으로 사용함)';


-- speaker2 <--- speaker : 채팅 사용자(고객, 회원을 같은 사용자 개념으로 묶기 위한)
CREATE TABLE IF NOT EXISTS `speaker2` (
  `id` bigint(5) unsigned NOT NULL AUTO_INCREMENT COMMENT 'PK',
  `create_date` timestamp NULL DEFAULT current_timestamp() COMMENT '생성일',
  `update_date` timestamp NULL DEFAULT current_timestamp() ON UPDATE current_timestamp() COMMENT '수정일',
  `update_member_id` bigint(5) unsigned DEFAULT NULL COMMENT '수정자 id(member table)',
  `company_id` varchar(10) NOT NULL COMMENT '회사 id(company table)',
  `name` varchar(255) DEFAULT NULL COMMENT '대화명',
  `is_customer` tinyint(1) NOT NULL DEFAULT 1 COMMENT '고객여부',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='채팅 사용자(고객, 회원을 같은 사용자 개념으로 묶기 위한)';


-- chat_message <--- speak : 메시지
CREATE TABLE IF NOT EXISTS `chat_message` (
  `id` bigint(5) unsigned NOT NULL AUTO_INCREMENT COMMENT 'PK',
  `create_date` timestamp NULL DEFAULT current_timestamp() COMMENT '생성일',
  `update_date` timestamp NULL DEFAULT current_timestamp() ON UPDATE current_timestamp() COMMENT '수정일',
  `speaker_id` bigint(5) unsigned DEFAULT NULL COMMENT '메시지를 입력한 사용자 id(speaker table)',
  `room_id` bigint(5) unsigned NOT NULL COMMENT '방 id(room table)',
  `company_id` varchar(10) NOT NULL COMMENT '회사 id(company table)',
  `message_type` tinyint(1) unsigned NOT NULL DEFAULT 0 COMMENT '0(일반), 1(이미지), 2(동영상 : X), 3(첨부파일 : X), 4(링크), 5(이모티콘), 6(전화번호)',
  `not_read_count` smallint(5) signed NOT NULL DEFAULT 0 COMMENT '메시지 읽지 않은 사용자 count ',
  `is_system_message` tinyint(1) NOT NULL DEFAULT 0 COMMENT '1:시스템메시지',
  `message` varchar(2047) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'message',
  `message_admin_type` tinyint(1) unsigned NOT NULL DEFAULT 0 COMMENT 'sys msg only adm (0:false, 1:noti, 2:warn)',
  `is_employee` tinyint(1) NOT NULL DEFAULT 0 COMMENT '직원 작성 여부',
  `message_detail` varchar(255) DEFAULT NULL COMMENT '첨부 파일/링크 이름',
  `template_id` bigint(5) unsigned DEFAULT NULL COMMENT '템플릿 id(template table)',
  `is_delete` tinyint(1) NOT NULL DEFAULT 0 COMMENT '메시지 삭제 여부(0, 1)',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='메시지';
ALTER TABLE chat_message MODIFY COLUMN company_id varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '회사 id(company table)';


-- room_speaker <--- spacespeaker : 방, 사용자 맵핑 정보
CREATE TABLE IF NOT EXISTS `room_speaker` (
  `id` bigint(5) unsigned NOT NULL AUTO_INCREMENT COMMENT 'PK',
  `create_date` timestamp NULL DEFAULT current_timestamp() COMMENT '생성일',
  `update_date` timestamp NULL DEFAULT current_timestamp() ON UPDATE current_timestamp() COMMENT '수정일',
  `update_member_id` bigint(5) unsigned DEFAULT NULL COMMENT '수정자 id(member table)',
  `company_id` varchar(10) DEFAULT NULL COMMENT '회사 id(company table)',
  `speaker_id` bigint(5) unsigned NOT NULL COMMENT '방에 입장한 사용자 id(speaker table)',
  `room_id` bigint(5) unsigned NOT NULL COMMENT '방 id(room table)',
  `read_last_message_id` bigint(5) unsigned DEFAULT NULL COMMENT '마지막 읽은 메시지 id(chat_message table)',
  `old_last_message_id` bigint(5) unsigned DEFAULT NULL COMMENT '이전 마지막 읽은 메시지(chat_message table) : 사용하지 않음',
  `is_alarm` tinyint(1) NOT NULL DEFAULT 0 COMMENT 'alarm off 여부 : 사용하지 않음',
  `is_customer` tinyint(1) NOT NULL DEFAULT 1 COMMENT '고객여부',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='방, 사용자 맵핑 정보';


-- message_read <--- new table : 메시지 읽음 정보
CREATE TABLE IF NOT EXISTS `message_read` (
  `id` bigint(5) unsigned NOT NULL AUTO_INCREMENT COMMENT 'PK',
  `create_date` timestamp NULL DEFAULT current_timestamp() COMMENT '생성일',
  `update_date` timestamp NULL DEFAULT current_timestamp() ON UPDATE current_timestamp() COMMENT '수정일',
  `read_date` timestamp NULL COMMENT '메시지 읽은 날짜',
  `company_id` varchar(10) NOT NULL COMMENT '회사 id(company table)',
  `room_id` bigint(5) unsigned NOT NULL COMMENT '방 id(room table)',
  `message_id` bigint(5) unsigned NOT NULL COMMENT '메시지 id(chat_message table)',
  `speaker_id` bigint(5) unsigned NOT NULL COMMENT '메시지 읽은 사용자 id(speaker table)',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='메시지 읽음 정보';


-- minwon_history <--- new table : 민원 이력
CREATE TABLE IF NOT EXISTS `minwon_history` (
  `id` bigint(5) unsigned NOT NULL AUTO_INCREMENT COMMENT 'PK',
  `company_id` varchar(10) NOT NULL COMMENT '회사 id(company table)',
  `create_date` timestamp NULL DEFAULT current_timestamp() COMMENT '생성일',
  `update_date` timestamp NULL DEFAULT current_timestamp() ON UPDATE current_timestamp() COMMENT '수정일',
  `update_member_id` bigint(5) unsigned DEFAULT NULL COMMENT '수정자 id(member table)',
  `category_small_id` bigint(5) unsigned DEFAULT NULL COMMENT '카테고리 소분류 id(category_small table)',
  `minwon_code` varchar(255) DEFAULT NULL COMMENT '민원코드',
  `gasapp_member_number` varchar(255) NOT NULL COMMENT '가스앱 회원번호',
  `use_contract_num` varchar(255) DEFAULT NULL COMMENT '사용계약번호',
  `tel_number` varchar(255) DEFAULT NULL COMMENT '고객 핸드폰 번호',
  `memo` varchar(2047) DEFAULT NULL COMMENT '메모',
  `chatid` int(10) unsigned DEFAULT NULL COMMENT '상담ID(기간계 연동)',
  `room_id` bigint(5) unsigned NOT NULL COMMENT '방 id(room table)',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='민원 이력';


-- link_menu <--- new table : 링크 메뉴
CREATE TABLE IF NOT EXISTS `link_menu` (
  `id` bigint(5) unsigned NOT NULL AUTO_INCREMENT COMMENT 'PK',
  `company_id` varchar(10) NOT NULL COMMENT '회사 id(company table)',
  `create_date` timestamp NULL DEFAULT current_timestamp() COMMENT '생성일',
  `update_date` timestamp NULL DEFAULT current_timestamp() ON UPDATE current_timestamp() COMMENT '수정일',
  `update_member_id` bigint(5) unsigned DEFAULT NULL COMMENT '수정자 id(member table)',
  `name` varchar(255) NOT NULL COMMENT '링크 프로토콜(web, app, tel)',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='링크 메뉴';


-- link_detail <--- new table : 링크 상세
CREATE TABLE IF NOT EXISTS `link_detail` (
  `id` bigint(5) unsigned NOT NULL AUTO_INCREMENT COMMENT 'PK',
  `company_id` varchar(10) NOT NULL COMMENT '회사 id(company table)',
  `create_date` timestamp NULL DEFAULT current_timestamp() COMMENT '생성일',
  `update_date` timestamp NULL DEFAULT current_timestamp() ON UPDATE current_timestamp() COMMENT '수정일',
  `update_member_id` bigint(5) unsigned DEFAULT NULL COMMENT '수정자 id(member table)',
  `link_protocol` varchar(255) NOT NULL COMMENT '링크 프로토콜(web, app, tel)',
  `link_text` varchar(255) NOT NULL COMMENT '링크 text',
  `link_url` varchar(255) DEFAULT NULL COMMENT '링크 url',
  `enable` tinyint(1) NOT NULL DEFAULT 0 COMMENT '활성화 여부(0, 1)',
  `menu_id` bigint(5) unsigned NOT NULL COMMENT '속해있는 메뉴 id(link_menu table)',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='링크 상세';


-- template_use_history <--- new table : 템플릿 클릭 히스토리
CREATE TABLE IF NOT EXISTS `template_use_history` (
  `id` bigint(5) unsigned NOT NULL AUTO_INCREMENT COMMENT 'PK',
  `company_id` varchar(10) NOT NULL COMMENT '회사 id(company table)',
  `create_date` timestamp NULL DEFAULT current_timestamp() COMMENT '생성일',
  `update_date` timestamp NULL DEFAULT current_timestamp() ON UPDATE current_timestamp() COMMENT '수정일',
  `update_member_id` bigint(5) unsigned DEFAULT NULL COMMENT '수정자 id(member table)',
  `room_id` bigint(5) unsigned DEFAULT NULL COMMENT '방 id(room table)',
  `template_id` bigint(5) unsigned DEFAULT NULL COMMENT '템플릿 id(template table)',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='템플릿 클릭 히스토리';


-- stats_company <--- new table : 일별 회사 통계
CREATE TABLE IF NOT EXISTS `stats_company` (
  `id` bigint(5) unsigned NOT NULL AUTO_INCREMENT COMMENT 'PK',
  `company_id` varchar(10) NOT NULL COMMENT '회사 id(company table)',
  `create_date` timestamp NULL DEFAULT current_timestamp() COMMENT '생성일',
  `update_date` timestamp NULL DEFAULT current_timestamp() ON UPDATE current_timestamp() COMMENT '수정일',
  `save_date` varchar(10) NOT NULL COMMENT '기준일 (YYYY-MM-DD)',
  `chatbot_use_count` int(10) unsigned DEFAULT 0 COMMENT '챗봇 이용 고객 건수',
  `talk_system_enter_count` int(10) unsigned DEFAULT 0 COMMENT '채팅상담시스템 인입 고객 건수',
  `new_count` int(10) unsigned DEFAULT 0 COMMENT '신규 접수 건수',
  `ready_count` int(10) unsigned DEFAULT 0 COMMENT '대기 상담 건수',
  `ing_count` int(10) unsigned DEFAULT 0 COMMENT '진행 상담 건수',
  `close_count` int(10) unsigned DEFAULT 0 COMMENT '종료 상담 건수',
  `out_count` int(10) unsigned DEFAULT 0 COMMENT '고객 이탈 건수',
  `speak_count` int(10) unsigned DEFAULT 0 COMMENT '총 상담건수',
  `max_ready_minute` int(10) unsigned DEFAULT 0 COMMENT '최장 고객 대기시간',
  `max_speak_minute` int(10) unsigned DEFAULT 0 COMMENT '최장 상담시간',
  `avg_ready_minute` int(10) unsigned DEFAULT 0 COMMENT '평균 고객 대기시간',
  `avg_speak_minute` int(10) unsigned DEFAULT 0 COMMENT '평균 상담시간',
  `avg_member_speak_count` int(10) unsigned DEFAULT 0 COMMENT '상담원 평균 응대 건수',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='일별 회사 통계';


-- stats_member <--- new table : 일별 회원 통계
CREATE TABLE IF NOT EXISTS `stats_member` (
  `id` bigint(5) unsigned NOT NULL AUTO_INCREMENT COMMENT 'PK',
  `company_id` varchar(10) NOT NULL COMMENT '회사 id(company table)',
  `member_id` bigint(5) unsigned DEFAULT NULL COMMENT '회원 id(member table)',
  `create_date` timestamp NULL DEFAULT current_timestamp() COMMENT '생성일',
  `update_date` timestamp NULL DEFAULT current_timestamp() ON UPDATE current_timestamp() COMMENT '수정일',
  `save_date` varchar(10) NOT NULL COMMENT '기준일 (YYYY-MM-DD)',
  `ing_count` int(10) unsigned DEFAULT 0 COMMENT '진행 상담 건수',
  `close_count` int(10) unsigned DEFAULT 0 COMMENT '종료 상담 건수',
  `new_count` int(10) unsigned DEFAULT 0 COMMENT '신규 접수 건수',
  `speak_count` int(10) unsigned DEFAULT 0 COMMENT '총 상담건수',
  `max_ready_minute` int(10) unsigned DEFAULT 0 COMMENT '최장 고객 대기시간',
  `max_speak_minute` int(10) unsigned DEFAULT 0 COMMENT '최장 상담시간',
  `avg_ready_minute` int(10) unsigned DEFAULT 0 COMMENT '평균 고객 대기시간',
  `avg_speak_minute` int(10) unsigned DEFAULT 0 COMMENT '평균 상담시간',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='일별 회원 통계';


-- stats_hashtag <--- new table : 일별 키워드 통계(랭크용)
CREATE TABLE IF NOT EXISTS `stats_hashtag` (
  `id` bigint(5) unsigned NOT NULL AUTO_INCREMENT COMMENT 'PK',
  `company_id` varchar(10) NOT NULL COMMENT '회사 id(company table)',
  `create_date` timestamp NULL DEFAULT current_timestamp() COMMENT '생성일',
  `update_date` timestamp NULL DEFAULT current_timestamp() ON UPDATE current_timestamp() COMMENT '수정일',
  `rank_num` int(10) unsigned DEFAULT 0 COMMENT '랭크',
  `save_date` varchar(10) NOT NULL COMMENT '기준일 (YYYY-MM-DD)',
  `name` varchar(255) NOT NULL COMMENT '태그명(카테고리 소분류, 메시지 상세...)',
  `issue_count` int(10) unsigned DEFAULT 0 COMMENT 'count',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='일별 키워드 통계(랭크용)';


-- alarm_member <--- new table : 알람 상세
CREATE TABLE IF NOT EXISTS `alarm_member` (
  `id` bigint(5) unsigned NOT NULL AUTO_INCREMENT COMMENT 'PK',
  `company_id` varchar(10) NOT NULL COMMENT '회사 id(company table)',
  `create_date` timestamp NULL DEFAULT current_timestamp() COMMENT '생성일',
  `update_date` timestamp NULL DEFAULT current_timestamp() ON UPDATE current_timestamp() COMMENT '수정일',
  `member_id` bigint(5) unsigned DEFAULT NULL COMMENT '알림을 보낸 사람(member table : 시스템인 경우 null)',
  `title` varchar(256) DEFAULT NULL COMMENT '제목 ',
  `content` varchar(2047) DEFAULT NULL COMMENT '내용 ',
  `read_date` timestamp NULL COMMENT '알림 읽은 날짜',
  `is_read` tinyint(1) NOT NULL DEFAULT 0 COMMENT '읽음 여부',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='알람 상세';


-- action_history <--- new table : 액션 히스토리
CREATE TABLE IF NOT EXISTS `action_history` (
  `id` bigint(5) unsigned NOT NULL AUTO_INCREMENT COMMENT 'PK',
  `company_id` varchar(10) DEFAULT NULL COMMENT '회사 id(company table)',
  `create_date` timestamp NULL DEFAULT current_timestamp() COMMENT '생성일',
  `update_date` timestamp NULL DEFAULT current_timestamp() ON UPDATE current_timestamp() COMMENT '수정일',
  `member_id` bigint(5) unsigned DEFAULT NULL COMMENT '수행한 회원 id(member table : 시스템인 경우 null)',
  `action_type` varchar(256) NOT NULL COMMENT '액션 유형 ',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='액션 히스토리';


-- talk_review <--- new table : 고객 리뷰
CREATE TABLE IF NOT EXISTS `talk_review` (
  `id` bigint(5) unsigned NOT NULL AUTO_INCREMENT COMMENT 'PK',
  `company_id` varchar(10) DEFAULT NULL COMMENT '회사 id(company table)',
  `create_date` timestamp NULL DEFAULT current_timestamp() COMMENT '생성일',
  `update_date` timestamp NULL DEFAULT current_timestamp() ON UPDATE current_timestamp() COMMENT '수정일',
  `gasapp_member_number` varchar(255) NOT NULL COMMENT '가스앱 회원번호',
  `review_score` smallint(5) unsigned NOT NULL DEFAULT 1 COMMENT '리뷰 점수(1,2,3,4,5)',
  `member_id` bigint(5) unsigned DEFAULT NULL COMMENT '회원 id(member table)',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='고객 리뷰';


/*

-- data migration query

-- company
INSERT INTO company (id, create_date, update_date, name, tel_number, homepage, fax_number, email, address, alias )
    SELECT CONCAT(id, ''), createdate, workdate, name, corpno, homepage, fax, email, addr, webname
    FROM Comp;

-- member
INSERT INTO member (id, create_date, update_date, company_id, auth_level, login_name, state, profile_image_id, speaker_id)
    SELECT id, createdate, workdate, CONCAT(cid, ''), auth, empno, state, profileimg, speaker
FROM Emp;

update member
  join Speaker on Speaker.id = member.speaker_id
   set member.name = Speaker.name;

UPDATE member
set auth_level = 3
where auth_level < 3;

insert into member(company_id, is_admin, auth_level, login_name, state, speaker_id, name, dept_name, position_name, use_status, password)
values('1', 1, 0, 'seoul_admin', 0, null, '서울도시가스 관리자', 'SCGLAB', '서비스관리자', 1, '9fd0b9976a4287506e489a33b04ca9f2f0f29249692ff08b6c84f1fa82a51e97');

-- category_large
INSERT INTO category_large (id, create_date, update_date, company_id, name, minwon_code, minwon_name, sort_index)
    SELECT id, createdate, workdate, CONCAT(cid, ''), name,  mwcode, mwname, id
    FROM CateLg;

-- category_middle
INSERT INTO category_middle (id, create_date, update_date, company_id, category_large_id,  name, minwon_code, minwon_name, sort_index)
    SELECT id, createdate, workdate, CONCAT(cid, ''), catelg, name,  mwcode, mwname, id
    FROM CateMd;

-- category_small
INSERT INTO category_small (id, create_date, update_date, company_id, category_middle_id,  name, minwon_code, minwon_name, sort_index)
    SELECT id, createdate, workdate, CONCAT(cid, ''), catemd, name,  mwcode, mwname, id
    FROM CateSm;

-- customer2
INSERT INTO customer2 (id, create_date, update_date, gasapp_member_number, name, tel_number, speaker_id)
SELECT id, createdate, workdate, userno, name, telno, speaker
FROM Customer;

-- customer_company
INSERT INTO customer_company (customer_id, create_date, update_date, company_id, is_block, block_type, block_date, remark, block_member_id, room_id, speaker_id, swear_count, insult_count, state )
SELECT id, createdate, workdate, '1', 0, blocktype, blockdt, remark, blockemp, space, speaker, swear, insult, state
FROM Customer;

update customer_company 
set block_type = ''
where block_type = '0' or block_type is null;

update customer_company 
set block_type = '업무 외 대화 시도'
where block_type = '2';

-- keyword2
INSERT INTO keyword2 (id, create_date, update_date, company_id, name)
SELECT id, createdate, workdate, cid, name
FROM Keyword;

-- manual
INSERT INTO manual (id, create_date, update_date, company_id, manual_index, page_number, page_no, page_code, title, tags, content, pdf_image_path)
SELECT id, createdate, workdate, '1', 1, pno, pageno, pagecode, title, tags, content, pdfimg
FROM PdfManual;

INSERT INTO manual (create_date, update_date, company_id, manual_index, page_number, page_no, page_code, title, tags, content, pdf_image_path)
SELECT createdate, workdate, '1', 2, pno, pageno, pagecode, title, tags, content, pdfimg
FROM PdfManual2018;

-- manual_favorite
INSERT INTO manual_favorite (id, create_date, update_date, company_id, member_id, manual_id)
SELECT id, createdate, workdate, '1', emp, pdf
FROM PdfManualFavorite;

-- template2
INSERT INTO template2 (id, create_date, update_date, company_id, category_small_id, ask, reply, link_url, image_path, image_name)
SELECT id, createdate, workdate, '1', catesm, ask, reply, link, img, imgname
FROM Template;

update template2 m
  join Template s on s.id = m.id
   set m.update_member_id = s.emp, m.member_id = s.emp;

-- auto_message
INSERT INTO auto_message (id, create_date, update_date, update_member_id, company_id, type, message)
SELECT id, createdate, workdate, emp, '1', type, msg
FROM TemplateAuto;

-- template_favorite
INSERT INTO template_favorite (id, create_date, update_date, member_id, company_id, template_id)
SELECT id, createdate, workdate, emp, '1', template
FROM TemplateFavorite;

-- template_keyword
INSERT INTO template_keyword (id, create_date, update_date, company_id, template_id, keyword_id)
SELECT id, createdate, workdate, '1', template, keyword
FROM TemplateKeyword;

-- room
INSERT INTO room (id, create_date, update_date, member_id, company_id, state, join_message_id, chatid, join_history_json, is_online, name, end_date, last_member_id)
SELECT id, createdate, workdate, emp, CONCAT(cid, ''), state, startid, chatid, prehistory, isonline, customer, enddt, lastemp
FROM Space;

UPDATE room
set state = 8
where state = 2;

UPDATE room
set state = 8, member_id = null, last_member_id = member_id, join_message_id = null, end_date = now()
where state = 1 and end_date is null;

update room
set join_message_id = null
where join_message_id = 0;

-- room_join_history
INSERT INTO room_join_history (id, create_date, update_date, company_id, member_id, room_id, start_message_id, end_message_id, end_date, last_member_id, category_small_id, join_history_json)
SELECT id, createdate, workdate, CONCAT(cid, ''), emp, space, startid, endid, enddt, lastemp, catesm, prehistory
FROM SpaceHist;

update room_join_history
set start_message_id = null
where start_message_id = 0;

-- speaker2
INSERT INTO speaker2 (id, create_date, update_date, company_id, name, is_customer)
SELECT id, createdate, workdate, CONCAT(cid, ''), name, iscustomer
FROM Speaker;

-- chat_message
INSERT INTO chat_message (id, create_date, update_date, company_id, room_id, speaker_id, message_type, not_read_count, is_system_message, message, message_admin_type, is_employee, message_detail)
SELECT id, createdate, workdate, CONCAT(cid, ''), space, speaker, mtype, 0, sysmsg, msg, onlyadm, isemp, msgname
FROM Speak;

-- room_speaker
INSERT INTO room_speaker (id, create_date, update_date, company_id, speaker_id, room_id, read_last_message_id, old_last_message_id, is_alarm, is_customer)
SELECT id, createdate, workdate, '1', speaker, space, lastid, oldid, iscalm, 1
FROM SpaceSpeaker
where spacename is not null;

INSERT INTO room_speaker (id, create_date, update_date, company_id, speaker_id, room_id, read_last_message_id, old_last_message_id, is_alarm, is_customer)
SELECT id, createdate, workdate, '1', speaker, space, lastid, oldid, iscalm, 0
FROM SpaceSpeaker
where spacename is null;

-- message_read
INSERT INTO message_read (create_date, update_date, read_date, company_id, room_id, message_id, speaker_id)
SELECT Speak.createdate, Speak.workdate, Speak.workdate, '1', Speak.space, Speak.id, SpaceSpeaker.speaker
FROM Speak inner join SpaceSpeaker on Speak.space = SpaceSpeaker.space;


*/


/*


-- 외래키, 인덱스

-- company
ALTER TABLE company ADD CONSTRAINT company_member_FK FOREIGN KEY (update_member_id) REFERENCES `member`(id) ON DELETE SET NULL;

-- member
ALTER TABLE `member` ADD CONSTRAINT member_company_FK FOREIGN KEY (company_id) REFERENCES company(id);
ALTER TABLE `member` ADD CONSTRAINT member_member_FK FOREIGN KEY (update_member_id) REFERENCES `member`(id) ON DELETE SET NULL;
ALTER TABLE `member` ADD CONSTRAINT member_speaker_FK FOREIGN KEY (speaker_id) REFERENCES speaker2(id);

-- category_large
ALTER TABLE category_large ADD CONSTRAINT category_large_company_FK FOREIGN KEY (company_id) REFERENCES company(id);
ALTER TABLE category_large ADD CONSTRAINT category_large_member_FK FOREIGN KEY (update_member_id) REFERENCES `member`(id) ON DELETE SET NULL;

--  category_middle
ALTER TABLE category_middle ADD CONSTRAINT category_middle_company_FK FOREIGN KEY (company_id) REFERENCES company(id);
ALTER TABLE category_middle ADD CONSTRAINT category_middle_member_FK FOREIGN KEY (update_member_id) REFERENCES `member`(id) ON DELETE SET NULL;
ALTER TABLE category_middle ADD CONSTRAINT category_middle_category_large_FK FOREIGN KEY (category_large_id) REFERENCES category_large(id);

-- category_small
ALTER TABLE category_small ADD CONSTRAINT category_small_company_FK FOREIGN KEY (company_id) REFERENCES company(id);
ALTER TABLE category_small ADD CONSTRAINT category_small_member_FK FOREIGN KEY (update_member_id) REFERENCES `member`(id) ON DELETE SET NULL;
ALTER TABLE category_small ADD CONSTRAINT category_small_category_middle_FK FOREIGN KEY (category_middle_id) REFERENCES category_middle(id);

-- customer2
ALTER TABLE customer2 ADD CONSTRAINT customer_member_FK FOREIGN KEY (update_member_id) REFERENCES `member`(id) ON DELETE SET NULL;
ALTER TABLE customer2 ADD CONSTRAINT customer_speaker_FK FOREIGN KEY (speaker_id) REFERENCES speaker2(id);

 @@.customer_gasapp_member_number_IDX 유니크 인덱스 생성전에 중복 데이터 삭제
  delete
  from customer2
  where id in(
  select id from (
    select id
    from customer2
    where id not in(
      select min(id)
      from customer2
      group by gasapp_member_number 
      having count(*) > 1
    )
    and gasapp_member_number in(
      select gasapp_member_number 
      from customer2
      group by gasapp_member_number 
      having count(*) > 1
    ) )as a
  );

CREATE UNIQUE INDEX customer_gasapp_member_number_IDX USING BTREE ON customer2 (gasapp_member_number);


-- customer_company
ALTER TABLE customer_company ADD CONSTRAINT customer_company_customer_FK FOREIGN KEY (customer_id) REFERENCES customer2(id) ON DELETE CASCADE;

delete
    from customer_company cc 
    where customer_id not in(
    select id
    from (
      select id
      from customer2 c2 
      ) as a
    );

ALTER TABLE customer_company ADD CONSTRAINT customer_company_member_FK FOREIGN KEY (update_member_id) REFERENCES `member`(id) ON DELETE SET NULL;
ALTER TABLE customer_company ADD CONSTRAINT customer_company_company_FK FOREIGN KEY (company_id) REFERENCES company(id);
ALTER TABLE customer_company ADD CONSTRAINT customer_company_member_FK_1 FOREIGN KEY (block_member_id) REFERENCES `member`(id) ON DELETE SET NULL;
ALTER TABLE customer_company ADD CONSTRAINT customer_company_room_FK FOREIGN KEY (room_id) REFERENCES room(id);
ALTER TABLE customer_company ADD CONSTRAINT customer_company_speaker_FK FOREIGN KEY (speaker_id) REFERENCES speaker2(id);
CREATE UNIQUE INDEX customer_company_customer_id_IDX USING BTREE ON customer_company (company_id, customer_id);

-- keyword2
ALTER TABLE keyword2 ADD CONSTRAINT keyword_member_FK FOREIGN KEY (update_member_id) REFERENCES `member`(id) ON DELETE SET NULL;
ALTER TABLE keyword2 ADD CONSTRAINT keyword_company_FK FOREIGN KEY (company_id) REFERENCES company(id);
CREATE UNIQUE INDEX keyword_company_id_IDX USING BTREE ON keyword2 (company_id, name);

-- manual
ALTER TABLE manual ADD CONSTRAINT manual_member_FK FOREIGN KEY (update_member_id) REFERENCES `member`(id) ON DELETE SET NULL;
ALTER TABLE manual ADD CONSTRAINT manual_company_FK FOREIGN KEY (company_id) REFERENCES company(id);
CREATE UNIQUE INDEX manual_company_id_IDX USING BTREE ON manual (company_id, manual_index, page_number);
CREATE INDEX manual_company_id_IDX2 USING BTREE ON manual (company_id, manual_index);

-- manual_favorite
ALTER TABLE manual_favorite ADD CONSTRAINT manual_favorite_company_FK FOREIGN KEY (company_id) REFERENCES company(id);
ALTER TABLE manual_favorite ADD CONSTRAINT manual_favorite_member_FK FOREIGN KEY (member_id) REFERENCES `member`(id) ON DELETE CASCADE;

delete
    from manual_favorite cc 
    where manual_id not in(
    select id
    from (
      select id
      from manual c2 
      ) as a
    );

ALTER TABLE manual_favorite ADD CONSTRAINT manual_favorite_manual_FK FOREIGN KEY (manual_id) REFERENCES manual(id) ON DELETE CASCADE;


  delete
  from manual_favorite
  where id in(
  select id from (
    select id
    from manual_favorite
    where id not in(
      select min(id)
      from manual_favorite
      group by member_id, manual_id 
      having count(*) > 1
    )
    and manual_id in(
      select manual_id 
      from manual_favorite
      group by member_id, manual_id 
      having count(*) > 1
    ) )as a
  );
  
CREATE UNIQUE INDEX manual_favorite_member_id_IDX USING BTREE ON manual_favorite (member_id, manual_id);



-- template2
ALTER TABLE template2 ADD CONSTRAINT template_company_FK FOREIGN KEY (company_id) REFERENCES company(id);
ALTER TABLE template2 ADD CONSTRAINT template_member_FK FOREIGN KEY (update_member_id) REFERENCES `member`(id) ON DELETE SET NULL;
ALTER TABLE template2 ADD CONSTRAINT template_category_small_FK FOREIGN KEY (category_small_id) REFERENCES category_small(id) ON DELETE CASCADE;
ALTER TABLE template2 ADD CONSTRAINT template_member_FK_1 FOREIGN KEY (member_id) REFERENCES `member`(id) ON DELETE SET NULL;

-- auto_message
ALTER TABLE auto_message ADD CONSTRAINT auto_message_company_FK FOREIGN KEY (company_id) REFERENCES company(id);
ALTER TABLE auto_message ADD CONSTRAINT auto_message_member_FK FOREIGN KEY (update_member_id) REFERENCES `member`(id) ON DELETE SET NULL;

-- template_favorite
ALTER TABLE template_favorite ADD CONSTRAINT template_favorite_company_FK FOREIGN KEY (company_id) REFERENCES company(id);
ALTER TABLE template_favorite ADD CONSTRAINT template_favorite_member_FK FOREIGN KEY (member_id) REFERENCES `member`(id) ON DELETE CASCADE;
ALTER TABLE template_favorite ADD CONSTRAINT template_favorite_template_FK FOREIGN KEY (template_id) REFERENCES template2(id) ON DELETE CASCADE;
CREATE UNIQUE INDEX template_favorite_member_id_IDX USING BTREE ON template_favorite (member_id, template_id);

-- template_keyword
ALTER TABLE template_keyword ADD CONSTRAINT template_keyword_member_FK FOREIGN KEY (update_member_id) REFERENCES `member`(id) ON DELETE SET NULL;
ALTER TABLE template_keyword ADD CONSTRAINT template_keyword_keyword_FK FOREIGN KEY (keyword_id) REFERENCES keyword2(id) ON DELETE CASCADE;
ALTER TABLE template_keyword ADD CONSTRAINT template_keyword_template_FK FOREIGN KEY (template_id) REFERENCES template2(id) ON DELETE CASCADE;
ALTER TABLE template_keyword ADD CONSTRAINT template_keyword_company_FK FOREIGN KEY (company_id) REFERENCES company(id);
CREATE UNIQUE INDEX template_keyword_template_id_IDX USING BTREE ON template_keyword (template_id, keyword_id);

-- room
ALTER TABLE room ADD CONSTRAINT room_company_FK FOREIGN KEY (company_id) REFERENCES company(id);
ALTER TABLE room ADD CONSTRAINT room_member_FK FOREIGN KEY (update_member_id) REFERENCES `member`(id) ON DELETE SET NULL;
ALTER TABLE room ADD CONSTRAINT room_member_FK_1 FOREIGN KEY (member_id) REFERENCES `member`(id) ON DELETE SET NULL;
ALTER TABLE room ADD CONSTRAINT room_chat_message_FK FOREIGN KEY (join_message_id) REFERENCES chat_message(id) ON DELETE SET NULL;
ALTER TABLE room ADD CONSTRAINT room_member_FK_2 FOREIGN KEY (last_member_id) REFERENCES `member`(id) ON DELETE SET NULL;

-- room_join_history
ALTER TABLE room_join_history ADD CONSTRAINT room_join_history_member_FK FOREIGN KEY (member_id) REFERENCES `member`(id) ON DELETE SET NULL;
ALTER TABLE room_join_history ADD CONSTRAINT room_join_history_room_FK FOREIGN KEY (room_id) REFERENCES room(id) ON DELETE CASCADE;
ALTER TABLE room_join_history ADD CONSTRAINT room_join_history_company_FK FOREIGN KEY (company_id) REFERENCES company(id);
ALTER TABLE room_join_history ADD CONSTRAINT room_join_history_category_small_FK FOREIGN KEY (category_small_id) REFERENCES category_small(id) ON DELETE SET NULL;
ALTER TABLE room_join_history ADD CONSTRAINT room_join_history_member_FK_1 FOREIGN KEY (last_member_id) REFERENCES `member`(id) ON DELETE SET NULL;
ALTER TABLE room_join_history ADD CONSTRAINT room_join_history_member_FK_2 FOREIGN KEY (update_member_id) REFERENCES `member`(id) ON DELETE SET NULL;

-- speaker2
ALTER TABLE speaker2 ADD CONSTRAINT speaker_company_FK FOREIGN KEY (company_id) REFERENCES company(id);
ALTER TABLE speaker2 ADD CONSTRAINT speaker_member_FK FOREIGN KEY (update_member_id) REFERENCES `member`(id) ON DELETE SET NULL;

-- chat_message
ALTER TABLE chat_message ADD CONSTRAINT chat_message_speaker_FK FOREIGN KEY (speaker_id) REFERENCES speaker2(id);
ALTER TABLE chat_message ADD CONSTRAINT chat_message_room_FK FOREIGN KEY (room_id) REFERENCES room(id);
ALTER TABLE chat_message ADD CONSTRAINT chat_message_company_FK FOREIGN KEY (company_id) REFERENCES company(id);
ALTER TABLE chat_message ADD CONSTRAINT chat_message_template_FK FOREIGN KEY (template_id) REFERENCES template2(id) ON DELETE SET NULL;

-- room_speaker
ALTER TABLE room_speaker ADD CONSTRAINT room_speaker_company_FK FOREIGN KEY (company_id) REFERENCES company(id);
ALTER TABLE room_speaker ADD CONSTRAINT room_speaker_speaker_FK FOREIGN KEY (speaker_id) REFERENCES speaker2(id);
ALTER TABLE room_speaker ADD CONSTRAINT room_speaker_room_FK FOREIGN KEY (room_id) REFERENCES room(id);
ALTER TABLE room_speaker ADD CONSTRAINT room_speaker_member_FK FOREIGN KEY (update_member_id) REFERENCES `member`(id) ON DELETE SET NULL;
CREATE UNIQUE INDEX room_speaker_room_id_IDX USING BTREE ON room_speaker (room_id, speaker_id);

-- message_read
ALTER TABLE message_read ADD CONSTRAINT message_read_company_FK FOREIGN KEY (company_id) REFERENCES company(id);
ALTER TABLE message_read ADD CONSTRAINT message_read_room_FK FOREIGN KEY (room_id) REFERENCES room(id) ON DELETE CASCADE;
ALTER TABLE message_read ADD CONSTRAINT message_read_chat_message_FK FOREIGN KEY (message_id) REFERENCES chat_message(id) ON DELETE CASCADE;
ALTER TABLE message_read ADD CONSTRAINT message_read_speaker_FK FOREIGN KEY (speaker_id) REFERENCES speaker2(id) ON DELETE CASCADE;
CREATE INDEX message_read_room_id_IDX USING BTREE ON message_read (room_id, message_id, speaker_id);

-- minwon_history
ALTER TABLE minwon_history ADD CONSTRAINT minwon_history_member_FK FOREIGN KEY (update_member_id) REFERENCES `member`(id) ON DELETE SET NULL;
ALTER TABLE minwon_history ADD CONSTRAINT minwon_history_company_FK FOREIGN KEY (company_id) REFERENCES company(id);
ALTER TABLE minwon_history ADD CONSTRAINT minwon_history_category_small_FK FOREIGN KEY (category_small_id) REFERENCES category_small(id);
ALTER TABLE minwon_history ADD CONSTRAINT minwon_history_room_FK FOREIGN KEY (room_id) REFERENCES room(id);

-- link_menu
ALTER TABLE link_menu ADD CONSTRAINT link_menu_company_FK FOREIGN KEY (company_id) REFERENCES company(id);
ALTER TABLE link_menu ADD CONSTRAINT link_menu_member_FK FOREIGN KEY (update_member_id) REFERENCES `member`(id) ON DELETE SET NULL;

-- link_detail
ALTER TABLE link_detail ADD CONSTRAINT link_detail_company_FK FOREIGN KEY (company_id) REFERENCES company(id);
ALTER TABLE link_detail ADD CONSTRAINT link_detail_member_FK FOREIGN KEY (update_member_id) REFERENCES `member`(id) ON DELETE SET NULL;
ALTER TABLE link_detail ADD CONSTRAINT link_detail_link_menu_FK FOREIGN KEY (menu_id) REFERENCES link_menu(id);

-- template_use_history
ALTER TABLE template_use_history ADD CONSTRAINT template_use_history_company_FK FOREIGN KEY (company_id) REFERENCES company(id);
ALTER TABLE template_use_history ADD CONSTRAINT template_use_history_member_FK FOREIGN KEY (update_member_id) REFERENCES `member`(id) ON DELETE SET NULL;
ALTER TABLE template_use_history ADD CONSTRAINT template_use_history_room_FK FOREIGN KEY (room_id) REFERENCES room(id) ON DELETE SET NULL;
ALTER TABLE template_use_history ADD CONSTRAINT template_use_history_template_FK FOREIGN KEY (template_id) REFERENCES template2(id) ON DELETE CASCADE;

-- stats_company
ALTER TABLE stats_company ADD CONSTRAINT stats_company_company_FK FOREIGN KEY (company_id) REFERENCES company(id);

-- stats_member
ALTER TABLE stats_member ADD CONSTRAINT stats_member_company_FK FOREIGN KEY (company_id) REFERENCES company(id);
ALTER TABLE stats_member ADD CONSTRAINT stats_member_member_FK FOREIGN KEY (member_id) REFERENCES `member`(id) ON DELETE SET NULL;

-- stats_hashtag
ALTER TABLE stats_hashtag ADD CONSTRAINT stats_hashtag_company_FK FOREIGN KEY (company_id) REFERENCES company(id);

-- alarm_member
ALTER TABLE alarm_member ADD CONSTRAINT alarm_member_company_FK FOREIGN KEY (company_id) REFERENCES company(id);
ALTER TABLE alarm_member ADD CONSTRAINT alarm_member_member_FK FOREIGN KEY (member_id) REFERENCES `member`(id) ON DELETE SET NULL;

-- action_history
ALTER TABLE action_history ADD CONSTRAINT action_history_company_FK FOREIGN KEY (company_id) REFERENCES company(id);
ALTER TABLE action_history ADD CONSTRAINT action_history_member_FK FOREIGN KEY (member_id) REFERENCES `member`(id) ON DELETE SET NULL;


-- 매뉴얼 이미지경로 일괄변경.
update manual
set pdf_image_path = concat('https://cstalk.gasapp.co.kr/attach/manual',pdf_image_path) 
where 1=1
and pdf_image_path like '/static/images/%'

*/


/*

   -- smigration check query
   select *
    from chat_message
    where speaker_id in(
    select speaker_id
    from (
      select speaker_id
      from chat_message
      where speaker_id not in(select id from speaker2) 
      ) as a
    )

*/

/*

메시지 읽음 처리

-- 마지막 메시지까지 모두 읽음 처리
        UPDATE chat_message 
           SET not_read_count = not_read_count - 1
         WHERE room_id = _room_id AND id <= IFNULL(v_max_message_id, 0);
        -- where room_id = _room_id and id between IFNULL(v_read_last_message_id, 0) and IFNULL(v_max_message_id, 0);

-- 사용자의 메시지 읽음 처리 하기
UPDATE message_read
    SET read_date = now()
  WHERE room_id = :room_id AND speaker_id = :speaker_id AND message_id <= IFNULL(:message_id, 0);

-- 방에 사용자가 정보가 존재하는 경우 : 마지막 읽은 메시지 최신화
UPDATE room_speaker
    SET old_last_message_id = read_last_message_id, read_last_message_id = v_max_message_id
  WHERE room_id = _room_id and speaker_id = _speaker_id;

*/

/*


1.chat_message의 join_message_id
 -join_message_id 값이 null 인 경우를 데이터 정립화할 필요가 있음

2.room_join_history의 start_message_id, end_message_id
 -start_message_id, end_message_id 값이 null 인 경우를 데이터 정립화할 필요가 있음

3.room_speaker의 read_last_message_id, old_last_message_id
 -read_last_message_id, old_last_message_id 값이 null 인 경우를 데이터 정립화할 필요가 있음


*/
