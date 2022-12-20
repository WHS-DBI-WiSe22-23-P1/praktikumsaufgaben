public static void insertHistory(Connection connection, int accId, int tellerId, int branchId, int delta, int updatedAccountBalance, String comment) throws SQLException {
    String query = "INSERT INTO history (accid, tellerid, branchid, delta, accbalance, cmmnt) VALUES (?, ?, ?, ?, ?, ?)";
    PreparedStatement insertStatement = connection.prepareStatement(query);
    insertStatement.setInt(1, accId);
    insertStatement.setInt(2, tellerId);
    insertStatement.setInt(3, branchId);
    insertStatement.setInt(4, delta);
    insertStatement.setInt(5, updatedAccountBalance);
    insertStatement.setString(6, comment);

    insertStatement.execute();
    insertStatement.close();
}