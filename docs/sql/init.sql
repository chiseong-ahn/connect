-- fk 제거
select concat('alter table ', TABLE_NAME, ' drop foreign key ', constraint_name, ';') 
from information_schema.table_constraints
where CONSTRAINT_SCHEMA = 'cstalk'
and CONSTRAINT_TYPE = 'FOREIGN KEY'
order by table_name;

-- index 제거
select concat('alter table ', TABLE_NAME, ' drop INDEX ', constraint_name, ';') 
from information_schema.table_constraints
where CONSTRAINT_SCHEMA = 'cstalk'
and CONSTRAINT_TYPE != 'FOREIGN KEY'
and CONSTRAINT_NAME != 'PRIMARY'
order by table_name;

alter table action_history drop foreign key action_history_member_FK;
alter table action_history drop foreign key action_history_company_FK;
alter table alarm_member drop foreign key alarm_member_member_FK;
alter table alarm_member drop foreign key alarm_member_company_FK;
alter table auto_message drop foreign key auto_message_member_FK;
alter table auto_message drop foreign key auto_message_company_FK;
alter table category_large drop foreign key category_large_company_FK;
alter table category_large drop foreign key category_large_member_FK;
alter table category_middle drop foreign key category_middle_category_large_FK;
alter table category_middle drop foreign key category_middle_company_FK;
alter table category_middle drop foreign key category_middle_member_FK;
alter table category_small drop foreign key category_small_category_middle_FK;
alter table category_small drop foreign key category_small_company_FK;
alter table category_small drop foreign key category_small_member_FK;
alter table chat_message drop foreign key chat_message_company_FK;
alter table chat_message drop foreign key chat_message_template_FK;
alter table chat_message drop foreign key chat_message_speaker_FK;
alter table chat_message drop foreign key chat_message_room_FK;
alter table company drop foreign key company_member_FK;
alter table customer2 drop foreign key customer_member_FK;
alter table customer2 drop foreign key customer_speaker_FK;
alter table customer_company drop foreign key customer_company_speaker_FK;
alter table customer_company drop foreign key customer_company_room_FK;
alter table customer_company drop foreign key customer_company_member_FK;
alter table customer_company drop foreign key customer_company_customer_FK;
alter table customer_company drop foreign key customer_company_company_FK;
alter table customer_company drop foreign key customer_company_member_FK_1;
alter table keyword2 drop foreign key keyword_company_FK;
alter table keyword2 drop foreign key keyword_member_FK;
alter table link_detail drop foreign key link_detail_company_FK;
alter table link_detail drop foreign key link_detail_link_menu_FK;
alter table link_detail drop foreign key link_detail_member_FK;
alter table link_menu drop foreign key link_menu_company_FK;
alter table link_menu drop foreign key link_menu_member_FK;
alter table manual drop foreign key manual_company_FK;
alter table manual drop foreign key manual_member_FK;
alter table manual_favorite drop foreign key manual_favorite_company_FK;
alter table manual_favorite drop foreign key manual_favorite_manual_FK;
alter table manual_favorite drop foreign key manual_favorite_member_FK;
alter table member drop foreign key member_company_FK;
alter table member drop foreign key member_member_FK;
alter table member drop foreign key member_speaker_FK;
alter table message_read drop foreign key message_read_speaker_FK;
alter table message_read drop foreign key message_read_room_FK;
alter table message_read drop foreign key message_read_chat_message_FK;
alter table message_read drop foreign key message_read_company_FK;
alter table minwon_history drop foreign key minwon_history_category_small_FK;
alter table minwon_history drop foreign key minwon_history_company_FK;
alter table minwon_history drop foreign key minwon_history_member_FK;
alter table minwon_history drop foreign key minwon_history_room_FK;
alter table room drop foreign key room_member_FK_2;
alter table room drop foreign key room_member_FK_1;
alter table room drop foreign key room_member_FK;
alter table room drop foreign key room_company_FK;
alter table room drop foreign key room_chat_message_FK;
alter table room_join_history drop foreign key room_join_history_category_small_FK;
alter table room_join_history drop foreign key room_join_history_company_FK;
alter table room_join_history drop foreign key room_join_history_member_FK;
alter table room_join_history drop foreign key room_join_history_member_FK_1;
alter table room_join_history drop foreign key room_join_history_member_FK_2;
alter table room_join_history drop foreign key room_join_history_room_FK;
alter table room_speaker drop foreign key room_speaker_speaker_FK;
alter table room_speaker drop foreign key room_speaker_room_FK;
alter table room_speaker drop foreign key room_speaker_company_FK;
alter table room_speaker drop foreign key room_speaker_member_FK;
alter table speaker2 drop foreign key speaker_company_FK;
alter table speaker2 drop foreign key speaker_member_FK;
alter table stats_company drop foreign key stats_company_company_FK;
alter table stats_hashtag drop foreign key stats_hashtag_company_FK;
alter table stats_member drop foreign key stats_member_company_FK;
alter table stats_member drop foreign key stats_member_member_FK;
alter table template2 drop foreign key template_member_FK_1;
alter table template2 drop foreign key template_member_FK;
alter table template2 drop foreign key template_company_FK;
alter table template2 drop foreign key template_category_small_FK;
alter table template_favorite drop foreign key template_favorite_company_FK;
alter table template_favorite drop foreign key template_favorite_member_FK;
alter table template_favorite drop foreign key template_favorite_template_FK;
alter table template_keyword drop foreign key template_keyword_company_FK;
alter table template_keyword drop foreign key template_keyword_keyword_FK;
alter table template_keyword drop foreign key template_keyword_member_FK;
alter table template_keyword drop foreign key template_keyword_template_FK;
alter table template_use_history drop foreign key template_use_history_company_FK;
alter table template_use_history drop foreign key template_use_history_member_FK;
alter table template_use_history drop foreign key template_use_history_room_FK;
alter table template_use_history drop foreign key template_use_history_template_FK;

-- index 삭제
alter table customer2 DROP INDEX customer_gasapp_member_number_IDX;
alter table customer_company drop INDEX customer_company_customer_id_IDX;
alter table keyword2 drop INDEX keyword_company_id_IDX;
alter table manual drop INDEX manual_company_id_IDX;
alter table manual_favorite drop INDEX manual_favorite_member_id_IDX;
alter table room_speaker drop INDEX room_speaker_room_id_IDX;
alter table template_favorite drop INDEX template_favorite_member_id_IDX;
alter table template_keyword drop INDEX template_keyword_template_id_IDX;
alter table room drop INDEX room_state_IDX;


-- table truncate
TRUNCATE TABLE company;
TRUNCATE TABLE member;
TRUNCATE TABLE category_large;
TRUNCATE TABLE category_middle;
TRUNCATE TABLE category_small;
TRUNCATE TABLE customer2;
TRUNCATE TABLE customer_company;
TRUNCATE TABLE keyword2;
TRUNCATE TABLE manual;
TRUNCATE TABLE manual_favorite;
TRUNCATE TABLE template2;
TRUNCATE TABLE auto_message;
TRUNCATE TABLE template_favorite;
TRUNCATE TABLE template_keyword;
TRUNCATE TABLE room;
TRUNCATE TABLE room_join_history;
TRUNCATE TABLE speaker2;
TRUNCATE TABLE chat_message;
TRUNCATE TABLE room_speaker;
TRUNCATE TABLE message_read;
TRUNCATE TABLE minwon_history;
TRUNCATE TABLE link_menu;
TRUNCATE TABLE link_detail;
TRUNCATE TABLE template_use_history;
TRUNCATE TABLE stats_company;
TRUNCATE TABLE stats_member;
TRUNCATE TABLE stats_hashtag;
TRUNCATE TABLE alarm_member;
TRUNCATE TABLE action_history;
TRUNCATE TABLE talk_review;