-- 고객 view
CREATE
OR REPLACE
VIEW `v_customer` AS SELECT
  `customer_company`.`id` AS `id`, 
  `customer2`.`id` AS `customer_id`, 
  `customer2`.`gasapp_member_number` AS `gasapp_member_number`, 
  `customer2`.`name` AS `name`, 
  `customer2`.`tel_number` AS `tel_number`, 
  `customer_company`.`create_date` AS `create_date`, 
  `customer_company`.`update_date` AS `update_date`, 
  `customer_company`.`update_member_id` AS `update_member_id`, 
  `customer_company`.`company_id` AS `company_id`, 
  `customer_company`.`is_block` AS `is_block`, 
  `customer_company`.`block_type` AS `block_type`, 
  `customer_company`.`remark` AS `remark`, 
  `customer_company`.`block_member_id` AS `block_member_id`, 
  `customer_company`.`room_id` AS `room_id`, 
  `customer_company`.`speaker_id` AS `speaker_id`, 
  `customer_company`.`swear_count` AS `swear_count`, 
  `customer_company`.`insult_count` AS `insult_count`, 
  `customer_company`.`state` AS `state`, 
  `customer_company`.`block_date` AS `block_date` 
FROM 
  (
    `customer2` 
    JOIN `customer_company` ON(
      `customer2`.`id` = `customer_company`.`customer_id`
    )
  );

-- 방 view
CREATE
OR REPLACE
VIEW `v_room` AS SELECT 
  `room`.`id` AS `id`, 
  `room`.`create_date` AS `create_date`, 
  `room`.`update_date` AS `update_date`, 
  `room`.`update_member_id` AS `update_member_id`, 
  `room`.`member_id` AS `member_id`, 
  `room`.`company_id` AS `company_id`, 
  `room`.`state` AS `state`, 
  `room`.`join_message_id` AS `join_message_id`, 
  `room`.`chatid` AS `chatid`, 
  `room`.`join_history_json` AS `join_history_json`, 
  `room`.`is_online` AS `is_online`, 
  `room`.`name` AS `name`, 
  `room`.`end_date` AS `end_date`, 
  `room`.`last_member_id` AS `last_member_id`, 
  (
    SELECT 
      `speaker2`.`id` 
    FROM 
      (
        `speaker2` 
        JOIN `room_speaker` ON(
          `speaker2`.`id` = `room_speaker`.`speaker_id`
        )
      ) 
    WHERE 
      `room_speaker`.`room_id` = `room`.`id` 
      AND `room_speaker`.`is_customer` = 1
  ) AS `customer_speaker_id`, 
  (
    SELECT 
      `member`.`speaker_id` 
    FROM 
      `member` 
    WHERE 
      `member`.`id` = `room`.`member_id`
  ) AS `member_speaker_id` 
FROM 
  `room`;

-- 카테고리 view
CREATE OR REPLACE VIEW `v_category`
AS
  select `s`.`company_id`        AS `company_id`,
  			`l`.`id`         AS `category_large_id`,
         `l`.`name`       AS `category_large_name`,
         `m`.`name`       AS `category_middle_name`,
         `s`.`id`         AS `id`,
         `s`.`name`       AS `name`,
         `s`.`create_date` AS `create_date`,
         `s`.`update_date`   AS `update_date`,
         `s`.`category_middle_id`     AS `category_middle_id`,
         `s`.`update_member_id`        AS `member_id`,
         `s`.`minwon_code`     AS `minwon_code`,
         `s`.`minwon_name`     AS `minwon_name`,
         `s`.`sort_index`     AS `sort_index`,
         `s`.`use_status`     AS `use_status`
  from   ((`category_small` `s`
           join `category_middle` `m`
             on (( `s`.`category_middle_id` = `m`.`id` )))
          join `category_large` `l`
            on (( `m`.`category_large_id` = `l`.`id` ))) 
