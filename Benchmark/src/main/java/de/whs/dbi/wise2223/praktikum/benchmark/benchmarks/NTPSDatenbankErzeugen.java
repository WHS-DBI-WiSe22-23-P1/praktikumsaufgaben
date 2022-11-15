package de.whs.dbi.wise2223.praktikum.benchmark.benchmarks;

import java.sql.SQLException;
import java.util.Random;
import java.util.UUID;

import static de.whs.dbi.wise2223.praktikum.benchmark.Helpers.withConnection;

public class NTPSDatenbankErzeugen {

    static final Random RANDOM = new Random();

    private NTPSDatenbankErzeugen() {}

    public static void insertAccounts(int n) throws SQLException {
        withConnection(connection -> {

            for (int i = 0; i < n * 100000; i++) {
                int randomBranchId = RANDOM.nextInt(n);

                String accountsName = "name------%010d".formatted(i);
                String accountsAddress = ("accountsaddress-------------------------------------------%010d").formatted(i);

                String query = "INSERT INTO accounts (accid, name, balance, branchid, address) " +
                        "VALUES (%d, '%s', %d, %d, '%s');".formatted(i, accountsName, 0, randomBranchId, accountsAddress);

                connection.createStatement().execute(query);
            }
        });
    }

    public static void insertBranches(int n) throws SQLException {
        withConnection(connection -> {

            for(int i = 0;i < n;i++) {
                String branchName = "branchname-----%05d".formatted(i);
                String branchAddress = "branchaddress------------------------------------------------------%05d".formatted(i);

                String query = ("INSERT INTO branches (branchid, branchname, balance, address) " +
                        "VALUES (%d, '%s', %d, '%s')").formatted(i, branchName, 0, branchAddress);

                connection.createStatement().execute(query);
            }
        });
    }

    public static void insertTellers(int n) throws SQLException {
        withConnection(connection -> {

            for(int i = 0;i < n * 10;i++) {
                int randomBranchId = RANDOM.nextInt(n);

                String tellersName = "tellername--%08d".formatted(i);
                String tellersAddress = ("tellersaddress----------------------------------------------%08d").formatted(i);

                String query = "INSERT INTO tellers (tellerid, tellername, balance, branchid, address) " +
                        "VALUES (%d, '%s', %d, %d, '%s')".formatted(i, tellersName, 0, randomBranchId, tellersAddress);

                connection.createStatement().execute(query);
            }
        });
    }
}
