-- create the credit module schema
CREATE DATABASE IF NOT EXISTS `credit_module` DEFAULT CHARACTER SET utf8;

USE `credit_module`;

-- `api_user`
CREATE TABLE `api_user` (
  `username` varchar(45) NOT NULL,
  `password` varchar(200) NOT NULL,
  `incoming_channel` varchar(200) NOT NULL,
  PRIMARY KEY (`username`)
) ENGINE=TokuDB DEFAULT CHARSET=utf8;

-- ` bu_master `
CREATE TABLE `bu_master` (
  `uid` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  `gl_code` varchar(50) DEFAULT NULL,
  `version` bigint(20) unsigned DEFAULT NULL,
  `modified_by` varchar(75) DEFAULT NULL,
  `modified_date` datetime DEFAULT NULL,
  `created_by` varchar(75) DEFAULT NULL,
  `created_date` datetime DEFAULT NULL,
  PRIMARY KEY (`uid`),
  UNIQUE KEY `uid_UNIQUE` (`uid`)
) ENGINE=TokuDB DEFAULT CHARSET=utf8;

CREATE TABLE `price_book` (
  `uid` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `service_family` varchar(50) DEFAULT NULL,
  `bu_name` varchar(50) DEFAULT NULL,
  `service_name` varchar(50) DEFAULT NULL,
  `short_name` varchar(50) DEFAULT NULL,
  `category_no` varchar(10) DEFAULT NULL,
  `category_name` varchar(100) DEFAULT NULL,
  `part_no` varchar(10) DEFAULT NULL,
  `product_name` varchar(100) DEFAULT NULL,
  `currency_code` varchar(4) DEFAULT NULL,
  `one_off_price` int(11) DEFAULT NULL,
  `recurring_price` int(11) DEFAULT NULL,
  `service_status` varchar(10) DEFAULT NULL,
  `category_status` varchar(10) DEFAULT NULL,
  `product_status` varchar(10) DEFAULT NULL,
  `pricebook_status` varchar(10) DEFAULT NULL,
  `gl_code` varchar(10) DEFAULT NULL,
  `provider` varchar(10) DEFAULT NULL,
  `provider_rate` decimal(10,3) DEFAULT NULL,
  `effective_date` datetime DEFAULT NULL,
  `version` bigint(20) unsigned DEFAULT NULL,
  `modified_by` varchar(75) DEFAULT NULL,
  `modified_date` datetime DEFAULT NULL,
  `created_by` varchar(75) DEFAULT NULL,
  `created_date` datetime DEFAULT NULL,
  PRIMARY KEY (`uid`),
  UNIQUE KEY `uid_UNIQUE` (`uid`)
) ENGINE=TokuDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;

-- ` charge_item `
CREATE TABLE `charge_item` (
  `uid` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `charge_item_id` varchar(50) DEFAULT NULL,
  `transaction_id` varchar(50) DEFAULT NULL,
  `payment_id` varchar(50) DEFAULT NULL,
  `quantity` int(11) DEFAULT NULL,
  `total_amount` int(11) DEFAULT NULL,
  `part_no` varchar(50) DEFAULT NULL,
  `product_name` varchar(200) DEFAULT NULL,
  `charge_type` varchar(10) DEFAULT NULL,
  `created_by` varchar(75) DEFAULT NULL,
  `created_date` datetime DEFAULT NULL,
  `version` int(11) NOT NULL DEFAULT '1',
  PRIMARY KEY (`uid`),
  UNIQUE KEY `uid_UNIQUE` (`uid`)
) ENGINE=TokuDB DEFAULT CHARSET=utf8;

-- ` contract `
CREATE TABLE `contract` (
  `uid` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `contract_id` varchar(50) DEFAULT NULL,
  `wallet_id` varchar(50) DEFAULT NULL,
  `ref_number` varchar(50) DEFAULT NULL,
  `type` varchar(20) DEFAULT NULL,
  `company_code` varchar(10) DEFAULT NULL,
  `amount` int(11) DEFAULT NULL,
  `currency_amount` decimal(20,8) DEFAULT NULL,
  `balance` int(11) DEFAULT NULL,
  `currency_balance` decimal(20,8) DEFAULT NULL,
  `currency_rate_id` int(11) DEFAULT NULL,
  `exchange_rate` decimal(13,8) DEFAULT NULL,
  `description` varchar(600) DEFAULT NULL,
  `status` varchar(20) DEFAULT NULL,
  `last_reopen_date` datetime DEFAULT NULL,
  `is_forfeited` tinyint(1) DEFAULT '0',
  `forfeit_amount` int(11) DEFAULT '0',
  `forfeit_currency_amount` decimal(20,8) DEFAULT '0.00000000',
  `is_all_bu` tinyint(1) DEFAULT '0',
  `is_all_product` tinyint(1) DEFAULT '0',
  `contract_effective_date` date DEFAULT NULL,
  `contract_termination_date` date DEFAULT NULL,
  `credit_expiry_date` date DEFAULT NULL,
  `last_recharge_date` datetime DEFAULT NULL,
  `terminated_reason` varchar(600) DEFAULT NULL,
  `transaction_date` datetime DEFAULT NULL,
  `version` bigint(20) unsigned DEFAULT NULL,
  `modified_by` varchar(75) DEFAULT NULL,
  `modified_date` datetime DEFAULT NULL,
  `created_by` varchar(75) DEFAULT NULL,
  `created_date` datetime DEFAULT NULL,
  PRIMARY KEY (`uid`),
  UNIQUE KEY `uid_UNIQUE` (`uid`)
) ENGINE=TokuDB DEFAULT CHARSET=utf8;

-- ` contract_to_bu_master `
CREATE TABLE `contract_to_bu_master` (
  `uid` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `contract_id` varchar(45) DEFAULT NULL,
  `bu_name` varchar(45) DEFAULT NULL,
  `created_by` varchar(45) DEFAULT NULL,
  `created_date` datetime DEFAULT NULL,
  PRIMARY KEY (`uid`),
  UNIQUE KEY `uid_UNIQUE` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ` contract_to_price_book `
CREATE TABLE `contract_to_price_book` (
  `uid` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `contract_id` varchar(45) DEFAULT NULL,
  `part_no` varchar(10) DEFAULT NULL,
  `created_by` varchar(45) DEFAULT NULL,
  `created_date` datetime DEFAULT NULL,
  PRIMARY KEY (`uid`),
  UNIQUE KEY `uid_UNIQUE` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


-- ` currency_rate `
CREATE TABLE `currency_rate` (
  `currency_rate_id` int(11) NOT NULL AUTO_INCREMENT,
  `from_currency_code` varchar(5) NOT NULL,
  `to_currency_code` varchar(45) NOT NULL,
  `exchange_rate` decimal(13,8) NOT NULL,
  PRIMARY KEY (`currency_rate_id`),
  UNIQUE KEY `currency_rate_id_UNIQUE` (`currency_rate_id`)
) ENGINE=TokuDB DEFAULT CHARSET=utf8;

-- ` map_charge_item_to_contract `
CREATE TABLE `map_charge_item_to_contract` (
  `uid` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `charge_item_id` varchar(45) DEFAULT NULL,
  `contract_id` varchar(45) DEFAULT NULL,
  `amount` int(11) DEFAULT NULL,
  `created_by` varchar(45) DEFAULT NULL,
  `created_date` datetime DEFAULT NULL,
  PRIMARY KEY (`uid`),
  UNIQUE KEY `uid_UNIQUE` (`uid`)
) ENGINE=TokuDB DEFAULT CHARSET=utf8;

-- ` payment `
CREATE TABLE `payment` (
  `uid` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `payment_id` varchar(50) DEFAULT NULL,
  `wallet_id` varchar(50) DEFAULT NULL,
  `organization_id` varchar(45) DEFAULT NULL,
  `total_amount` int(11) DEFAULT NULL,
  `description` varchar(200) DEFAULT NULL,
  `expired_date` datetime DEFAULT NULL,
  `status` varchar(20) DEFAULT NULL,
  `version` bigint(20) DEFAULT NULL,
  `modified_by` varchar(75) DEFAULT NULL,
  `modified_date` datetime DEFAULT NULL,
  `created_by` varchar(75) DEFAULT NULL,
  `created_date` datetime DEFAULT NULL,
  PRIMARY KEY (`uid`),
  UNIQUE KEY `uid_UNIQUE` (`uid`),
  UNIQUE KEY `index_paymentid` (`payment_id`)
) ENGINE=TokuDB DEFAULT CHARSET=utf8;

-- ` report `
CREATE TABLE `report` (
  `uid` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `report_date` datetime DEFAULT NULL,
  `report_file` blob,
  `version` bigint(20) unsigned DEFAULT NULL,
  `modified_by` varchar(75) DEFAULT NULL,
  `modified_date` datetime DEFAULT NULL,
  `created_by` varchar(75) DEFAULT NULL,
  `created_date` datetime DEFAULT NULL,
  PRIMARY KEY (`uid`),
  UNIQUE KEY `uid_UNIQUE` (`uid`)
) ENGINE=TokuDB DEFAULT CHARSET=utf8;

--` tnc_template `
CREATE TABLE `tnc_template` (
  `uid` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `uploaded_name` varchar(255) DEFAULT NULL,
  `file_name` varchar(50) DEFAULT NULL,
  `template_name` varchar(50) DEFAULT NULL,
  `description` varchar(200) DEFAULT NULL,
  `bu_name` varchar(50) DEFAULT NULL,
  `is_default` tinyint(1) DEFAULT NULL,
  `is_visible` tinyint(1) DEFAULT NULL,
  `version` bigint(20) unsigned DEFAULT NULL,
  `modified_by` varchar(75) DEFAULT NULL,
  `modified_date` datetime DEFAULT NULL,
  `created_by` varchar(75) DEFAULT NULL,
  `created_date` datetime DEFAULT NULL,
  PRIMARY KEY (`uid`),
  UNIQUE KEY `uid_UNIQUE` (`uid`),
  UNIQUE KEY `template_name_UNIQUE` (`template_name`)
) ENGINE=TokuDB DEFAULT CHARSET=utf8;

-- ` tnc_to_price_book `
CREATE TABLE `tnc_to_price_book` (
  `uid` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `tnc_id` int(11) DEFAULT NULL,
  `bu_name` varchar(45) DEFAULT NULL,
  `part_no` varchar(45) DEFAULT NULL,
  `organization_id` varchar(45) DEFAULT NULL,
  `is_default_bu` tinyint(1) DEFAULT NULL,
  `is_all_product` tinyint(1) DEFAULT NULL,
  `is_all_bu` tinyint(1) DEFAULT NULL,
  `is_all_customer` tinyint(1) DEFAULT NULL,
  `priority` int(11) DEFAULT NULL,
  `version` bigint(20) unsigned DEFAULT NULL,
  `modified_by` varchar(75) DEFAULT NULL,
  `modified_date` datetime DEFAULT NULL,
  `created_by` varchar(75) DEFAULT NULL,
  `created_date` datetime DEFAULT NULL,
  PRIMARY KEY (`uid`),
  UNIQUE KEY `uid_UNIQUE` (`uid`)
) ENGINE=TokuDB DEFAULT CHARSET=utf8;

-- ` transaction `
CREATE TABLE `transaction` (
  `uid` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `payment_id` varchar(50) DEFAULT NULL,
  `transaction_id` varchar(50) DEFAULT NULL,
  `parent_transaction_id` varchar(50) DEFAULT NULL,
  `state` varchar(15) DEFAULT NULL,
  `status` varchar(8) DEFAULT NULL COMMENT 'SUCCESS,DELETED',
  `wallet_id` varchar(50) DEFAULT NULL,
  `service_order` varchar(50) DEFAULT NULL,
  `contract_effective_date` datetime DEFAULT NULL,
  `service_id` varchar(75) DEFAULT NULL,
  `request_id` varchar(50) DEFAULT NULL,
  `source` varchar(20) DEFAULT NULL,
  `amount` int(11) DEFAULT NULL,
  `compensation_amount` int(11) DEFAULT NULL,
  `currency_amount` decimal(20,8) DEFAULT NULL,
  `balance` int(11) DEFAULT NULL,
  `action` varchar(75) DEFAULT NULL,
  `description` varchar(200) DEFAULT NULL,
  `charge_date` datetime DEFAULT NULL,
  `user_id` varchar(75) DEFAULT NULL,
  `user_name` varchar(75) DEFAULT NULL,
  `version` bigint(20) DEFAULT NULL,
  `modified_by` varchar(75) DEFAULT NULL,
  `modified_date` datetime DEFAULT NULL,
  `created_by` varchar(75) DEFAULT NULL,
  `created_date` datetime DEFAULT NULL,
  PRIMARY KEY (`uid`),
  UNIQUE KEY `uid_UNIQUE` (`uid`)
) ENGINE=TokuDB DEFAULT CHARSET=utf8;

-- ` wallet `
CREATE TABLE `wallet` (
  `uid` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `wallet_id` varchar(50) DEFAULT NULL,
  `organization_id` varchar(50) DEFAULT NULL,
  `organization_name` varchar(70) DEFAULT NULL,
  `status` varchar(20) DEFAULT NULL COMMENT 'INACTIVATE, ACTIVE, INACTIVE, DELETED',
  `max_idle_period` int(11) DEFAULT NULL,
  `idle_unit` varchar(8) DEFAULT NULL,
  `expired_on` date DEFAULT NULL,
  `credit_buffer` int(11) DEFAULT NULL,
  `remain_buffer` int(11) DEFAULT NULL,
  `is_one_time` tinyint(1) DEFAULT NULL,
  `balance` int(11) DEFAULT NULL,
  `available` int(11) DEFAULT NULL,
  `reserved` int(11) DEFAULT NULL,
  `utilization_alert_1_threshold` int(11) DEFAULT NULL,
  `utilization_alert_1_receivers` varchar(400) DEFAULT NULL,
  `utilization_alert_1_bcc` varchar(400) DEFAULT NULL,
  `utilization_alert_2_threshold` int(11) DEFAULT NULL,
  `utilization_alert_2_receivers` varchar(400) DEFAULT NULL,
  `utilization_alert_2_bcc` varchar(400) DEFAULT NULL,
  `forfeit_alert_1_threshold` int(11) DEFAULT NULL,
  `forfeit_alert_1_receivers` varchar(400) DEFAULT NULL,
  `forfeit_alert_1_bcc` varchar(400) DEFAULT NULL,
  `forfeit_alert_2_threshold` int(11) DEFAULT NULL,
  `forfeit_alert_2_receivers` varchar(400) DEFAULT NULL,
  `forfeit_alert_2_bcc` varchar(400) DEFAULT NULL,
  `version` bigint(20) DEFAULT NULL,
  `modified_by` varchar(75) DEFAULT NULL,
  `modified_date` datetime DEFAULT NULL,
  `created_by` varchar(75) DEFAULT NULL,
  `created_date` datetime DEFAULT NULL,
  `utilization_threshold_1_last_sent_date` datetime DEFAULT NULL,
  `utilization_negative_1_last_sent_date` datetime DEFAULT NULL,
  `utilization_threshold_2_last_sent_date` datetime DEFAULT NULL,
  `utilization_negative_2_last_sent_date` datetime DEFAULT NULL,
  `forfeit_threshold_1_last_sent_date` datetime DEFAULT NULL,
  `forfeit_negative_1_last_sent_date` datetime DEFAULT NULL,
  `forfeit_threshold_2_last_sent_date` datetime DEFAULT NULL,
  `forfeit_negative_2_last_sent_date` datetime DEFAULT NULL,
  `recharge_day` int(11) DEFAULT NULL,
  PRIMARY KEY (`uid`),
  UNIQUE KEY `uid_UNIQUE` (`uid`)
) ENGINE=TokuDB DEFAULT CHARSET=utf8;

--create api user for newportal
INSERT INTO `credit_module`.`api_user` (`username`, `password`, `incoming_channel`) VALUES ('NEW_PORTAL', 'yBV6z3HaQjafmwqvLMmm7w==', 'NEW_PORTAL');

-- init exchange_rate
INSERT INTO `credit_module`.`currency_rate` (`currency_rate_id`, `from_currency_code`, `to_currency_code`, `exchange_rate`) VALUES ('1', 'HKD', 'HKD', '1.00000000');
INSERT INTO `credit_module`.`currency_rate` (`currency_rate_id`, `from_currency_code`, `to_currency_code`, `exchange_rate`) VALUES ('2', 'USD', 'HKD', '7.84000015');
INSERT INTO `credit_module`.`currency_rate` (`currency_rate_id`, `from_currency_code`, `to_currency_code`, `exchange_rate`) VALUES ('3', 'RMB', 'HKD', '1.24000001');

-- 20180706 changes
ALTER TABLE `credit_module`.`price_book` 
ALTER TABLE `credit_module`.`price_book` 
CHANGE COLUMN `service_family` `service_family` VARCHAR(255) NULL DEFAULT NULL ,
CHANGE COLUMN `service_name` `service_name` VARCHAR(255) NULL DEFAULT NULL ,
CHANGE COLUMN `short_name` `short_name` VARCHAR(100) NULL DEFAULT NULL ,
CHANGE COLUMN `category_no` `category_no` VARCHAR(32) NULL DEFAULT NULL ,
CHANGE COLUMN `category_name` `category_name` VARCHAR(255) NULL DEFAULT NULL ,
CHANGE COLUMN `product_name` `product_name` VARCHAR(255) NULL DEFAULT NULL ,
CHANGE COLUMN `currency_code` `currency_code` VARCHAR(36) NULL DEFAULT NULL ,
CHANGE COLUMN `service_status` `service_status` VARCHAR(100) NULL DEFAULT NULL ,
CHANGE COLUMN `category_status` `category_status` VARCHAR(100) NULL DEFAULT NULL ,
CHANGE COLUMN `product_status` `product_status` VARCHAR(100) NULL DEFAULT NULL ,
CHANGE COLUMN `pricebook_status` `pricebook_status` VARCHAR(100) NULL DEFAULT NULL ,
CHANGE COLUMN `gl_code` `gl_code` VARCHAR(25) NULL DEFAULT NULL ;

