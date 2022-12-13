public int getBalanceFromAccount(int accId) throws SQLException {
    String statement = "SELECT balance FROM accounts WHERE accid = ? LIMIT 1";
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