public void initialisePreparedStatements() throws SQLException {
    String statementEinzahlung = "SELECT balance FROM accounts_balances WHERE accid = ? LIMIT 1";
    preparedStatementEinzahlung = connection.prepareStatement(statementEinzahlung);

    String statementAnalyse = "SELECT balance_number FROM accounts_balance_numbers WHERE accbalance = ?";
    preparedStatementAnalyse = connection.prepareStatement(statementAnalyse);
}