CREATE VIEW accounts_balance_numbers AS SELECT accbalance, COUNT(accbalance) as balance_number FROM history GROUP BY accbalance;