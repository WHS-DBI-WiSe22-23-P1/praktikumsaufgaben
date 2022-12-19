public int updateBalance(int accId, int tellerId, int branchId, int delta, String comment) throws SQLException{
    int updatedAccountBalance=updateValueTupel("accid","balance","accounts",accId,delta);
    updateValueTupel("branchid", "balance", "branches", branchId, delta);
    updateValueTupel("tellerid", "balance", "tellers", tellerId, delta);

    NTPSDatenbankErzeugen.insertHistory(connection, accId, tellerId, branchId, delta, updatedAccountBalance, comment);
    connection.commit();

    return updatedAccountBalance;
}
