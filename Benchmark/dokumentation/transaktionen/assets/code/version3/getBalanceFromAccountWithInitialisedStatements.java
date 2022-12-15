public int getBalanceFromAccountWithInitialisedStatement(int accId) throws SQLException {
    preparedStatementEinzahlung.setInt(1, accId);

    ResultSet resultSet = preparedStatementEinzahlung.executeQuery();

    int accountBalance = -1;
    if(resultSet.next())
        accountBalance = resultSet.getInt(1);

    resultSet.close();

    return accountBalance;
}