-- ******Functions******
set global log_bin_trust_function_creators=1;

-- ####### Split String Function ##########
CREATE FUNCTION `splitString`(
  str VARCHAR(255) ,
  delim VARCHAR(12) ,
  pos INT
) RETURNS varchar(255) CHARSET utf8
RETURN REPLACE(
  SUBSTRING(
    SUBSTRING_INDEX(str , delim , pos) ,
    CHAR_LENGTH(
      SUBSTRING_INDEX(str , delim , pos - 1)
    ) + 1
  ) ,
  delim ,
  ''
)
-- ******Stored Procedures******
-- ####### Calculate Total Price Stored Procedure ##########
USE `credit_module`;
DROP procedure IF EXISTS `calculatePrice`;

DELIMITER $$
USE `credit_module`$$
CREATE PROCEDURE `calculatePrice`(
IN partNoList VARCHAR(255),
IN chargeTypeList VARCHAR(255),
IN quantityList VARCHAR(255),
OUT totalAmount INT(11)
)
BEGIN
  /* Calculate Price
   *
   * Parameters: partNoList, chargeTypeList, quantityList
   * separate by comma, sort by partNo ASC
   * same index (partNo, chargeType, quantity) are from the same chargingItem
   */

  DECLARE currentPartNo VARCHAR(255);
  DECLARE currentChargeType VARCHAR(255);
  DECLARE currentQuantity INT;
  -- CHANGE TO OUT PARAM: DECLARE totalAmount INT;
  DECLARE bDone INT;
  DECLARE rowCount INT;
  DECLARE oneOffPrice INT;
  DECLARE recurringPrice INT;
  DECLARE partNoListItemCount INT;
  DECLARE partNoListQueryCount INT;
  DECLARE curs CURSOR FOR SELECT pb.one_off_price, pb.recurring_price, pb.part_no FROM price_book pb WHERE FIND_IN_SET(pb.part_no, partNoList) > 0 ORDER BY pb.part_no ASC;
  DECLARE CONTINUE HANDLER FOR NOT FOUND SET bDone = 1;
  
  SET partNoListItemCount = LENGTH(partNoList) - LENGTH(REPLACE(partNoList, ',', '')) + 1;
  SELECT COUNT(*) INTO partNoListQueryCount FROM price_book pb WHERE FIND_IN_SET(pb.part_no, partNoList) > 0;
  CALL throwExceptionIfTrue(partNoListItemCount != partNoListQueryCount, 'PRICE-BOOK-ERROR-0009');

  OPEN curs;

  SET totalAmount = 0;
  SET rowCount = 0;
  SET bDone = 0;
  repeat_loop:REPEAT
    FETCH curs INTO oneOffPrice, recurringPrice, currentPartNo;
	IF bDone = 1 THEN LEAVE repeat_loop; END IF;

    SET rowCount = rowCount + 1;
    SET currentChargeType = splitString(chargeTypeList, ',', rowCount);
    SET currentQuantity = CAST(splitString(quantityList, ',', rowCount) AS UNSIGNED);

    IF (currentChargeType = 'ONE_OFF') THEN
		SET totalAmount = totalAmount + oneOffPrice * currentQuantity;
    ELSE
		SET totalAmount = totalAmount + recurringPrice * currentQuantity;
    END IF;
  UNTIL bDone END REPEAT;
  CLOSE curs;
END$$

DELIMITER ;

-- ####### CreateReal Time Payment Stored Procedure ##########
USE `credit_module`;
DROP procedure IF EXISTS `createRealTimePayment`;

DELIMITER $$
USE `credit_module`$$
-- ####### CreateReal Time Payment Stored Procedure ##########
CREATE PROCEDURE `createRealTimePayment`(
IN walletId VARCHAR(50), 
IN partNoList VARCHAR(255),
IN chargeTypeList VARCHAR(255),
IN unitPriceList VARCHAR(255),
IN quantityList VARCHAR(255),
IN description VARCHAR(200) CHARSET utf8,
IN userId VARCHAR(45),
IN requestId VARCHAR(50))
BEGIN
	-- product item related fields
	DECLARE currentPartNo VARCHAR(50);
	DECLARE chargeItemId VARCHAR(50);
	DECLARE currentProductName VARCHAR(50);
	DECLARE currentChargeType VARCHAR(10);
    DECLARE currentUnitPrice VARCHAR(50);
	DECLARE currentQuantity INT(11);
	DECLARE oneOffPrice INT(11);
	DECLARE recurringPrice INT(11);
    DECLARE currentItemTotalAmount INT(11);
	DECLARE totalAmount INT(11);
	DECLARE partNoListItemCount INT;
	DECLARE partNoListQueryCount INT;
	-- contract related fields
	DECLARE currentContractBalance INT(11);
	DECLARE rate DECIMAL(10,8);
    DECLARE previousRate DECIMAL(10,8);
	DECLARE contractId VARCHAR(50);
    DECLARE creditConsumed INT(11);
	DECLARE contractCount INT(11);
	DECLARE isAllProduct TINYINT(1);
    DECLARE isAllBu TINYINT(1);
    -- compensation
    DECLARE compensationTotalUsage INT(11);
	-- wallet related fields
	DECLARE walletBalance INT(11);
    DECLARE walletAvailable INT(11);
	DECLARE organizationId VARCHAR(50);
    DECLARE walletStatus VARCHAR(50);
    DECLARE tmpNumber INT(11);
    -- wallet buffer
    DECLARE walletBuffer INT(11);
    DECLARE walletRemainBuffer INT(11);
    DECLARE isBufferOneTime INT(1);
    DECLARE isBufferUsed INT(1);
    DECLARE walletBufferTotalUsage INT(11);
	-- payment related fields
	DECLARE paymentId VARCHAR(50);
    DECLARE paymentUid INT(11);
	-- chargeItem list for insert
	DECLARE chargeItemList VARCHAR(255);
	-- contract_to_price_book
    DECLARE productCount INT(11);
    DECLARE cpbPartNo VARCHAR(50);
    DECLARE buCount INT(11);
	DECLARE compensationContractCount INT(11);
    -- contract_to_bu_master
    DECLARE currentBuName VARCHAR(50);
    -- price_book
    DECLARE pbCount INT(11); 
	-- cursor
	DECLARE rowCount INT(11);
	DECLARE done INT;
	DECLARE contract_done INT;
    DECLARE cpb_done INT;
    DECLARE cbm_done INT;
    DECLARE pb_curs CURSOR FOR SELECT pb.one_off_price, pb.recurring_price, pb.part_no, pb.product_name FROM price_book pb WHERE FIND_IN_SET(pb.part_no, partNoList) > 0 ORDER BY pb.part_no ASC;
	DECLARE ci_curs CURSOR FOR SELECT charge_item_id, part_no, product_name, charge_type, total_amount, quantity FROM tmp_charge_item;
	DECLARE ci_map_curs CURSOR FOR SELECT charge_item_id, contract_id, amount FROM tmp_map_charge_item_to_contract;
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = 1;
	-- validation partNo and priceBook
	SET partNoListItemCount = LENGTH(partNoList) - LENGTH(REPLACE(partNoList, ',', '')) + 1;
	SELECT COUNT(*) INTO partNoListQueryCount FROM price_book pb WHERE FIND_IN_SET(pb.part_no, partNoList) > 0;
	CALL throwExceptionIfTrue(partNoListItemCount != partNoListQueryCount, 'PRICE-BOOK-ERROR-0009');
    -- create charge_item temporary table
	DROP TEMPORARY TABLE IF EXISTS tmp_charge_item;
    CREATE TEMPORARY TABLE IF NOT EXISTS tmp_charge_item(charge_item_id VARCHAR(50), part_no VARCHAR(50), product_name VARCHAR(50), charge_type VARCHAR(10), total_amount INT, quantity INT) ENGINE=MEMORY;
    DROP TEMPORARY TABLE IF EXISTS tmp_map_charge_item_to_contract;
    CREATE TEMPORARY TABLE IF NOT EXISTS tmp_map_charge_item_to_contract(charge_item_id VARCHAR(50), contract_id VARCHAR(50), amount INT) ENGINE=MEMORY;
	-- validation wallet has enough credit before lock wallet table
    -- assume that compensation is totally available, not cater the case that compensation is unavailable for some items
    CALL calculatePrice(partNoList, chargeTypeList, quantityList, totalAmount);
    SELECT w.available, w.credit_buffer, w.remain_buffer, w.is_one_time INTO walletAvailable, walletBuffer, walletRemainBuffer, isBufferOneTime FROM wallet w WHERE wallet_id = walletId;
    IF ((isBufferOneTime = 1 AND walletRemainBuffer = walletBuffer) OR (isBufferOneTime != 1 AND walletRemainBuffer > 0)) THEN
		CALL throwExceptionIfTrue(walletAvailable + walletRemainBuffer < totalAmount, 'WALLET-ERROR-1029');
	ELSE
		CALL throwExceptionIfTrue(walletAvailable < totalAmount, 'WALLET-ERROR-1029');
	END IF;
   
	-- 2. Lock wallet
	SET autocommit=0;
	START TRANSACTION;
	SELECT w.balance, w.available, w.organization_id, w.status INTO walletBalance, walletAvailable, organizationId, walletStatus FROM wallet w WHERE wallet_id = walletId FOR UPDATE;
    -- validation wallet Status
	-- CALL throwExceptionIfTrue(walletCount = 0, 'WALLET-ERROR-1009');
	CALL throwExceptionIfTrue(walletStatus = 'INACTIVE', 'WALLET-ERROR-1014');
	-- 3. Determine contract
	OPEN pb_curs;
	SET currentItemTotalAmount = 0;
    SET totalAmount = 0;
	SET done = 0;
	SET rowCount = 0;
    SET rate = 0;
	SET isAllProduct=0;
    SET isAllBu=0;
    SET compensationTotalUsage = 0;
    SET walletBufferTotalUsage = 0;
    pbLoop: LOOP
		FETCH pb_curs INTO oneOffPrice, recurringPrice, currentPartNo, currentProductName;
		IF done = 1 THEN CLOSE pb_curs; LEAVE pbLoop; END IF;
            
		SET rowCount = rowCount + 1;
		SET currentChargeType = splitString(chargeTypeList, ',', rowCount);
		SET currentQuantity = CAST(splitString(quantityList, ',', rowCount) AS UNSIGNED);
        SET currentUnitPrice = splitString(unitPriceList, ',', rowCount);
        
        -- compare priceBook with unitPriceList, throw exception if not match
        IF (currentUnitPrice != 'null') THEN
			IF (currentChargeType = 'ONE_OFF') THEN
				CALL throwExceptionIfTrue(CAST(currentUnitPrice AS UNSIGNED) != oneOffPrice, 'PRICE-BOOK-ERROR-0010');
			ELSE
				CALL throwExceptionIfTrue(CAST(currentUnitPrice AS UNSIGNED) != recurringPrice, 'PRICE-BOOK-ERROR-0010');
			END IF;
        END IF;
				
		IF (currentChargeType = 'ONE_OFF') THEN 
			SET currentItemTotalAmount = oneOffPrice * currentQuantity;
			SET totalAmount = totalAmount + oneOffPrice * currentQuantity;
		ELSE 
			SET currentItemTotalAmount = recurringPrice * currentQuantity;
			SET totalAmount = totalAmount + recurringPrice * currentQuantity;
		END IF;

		-- insert temporary data into tmp_charge_item
		SET chargeItemId = concat('CI-', uuid_short());
		
        INSERT INTO tmp_charge_item (charge_item_id, part_no, product_name, charge_type, total_amount, quantity) 
			VALUES(chargeItemId, currentPartNo, currentProductName, currentChargeType, currentItemTotalAmount, currentQuantity);
                             
      BLOCK1: BEGIN
        -- contract cursor for determine compensation contract deduction priority
		DECLARE contract_curs CURSOR FOR SELECT FOUND_ROWS(), contract_id, balance, is_all_product, is_all_bu FROM contract c WHERE c.wallet_id = walletId AND c.balance>0 AND c.balance IS NOT NULL AND type='COMPENSATION' ORDER BY c.is_all_bu ASC, c.is_all_product ASC;
        DECLARE CONTINUE HANDLER FOR NOT FOUND SET contract_done = 1;
        OPEN contract_curs;
        SET contract_done =0;
        SET contractCount =0;
			contractLoop: LOOP
			FETCH contract_curs INTO compensationContractCount, contractId, currentContractBalance, isAllProduct, isAllBu;
			
			IF contract_done = 1 THEN CLOSE contract_curs; LEAVE contractLoop; END IF;
			-- select compensation contract to deduce credit first	
		  	WHILE (compensationContractCount>0 && currentContractBalance>0) DO		
           
				SET compensationContractCount = compensationContractCount-1;
                
                -- (1)deduce specific product(is_ll_product:is_all_bu=0:0)
				SELECT balance INTO currentContractBalance FROM contract c WHERE c.contract_id = contractId;
				IF (!isAllProduct && !isAllBu && currentItemTotalAmount>0 && currentContractBalance>0) THEN
                
					BLOCK2: BEGIN     
					DECLARE cpb_curs CURSOR FOR SELECT COUNT(1), part_no FROM contract_to_price_book cp WHERE cp.contract_id = contractId and cp.part_no = currentPartNo;
					DECLARE CONTINUE HANDLER FOR NOT FOUND SET cpb_done = 1;
					OPEN cpb_curs;
					SET cpb_done=0;
						cpb_loop: LOOP
						FETCH cpb_curs INTO productCount, cpbPartNo;
						IF cpb_done = 1 THEN CLOSE cpb_curs; LEAVE cpb_loop; END IF;
                        IF(productCount != 0) THEN
							-- Contract deduction
							IF (currentItemTotalAmount < currentContractBalance) THEN
								UPDATE contract SET balance = balance - currentItemTotalAmount WHERE contract_id=contractId;
								SET creditConsumed = currentItemTotalAmount;
								SET currentItemTotalAmount = 0;
							ELSE
								UPDATE contract c SET c.balance=0, c.currency_balance= 0, c.status='CLOSED' WHERE contract_id=contractId;
								SET creditConsumed = currentContractBalance;
								SET currentItemTotalAmount = currentItemTotalAmount - currentContractBalance;
							END IF;
							-- insert temporary data into tmp_map_charge_item_to_contract
							INSERT INTO tmp_map_charge_item_to_contract (charge_item_id, contract_id, amount) VALUES(chargeItemId, contractId, creditConsumed);
                            SET compensationTotalUsage = compensationTotalUsage + creditConsumed;
						END IF;
						END LOOP cpb_loop;
					END BLOCK2;
				END IF;	 
         
               -- (2)deduce specific bu(is_ll_product:is_all_bu=1:0)
                SELECT balance INTO currentContractBalance FROM contract c WHERE c.contract_id = contractId ;
				IF (!isAllBu && isAllProduct && currentItemTotalAmount>0 && currentContractBalance>0) THEN
					BLOCK3: BEGIN     
						DECLARE cbm_curs CURSOR FOR SELECT FOUND_ROWS(), bu_name FROM contract_to_bu_master cb WHERE cb.contract_id = contractId;
						DECLARE CONTINUE HANDLER FOR NOT FOUND SET cbm_done = 1;
						OPEN cbm_curs;
						SET cbm_done=0;
						cbm_loop: LOOP
							FETCH cbm_curs INTO buCount, currentBuName;
							IF cbm_done = 1 THEN CLOSE cbm_curs; LEAVE cbm_loop; END IF;
							IF(buCount != 0) THEN
									SELECT count(1) into pbCount FROM price_book pb WHERE pb.bu_name = currentBuName AND pb.part_no=currentPartNo;
									-- Contract deduction
									IF(pbCount!=0) THEN
										IF (currentItemTotalAmount < currentContractBalance) THEN
											UPDATE contract SET balance = balance - currentItemTotalAmount WHERE contract_id=contractId;
											SET creditConsumed = currentItemTotalAmount;
											SET currentItemTotalAmount = 0;
										ELSE
											UPDATE contract c SET c.balance=0, c.currency_balance= 0, c.status='CLOSED' WHERE contract_id=contractId;
											SET creditConsumed = currentContractBalance;
											SET currentItemTotalAmount = currentItemTotalAmount - currentContractBalance;
										END IF; 
                                        -- insert temporary data into tmp_map_charge_item_to_contract
										INSERT INTO tmp_map_charge_item_to_contract (charge_item_id, contract_id, amount) VALUES(chargeItemId, contractId, creditConsumed);
                                        SET compensationTotalUsage = compensationTotalUsage + creditConsumed;
									END IF;									
								END IF;
						END LOOP cbm_loop;
					END BLOCK3;
				END IF;
			
				-- (3)deduce general compensation（is_ll_product:is_all_bu=0:1/1:1)
				SELECT balance INTO currentContractBalance FROM contract c WHERE c.contract_id = contractId ;
				IF (isAllBu && currentItemTotalAmount>0 && currentContractBalance>0) THEN
					IF (currentItemTotalAmount < currentContractBalance) THEN
						UPDATE contract SET balance = balance - currentItemTotalAmount WHERE contract_id=contractId;
						SET creditConsumed = currentItemTotalAmount;
						SET currentItemTotalAmount = 0;
					ELSE
						UPDATE contract c SET c.balance=0, c.currency_balance= 0, c.status='CLOSED' WHERE contract_id=contractId;
						SET creditConsumed = currentContractBalance;
						SET currentItemTotalAmount = currentItemTotalAmount - currentContractBalance;
					END IF; 					
					-- insert temporary data into tmp_map_charge_item_to_contract
					INSERT INTO tmp_map_charge_item_to_contract (charge_item_id, contract_id, amount) VALUES(chargeItemId, contractId, creditConsumed);
                    SET compensationTotalUsage = compensationTotalUsage + creditConsumed;
                    
				END IF;
                
				END WHILE;
			END LOOP contractLoop;
		END BLOCK1;
   
		whileLoop: WHILE (currentItemTotalAmount > 0) DO
        
			SET previousRate = rate;
        
            SELECT count(1), c.balance, (c.currency_amount/c.amount) AS rate, c.contract_id INTO contractCount, currentContractBalance, rate, contractId FROM contract c WHERE c.wallet_id = walletId AND c.balance>0 AND c.balance IS NOT NULL AND type='ONE_OFF' ORDER BY c.contract_effective_date ASC, c.ref_number ASC LIMIT 1 ;
			
            IF (rate IS NULL) THEN
				SET rate = previousRate;
			END IF;
            
            -- (contractCount = 0 and currentItemTotalAmount > 0) means compensation / one_off contracts are totally used up and it's time to use buffer
            IF (contractCount = 0) THEN
				-- validate buffer again, make sure it's available
                IF ((isBufferOneTime = 1 AND walletRemainBuffer != walletBuffer) OR (isBufferOneTime != 1 AND walletRemainBuffer = 0)) THEN
					CALL throwExceptionIfTrue(true, 'WALLET-ERROR-1029');
				END IF;
				-- try using buffer
                IF (walletRemainBuffer - walletBufferTotalUsage >= currentItemTotalAmount) THEN
					-- flag, for following logic to check if buffer is used
					SET isBufferUsed = 1;
                    -- mark down related data
                    SET creditConsumed = currentItemTotalAmount;
					SET currentItemTotalAmount = 0;
                    SET walletBufferTotalUsage = walletBufferTotalUsage + creditConsumed;
                    -- insert temporary data into tmp_map_charge_item_to_contract
					INSERT INTO tmp_map_charge_item_to_contract (charge_item_id, contract_id, amount) VALUES (chargeItemId, null, creditConsumed);
                    leave whileLoop;
				ELSE
					CALL throwExceptionIfTrue(contractCount = 0, 'WALLET-ERROR-1029');
				END IF;
			END IF;

			-- 4. Contract deduction
			IF (currentItemTotalAmount < currentContractBalance) THEN
				UPDATE contract SET balance = balance - currentItemTotalAmount, currency_balance= currency_balance-(rate*currentItemTotalAmount) WHERE contract_id=contractId;
				SET creditConsumed = currentItemTotalAmount;
				SET currentItemTotalAmount = 0;
			ELSE
				UPDATE contract c SET c.balance=0, c.currency_balance= 0, c.status='CLOSED' WHERE contract_id=contractId;
				SET creditConsumed = currentContractBalance;
				SET currentItemTotalAmount = currentItemTotalAmount - currentContractBalance;
			END IF;           	
						
			-- insert temporary data into tmp_map_charge_item_to_contract
			INSERT INTO tmp_map_charge_item_to_contract (charge_item_id, contract_id, amount) VALUES(chargeItemId, contractId, creditConsumed);
		END WHILE whileLoop;
	END LOOP pbLoop;
    
    -- 5. Wallet deduction
	UPDATE wallet SET balance = walletBalance - totalAmount + walletBufferTotalUsage, available = walletAvailable - totalAmount + walletBufferTotalUsage, remain_buffer = walletRemainBuffer - walletBufferTotalUsage WHERE wallet_id = walletId;
	
    -- 6. Release wallet
	COMMIT;
    SET autocommit=1;
    
	-- 7. Insert audit log
    SET autocommit=0;
	START TRANSACTION;
		-- 'payment'
		INSERT INTO payment (payment_id, wallet_id, organization_id, total_amount, description, status, version, modified_by, modified_date, created_by, created_date) 
			VALUES(UUID(), walletId, organizationId, totalAmount, description,'CONFIRMED', 1, userId, now(), userId, now());
		-- TODO:use trigger after insert
        SET paymentId = CONCAT('PM-', LPAD(LAST_INSERT_ID(), 10, '0'));
		UPDATE payment SET payment_id = paymentId WHERE uid= LAST_INSERT_ID();
		
        -- Mark paymentUid for return final result use
        SET paymentUid = LAST_INSERT_ID();
	OPEN ci_curs;	
	SET done = 0;

	ciLoop: LOOP
		FETCH ci_curs INTO chargeItemId, currentPartNo, currentProductName, currentChargeType, currentItemTotalAmount, currentQuantity;
		IF done = 1 THEN LEAVE ciLoop; END IF;
		
        -- 'charge_item'
        INSERT INTO charge_item (charge_item_id, payment_id, quantity, total_amount, part_no, product_name, charge_type, created_by, created_date, version) 
			VALUES(chargeItemId, paymentId, currentQuantity, currentItemTotalAmount, currentPartNo, currentProductName, currentChargeType, userId, now(), 1);
		UPDATE charge_item SET payment_id = paymentId WHERE uid=last_insert_id();
			
	END LOOP ciLoop;
    
	CLOSE ci_curs; 
        
	-- insert data into 'map_charge_item_to_contract'
	OPEN ci_map_curs;
	SET done = 0;

	cimLoop: LOOP
		FETCH ci_map_curs INTO chargeItemId, contractId, creditConsumed;
		IF done = 1 THEN LEAVE cimLoop; END IF;
		
        -- 'charge_item'
        INSERT INTO map_charge_item_to_contract (charge_item_id, contract_id, amount, created_by, created_date) 
			VALUES(chargeItemId,contractId, creditConsumed, userId, now());          
			
	END LOOP cimLoop;
        
	CLOSE ci_map_curs;  

	-- contract transaction
    -- only create when One_Off / Compensation contracts are used
    IF (totalAmount - walletBufferTotalUsage != 0) THEN
		SET tmpNumber = -totalAmount + walletBufferTotalUsage + compensationTotalUsage;
		INSERT INTO transaction(payment_id, transaction_id, state, status, wallet_id, request_id, source, amount, compensation_amount, currency_amount, balance, action, description, charge_date, user_id, user_name, version, modified_by, modified_date, created_by, created_date) 
			VALUES(paymentId, uuid(),'COMMITE', 'SUCCESS', walletId, requestId, 'SERVICE', -totalAmount + walletBufferTotalUsage + compensationTotalUsage, -compensationTotalUsage, tmpNumber * rate, walletBalance - totalAmount + walletBufferTotalUsage, 'real-time-tx', description, now(), userId, userId, 1, userId, now(), userId, now());
		UPDATE transaction SET transaction_id = concat('TX-', LPAD(last_insert_id(),10,'0')) WHERE uid = last_insert_id();
    END IF;
    
    -- buffer transaction
    -- only create when buffer is used
    IF (isBufferUsed) THEN
		-- SET tmpNumber = walletBufferTotalUsage * -1;
		INSERT INTO transaction(payment_id, transaction_id, state, status, wallet_id, request_id, source, amount, compensation_amount, currency_amount, balance, action, description, charge_date, user_id, user_name, version, modified_by, modified_date, created_by, created_date) 
			VALUES(paymentId, uuid(),'COMMITE', 'PENDING', walletId, requestId, 'SERVICE', -walletBufferTotalUsage, 0, 0, walletBalance - totalAmount + walletBufferTotalUsage, 'real-time-tx', description, now(), userId, userId, 1, userId, now(), userId, now());
		UPDATE transaction SET transaction_id = concat('TX-', LPAD(last_insert_id(),10,'0')) WHERE uid = last_insert_id();
    END IF;
		
	COMMIT;
    SET autocommit=1;
     -- return payment and wallet info
    SELECT
		p.uid, p.payment_id, p.wallet_id, p.organization_id, p.total_amount, p.description, p.status,
		w.uid, w.wallet_id, w.balance, w.utilization_alert_1_threshold, w.utilization_alert_1_receivers, w.utilization_alert_1_bcc, w.utilization_alert_2_threshold, w.utilization_alert_2_receivers, w.utilization_alert_2_bcc,
        w.utilization_threshold_1_last_sent_date, w.utilization_negative_1_last_sent_date, w.utilization_threshold_2_last_sent_date, w.utilization_negative_2_last_sent_date, compensationTotalUsage
    FROM payment p left join wallet w on p.wallet_id = w.wallet_id where p.uid = paymentUid;
END$$

DELIMITER ;


-- ####### Generate Finance Report Stored Procedure ##########
USE `credit_module`;
DROP procedure IF EXISTS `generateReport`;

DELIMITER $$
USE `credit_module`$$
CREATE PROCEDURE `generateReport`(
IN inputYear INT(4),
IN inputMonth INT(2)
)
BEGIN
	SELECT
	c.contract_id, w.organization_id, c.company_code, w.organization_name, t.charge_date,
    t.transaction_id, t.description, ROUND(cim.amount * (-1) * ((t.amount + t.compensation_amount) / ABS(t.amount + t.compensation_amount))) as report_amount,'NTT DOLLAR',
	cr.from_currency_code, cim.amount * (-1) *(c.currency_amount/c.amount) * ((t.amount + t.compensation_amount) / ABS(t.amount + t.compensation_amount)) as report_currency_amount,
    pb.gl_code, ci.charge_type, pb.provider, pb.provider_rate, c.status, c.last_reopen_date
	FROM contract c
    LEFT JOIN map_charge_item_to_contract cim ON c.contract_id = cim.contract_id
	LEFT JOIN charge_item ci ON ci.charge_item_id = cim.charge_item_id
	LEFT JOIN payment p ON p.payment_id = ci.payment_id
	LEFT JOIN wallet w ON w.wallet_id = c.wallet_id
	LEFT JOIN transaction t ON ((t.payment_id IS NOT NULL AND t.payment_id = p.payment_id) OR (t.payment_id IS NULL AND (ci.transaction_id = t.transaction_id OR ci.transaction_id = t.parent_transaction_id)))
	LEFT JOIN currency_rate cr ON cr.currency_rate_id = c.currency_rate_id
	LEFT JOIN price_book pb ON ci.part_no = pb.part_no
	WHERE YEAR(cim.created_date) = inputYear AND MONTH(cim.created_date) = inputMonth AND t.status != 'PENDING';
END$$

DELIMITER ;

-- ####### Throw Exception If True Stored Procedure ##########
USE `credit_module`;
DROP procedure IF EXISTS `throwExceptionIfTrue`;

DELIMITER $$
USE `credit_module`$$
CREATE PROCEDURE `throwExceptionIfTrue`(
  flag TINYINT(1),
  message VARCHAR(255))
BEGIN
  IF (flag) THEN
    SIGNAL SQLSTATE '45000'
    SET MESSAGE_TEXT = message;
  END IF;
END$$

DELIMITER ;