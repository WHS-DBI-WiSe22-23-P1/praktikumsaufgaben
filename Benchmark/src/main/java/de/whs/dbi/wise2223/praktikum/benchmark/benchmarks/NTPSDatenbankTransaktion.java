package de.whs.dbi.wise2223.praktikum.benchmark.benchmarks;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class NTPSDatenbankTransaktion {

    private Connection connection;

    private PreparedStatement preparedStatementEinzahlung, preparedStatementAnalyse;

    public NTPSDatenbankTransaktion(Connection connection) {
        this.connection = connection;
    }

    public void initialisePreparedStatements() throws SQLException {
        String statementEinzahlung = "SELECT balance FROM accounts_balances WHERE accid = ? LIMIT 1";
        preparedStatementEinzahlung = connection.prepareStatement(statementEinzahlung);

        String statementAnalyse = "SELECT balance_number FROM accounts_balance_numbers WHERE accbalance = ?";
        preparedStatementAnalyse = connection.prepareStatement(statementAnalyse);
    }

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

    public int getBalanceFromAccountWithInitialisedStatement(int accId) throws SQLException {
        preparedStatementEinzahlung.setInt(1, accId);

        ResultSet resultSet = preparedStatementEinzahlung.executeQuery();

        int accountBalance = -1;
        if(resultSet.next())
            accountBalance = resultSet.getInt(1);

        resultSet.close();

        return accountBalance;
    }

    public int updateBalance(int accId, int tellerId, int branchId, int delta, String comment) throws SQLException {
        int updatedAccountBalance = updateValueTupel("accid", "balance","accounts", accId, delta);
        updateValueTupel("branchid","balance", "branches", branchId, delta);
        updateValueTupel("tellerid", "balance","tellers", tellerId, delta);

        NTPSDatenbankErzeugen.insertHistory(connection, accId, tellerId, branchId, delta, updatedAccountBalance, comment);

        return updatedAccountBalance;
    }

    public int getNumberOfDeltaBalance(int delta) throws SQLException {
        String query = "SELECT COUNT(*) FROM history WHERE accbalance = ?";
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

    public int getNumberOfDeltaBalanceWithInitialisedStatement(int delta) throws SQLException {
        preparedStatementAnalyse.setInt(1, delta);

        ResultSet resultSet = preparedStatementAnalyse.executeQuery();

        int numberOfDeltaBalances = -1;
        if(resultSet.next())
            numberOfDeltaBalances = resultSet.getInt(1);

        resultSet.close();

        return numberOfDeltaBalances;
    }

    //todo - acid eigenschaften
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

}
