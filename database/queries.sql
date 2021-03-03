BEGIN TRANSACTION;

-- (3) SEE MY CURRENT ACCOUNT BALANCE
SELECT balance FROM accounts WHERE user_id = 1001;



--(4.1) LIST OF USERS TO SEND TEBUCKS TO
SELECT user_id, username FROM users WHER user-id IT NOT = jasmien;


--(4.2 INCLUDES 4.6) MAKE A TRANSFER
INSERT INTO transfers(transfer_type_id, transfer_status_id, account_from, account_to, amount)
VALUES(2, 2, 
(SELECT account_id FROM accounts WHERE user_id = (SELECT user_id FROM users WHERE username = 'jasmine')), 
(SELECT account_id FROM accounts WHERE user_id = (SELECT user_id FROM users WHERE username = 'steven')),
1000); 

INSERT INTO transfers(transfer_type_id, transfer_status_id, account_from, account_to, amount)
VALUES(2, 2, 
(SELECT account_id FROM accounts WHERE user_id = (SELECT user_id FROM users WHERE username = 'steven')), 
(SELECT account_id FROM accounts WHERE user_id = (SELECT user_id FROM users WHERE username = 'jasmine')),
1000);

--SELECT *FROM transfers;

--(4.3 + 4.4) UPDATING ACCOUNT BALANCES
UPDATE accounts
SET balance = 2000
WHERE user_id = 1001;

update accounts
SET balance = 0  
WHERE user_id = 1002; 
-- until here --

--SELECT user_id, balance FROM accounts WHERE user_id = 1001;
--SELECT user_id, balance FROM accounts WHERE user_id = 1002;

--(5) SELECT ALL 'MY' TRANSFERS - FROM AND TO 'ME'
SELECT transfer_id, account_to AS From/To, amount FROM transfers WHERE 
account_from = (SELECT account_id FROM accounts WHERE user_id = (SELECT user_id FROM users WHERE username = 'jasmine'))
OR
account_to = (SELECT account_id FROM accounts WHERE user_id = (SELECT user_id FROM users WHERE username = 'jasmine'));

SELECT transfer_id, account_from || ', ' || account_to AS From, amount FROM transfers WHERE 
account_from = (SELECT account_id FROM accounts WHERE user_id = (SELECT user_id FROM users WHERE username = 'jasmine'))
OR
account_to = (SELECT account_id FROM accounts WHERE user_id = (SELECT user_id FROM users WHERE username = 'jasmine'));

--(6) SELECT ANY TRANSFER BY ID
SELECT transfer_id, account_from, account_to, transfer_type_id, transfer_status_id, amount FROM transfers WHERE transfer_id = 3016;


ROLLBACK;