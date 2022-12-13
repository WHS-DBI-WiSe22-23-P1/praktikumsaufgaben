public int getNumberOfDeltaBalanceWithViews(int delta) throws SQLException {
    String query = "SELECT balance_number FROM accounts_balance_numbers WHERE accbalance = ?";
    PreparedStatement preparedStatement = connection.prepareStatement(query);
    preparedStatement.setInt(1, delta);

    ResultSet resultSet = preparedStatement.executeQuery();

    int numberOfDeltaBalances = -1;
    if(resultSet.next())
        numberOfDeltaBalances = resultSet.getInt(1);

    resultSet.close();
    preparedStatement.close();

    return numberOfDeltaBalances;
}