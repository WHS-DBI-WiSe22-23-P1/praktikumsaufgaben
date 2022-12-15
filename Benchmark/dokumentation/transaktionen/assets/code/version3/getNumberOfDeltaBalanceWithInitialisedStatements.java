public int getNumberOfDeltaBalanceWithInitialisedStatement(int delta) throws SQLException {
    preparedStatementAnalyse.setInt(1, delta);

    ResultSet resultSet = preparedStatementAnalyse.executeQuery();

    int numberOfDeltaBalances = -1;
    if(resultSet.next())
        numberOfDeltaBalances = resultSet.getInt(1);

    resultSet.close();

    return numberOfDeltaBalances;
}