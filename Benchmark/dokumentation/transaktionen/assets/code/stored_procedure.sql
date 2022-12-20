DELIMITER //
CREATE PROCEDURE updateBalance(accountID int, tID int, brID int, delta int,commentar text)
BEGIN
UPDATE accounts set balance = balance + delta  WHERE accid = acID;
UPDATE branches set balance = balance + delta  WHERE branchid = bID;
UPDATE tellers set balance = balance + delta  WHERE tellerid = tID;
SELECT balance, @var_blance := balance FROM accounts WHERE accid = accountID;
INSERT INTO history (accid, tellerid, branchid, delta,accbalance, cmmnt) VALUES (accountID, tID, brID, delta, @var_blance, commentar);
END //