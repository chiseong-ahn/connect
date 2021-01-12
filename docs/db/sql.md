DROP PROCEDURE IF EXISTS cstalk.regist_member;

DELIMITER $$
$$
CREATE PROCEDURE cstalk.stats_company_daily(
	IN _company_id VARCHAR(255),		-- 회사id (1-서울도시가스, 2-인천도시가스, ...)
	IN _target_date VARCHAR(10)			-- 집계대상 일자(YYYY-MM-DD)
)
	BEGIN
		
		-- 이미 집계된 데이터가 존재하는지 확인.
		IF (		
			SELECT sc.id 
			FROM stats_company sc 
			WHERE 1=1
			AND sc.save_date = _target_date
			LIMIT 1
		) THEN
		
			-- 이미 집계된 데이터가 존재할 경우 : 등록되어 있는 데이터를 삭제 한다.
			DELETE 
			FROM stats_company
			WHERE save_date = _target_date;
		
		END IF;
		
		
		-- 집계 및 등록.
		INSERT INTO stats_company (
			company_id, save_date, chatbot_use_count, talk_system_enter_count, new_count, 
			ready_count, ing_count, close_count, out_count, speak_count, 
			max_ready_minute, max_speak_minute, avg_ready_minute, avg_speak_minute, avg_member_speak_count
		)
		SELECT  '1' as companyId
				,_target_date as saveDate
				,sum(a.isUseChatbot) as useChatbotCount		-- 챗봇을 이용한 유입한 카운트.
				,(
					SELECT count(*)
					FROM(
						SELECT distinct(cm.speaker_id)
						FROM chat_message cm 
						WHERE 1=1
						AND cm.company_id = '1' 
						AND DATE_FORMAT(cm.create_date, '%Y-%m-%d') = _target_date
						AND cm.is_system_message = 0
						AND cm.is_employee = 0
					)a
				) as talkSystemEnterCount					-- 채팅상담 인입 고객 (해당일에 메세지 작성이력이 있는 고객)
				,sum(a.isNew) as newCount					-- 신규상담 카운트.
				,sum(a.isReady) as readyCount				-- 대기상담 카운트. (애매~~~)
				,sum(a.isIng) as ingCount					-- 진행상담 카운트. (애매~~~)
				,sum(a.isClose) as closeCount				-- 종료상담 카운트.
				,0 as outCount								-- 이탈 카운트.
				,ifnull((
					SELECT count(*)
					FROM room r 
					WHERE 1=1
					AND DATE_FORMAT(r.update_date, '%Y-%m-%d') = _target_date
				), 0) as speakCount								-- 총 상담 건수.
				,ifnull(round(max(a.readyTime / 60)), 0) as maxReadyMinute			-- 최대 대기시간.
				,ifnull(round(max(a.speakTime / 60)), 0) as maxSpeakMinute			-- 최대 상담시간.
				,ifnull(round(avg(a.readyTime) / 60), 0) as avgReadyMinute	-- 평균 대기시간.
				,ifnull(round(avg(a.speakTime) / 60), 0) as avgSpeakMinute	-- 평균 상담시간.
				,ifnull((
					SELECT round(avg(a2.speak_count))
					FROM(
						SELECT member_id, count(*) as speak_count
						FROM room_join_history rjh
						WHERE 1=1
						AND company_id = '1'
						AND DATE_FORMAT(rjh.create_date, '%Y-%m-%d') = _target_date
						AND member_id is not null
						GROUP BY member_id
					)a2
				), 0) as avgMemberSpeakCount					-- 상담사별 평균 상담 건수.
		FROM(
				SELECT *
						,TIMESTAMPDIFF(SECOND, a.joinMessageTime, a.firstMemberMessageTime) as readyTime	-- 대기시간
				FROM(
						SELECT a.id, a.create_date, a.member_id, a.company_id, a.state, a.join_message_id, a.end_date
								,(
									SELECT cm.create_date 
									FROM chat_message cm 
									WHERE 1=1
									AND cm.company_id = a.company_id 
									AND cm.room_id = a.id
									AND cm.id = CASE WHEN a.state < 8 THEN a.join_message_id ELSE (
										SELECT rjh2.start_message_id
										FROM room_join_history rjh2
										WHERE 1=1
										AND rjh2.company_id = a.company_id 
										AND rjh2.room_id = a.id
										AND rjh2.id = a.chatid 
									) END -- 종료된 상담일 경우 history 테이블의 시작시간을 기준으로 추출.
								) as joinMessageTime		-- 조인 메시지 작성시간.
								,(
									SELECT cm.create_date 
									FROM chat_message cm 
									WHERE 1=1
									AND cm.company_id = a.company_id 
									AND cm.room_id = a.id
									AND cm.id > CASE WHEN a.state < 8 THEN a.join_message_id ELSE (
										SELECT rjh2.start_message_id
										FROM room_join_history rjh2
										WHERE 1=1
										AND rjh2.company_id = a.company_id 
										AND rjh2.room_id = a.id
										AND rjh2.id = a.chatid 
									) END
									AND cm.is_system_message = 0
									AND cm.is_employee = 1
									limit 1
								) as firstMemberMessageTime	-- 상담사의 처음 메시지 작성시간.
								,CASE WHEN DATE_FORMAT(a.create_date, '%Y-%m-%d') = _target_date AND a.join_history_json is not null THEN 1 ELSE 0 END as isUseChatbot -- 챗봇이용 여부.
								,CASE WHEN a.member_id is null AND a.state < 2 THEN 1 ELSE 0 END as isReady	-- 대기상담 여부.
								,CASE WHEN DATE_FORMAT(a.create_date, '%Y-%m-%d') = _target_date THEN 1 ELSE 0 END isNew		-- 신규상담 여부.
								,CASE WHEN a.state < 2 THEN 1 ELSE 0 END as isIng		-- 진행상담 여부. 
								,CASE WHEN (
									SELECT count(*)
									FROM room_join_history rjh 
									WHERE 1=1
									AND rjh.company_id = a.company_id
									AND rjh.room_id = a.id
									AND DATE_FORMAT(rjh.end_date, '%Y-%m-%d') = _target_date
								) > 0 THEN 1 ELSE 0 END as isClose -- 종료상담 여부.
								,CASE WHEN DATE_FORMAT(a.update_date, '%Y-%m-%d') = _target_date THEN (
									ifnull((
										SELECT CASE WHEN a.state < 8 or rjh2.end_date is null THEN 0 ELSE TIMESTAMPDIFF(SECOND, rjh2.create_date, rjh2.end_date) END
										FROM room_join_history rjh2 
										WHERE 1=1
										AND rjh2.company_id = a.company_id 
										AND rjh2.room_id = a.id
										AND rjh2.id = a.chatid 
									), 0)) ELSE 0 
								END as speakTime	-- 상담시간 (종료시간 - 시작시간)
						FROM room As a
						WHERE 1=1
						AND a.company_id = '1'
				)a
		)a;
		
	END$$
DELIMITER ;