public int getBalanceFromAccountWithViews(int accId) throws SQLException {
    String statement = "SELECT balance FROM accounts_balances WHERE accid = ? LIMIT 1";
    PreparedStatement preparedStatement = connection.prepareStatement(statement);
    preparedStatement.setInt(1, accId);

    ResultSet resultSet = preparedStatement.executeQuery();

    int accountBalance = -1;
    if(resultSet.next())
        accountBalance = resultSet.getInt(1);

    resultSet.close();
    preparedStatement.close();

    return accountBalance;
}