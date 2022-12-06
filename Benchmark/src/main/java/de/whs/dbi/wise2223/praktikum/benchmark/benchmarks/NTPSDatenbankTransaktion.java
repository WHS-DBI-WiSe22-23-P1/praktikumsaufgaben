package de.whs.dbi.wise2223.praktikum.benchmark.benchmarks;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static de.whs.dbi.wise2223.praktikum.benchmark.Helpers.withConnection;

public class NTPSDatenbankTransaktion {

    public int getBalanceFromAccount(int accId) throws SQLException {
        return withConnection(connection -> {
            String statement = "SELECT balance FROM accounts WHERE accid = ? LIMIT 1";
            PreparedStatement preparedStatement = connection.prepareStatement(statement);
            preparedStatement.setInt(accId, 0);

            ResultSet resultSet = preparedStatement.executeQuery();

            //todo - exception
            int accountBalance = resultSet.getInt(1);

            resultSet.close();
            preparedStatement.close();

            return accountBalance;
        });
    }

    public int updateBalance(int accId, int tellerId, int branchId, int delta) throws SQLException {
        return withConnection(connection -> {
            int updatedAccountBalance = updateValueTupel(connection, "accounts", "balance", "accid", accId, delta);
            updateValueTupel(connection, "branches", "balance", "branchid", branchId, delta);
            updateValueTupel(connection, "tellers", "balance", "tellerid", tellerId, delta);

            String historyComment = "------------------------------";
            String query = "INSERT INTO history (accid, tellerid, delta, branchid, accbalance, comment) VALUES %d %d %d %d %d %s;".
                    formatted(accId, tellerId, delta, branchId, updatedAccountBalance, historyComment);

            connection.createStatement().execute(query);

            return updatedAccountBalance;
        });
    }

    public int getNumberOfDeltaBalance(int delta) throws SQLException {
        return withConnection(connection -> {
            String query = "SELECT COUNT(*) FROM history WHERE accbalance = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, delta);

            ResultSet resultSet = preparedStatement.executeQuery();

            //todo - exception
            int numberOfDeltaBalances = resultSet.getInt(1);

            resultSet.close();
            preparedStatement.close();

            return numberOfDeltaBalances;
        });
    }

    private int updateValueTupel(Connection connection, String tableName, String updatingValueName, String valueName, int value, int updatingValueDelta) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT ? FROM ? WHERE ? = ?");
        preparedStatement.setString(1, updatingValueName);
        preparedStatement.setString(2, tableName);
        preparedStatement.setString(3, valueName);
        preparedStatement.setInt(4, value);

        ResultSet resultSet = preparedStatement.executeQuery();

        //todo - acid eigenschaften? / exception
        int currentValue = resultSet.getInt(1);
        resultSet.updateInt(0, currentValue + updatingValueDelta);

        resultSet.close();
        preparedStatement.close();

        return currentValue + updatingValueDelta;
    }

}
