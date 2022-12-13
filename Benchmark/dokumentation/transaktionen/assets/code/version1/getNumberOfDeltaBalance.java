public int getNumberOfDeltaBalance(int delta) throws SQLException {
    String query = "SELECT COUNT(*) FROM history WHERE accbalance = ?";
    PreparedStatement preparedStatement = connection.prepareStatement(query);
    preparedStatement.setInt(1, delta);

    ResultSet resultSet = preparedStatement.executeQuery();

    int numberOfDeltaBalances = -1;
    if(resultSet.next())
      numberOfDeltaBalances = resultSet.getInt(1);

    resultSet.close();
    reparedStatement.close();

    return numberOfDeltaBalances;
}