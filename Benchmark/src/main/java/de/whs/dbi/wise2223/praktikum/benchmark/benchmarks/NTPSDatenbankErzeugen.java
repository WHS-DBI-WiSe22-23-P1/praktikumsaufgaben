package de.whs.dbi.wise2223.praktikum.benchmark.benchmarks;

import java.sql.SQLException;
import java.util.Random;
import java.util.UUID;

import static de.whs.dbi.wise2223.praktikum.benchmark.Helpers.withConnection;

public class NTPSDatenbankErzeugen {
    private NTPSDatenbankErzeugen() {
    }

    public static void insertAccounts(int n) throws SQLException {
        withConnection(connection -> {

            final Random random = new Random();

            for (int i = 0; i < n * 100000; i++) {
                int randomBranchId = random.nextInt(n);

                String randomAccountsName = UUID.randomUUID().toString().substring(0, 20);
                String randomAccountsAddress = UUID.randomUUID().toString().substring(0, 68);

                String query = "INSERT TO accounts (accid, name, balance, branchid, address) " + "VALUES (%d, %s, %d, %d, %s);".formatted(i, randomAccountsName, 0, randomBranchId, randomAccountsAddress);

                connection.createStatement().execute(query);
            }
        });

    }

    public static void insertBranches(int n) throws SQLException {
        withConnection(connection -> {

            for (int i = 0; i < n; i++) {
                String randomBranchesName = UUID.randomUUID().toString().substring(0, 20);
                String randomAccountsAddress = UUID.randomUUID().toString().substring(0, 72);

                String query = ("INSERT TO branches (branchid, branchname, balance, address) " + "VALUES (%d, %s, %d, %s)").formatted(i, randomBranchesName, 0, randomAccountsAddress);

                connection.createStatement().execute(query);
            }

        });
    }

    public static void insertTellers(int n) throws SQLException {
        withConnection(connection -> {
            final Random random = new Random();

            for (int i = 0; i < n * 10; i++) {
                int randomBranchId = random.nextInt(n);

                String randomTellersName = UUID.randomUUID().toString().substring(0, 20);
                String randomTellersAddress = UUID.randomUUID().toString().substring(0, 68);

                String query = "INSERT TO tellers (tellerid, tellername, balance, branchid, address) " + "VALUES (%d, %s, %d, %d, %s)".formatted(i, randomTellersName, 0, randomBranchId, randomTellersAddress);

                connection.createStatement().execute("");
            }

        });
    }
}
