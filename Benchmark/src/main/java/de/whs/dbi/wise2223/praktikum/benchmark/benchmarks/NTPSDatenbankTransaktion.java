package de.whs.dbi.wise2223.praktikum.benchmark.benchmarks;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static de.whs.dbi.wise2223.praktikum.benchmark.Helpers.withConnection;

public class NTPSDatenbankTransaktion {

    public static int getBalanceFromAccount(int accId) throws SQLException {
        return withConnection(connection -> {
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
        });
    }

    public static int updateBalance(int accId, int tellerId, int branchId, int delta, String comment) throws SQLException {
        return withConnection(connection -> {
            int updatedAccountBalance = updateValueTupel(connection, "accid", "balance","accounts", accId, delta);
            updateValueTupel(connection, "branchid","balance", "branches", branchId, delta);
            updateValueTupel(connection, "tellerid", "balance","tellers", tellerId, delta);

            NTPSDatenbankErzeugen.insertHistory(accId, tellerId, branchId, delta, updatedAccountBalance, comment);

            return updatedAccountBalance;
        });
    }

    public static int getNumberOfDeltaBalance(int delta) throws SQLException {
        return withConnection(connection -> {
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
        });
    }

    //todo - acid eigenschaften
    private static int updateValueTupel(Connection connection, String primaryKeyName, String updatingValueName, String tableName, int primaryKeyValue, int updatingValueDelta) throws SQLException {
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
