public int updateBalance(int accId, int tellerId, int branchId, int delta, String comment) throws SQLException {
    int updatedAccountBalance = updateValueTupel("accid", "balance","accounts", accId, delta);
    updateValueTupel("branchid","balance", "branches", branchId, delta);
    updateValueTupel("tellerid", "balance","tellers", tellerId, delta);

    NTPSDatenbankErzeugen.insertHistory(accId, tellerId, branchId, delta, updatedAccountBalance, comment);

    return updatedAccountBalance;
}

private int updateValueTupel(String primaryKeyName, String updatingValueName, String tableName, int primaryKeyValue, int updatingValueDelta) throws SQLException {
    String selectQuery = "SELECT %s FROM %s WHERE %s = ?".formatted(updatingValueName, tableName, primaryKeyName);
    PreparedStatement selectStatement = connection.prepareStatement(selectQuery);
    selectStatement.setInt(1, primaryKeyValue);

    ResultSet selectResultSet = selectStatement.executeQuery();
    if(!selectResultSet.next())
        return -1;

    int currentValue = selectResultSet.getInt(1);

    selectResultSet.close();
    selectStatement.close();

    String updateQuery = "UPDATE %s SET %s = ? WHERE %s = ?".formatted(tableName, updatingValueName, primaryKeyName);
    PreparedStatement updateStatement = connection.prepareStatement(updateQuery);
    updateStatement.setInt(1, currentValue + updatingValueDelta);
    updateStatement.setInt(2, primaryKeyValue);
    updateStatement.executeUpdate();
    updateStatement.close();

    return currentValue + updatingValueDelta;
}