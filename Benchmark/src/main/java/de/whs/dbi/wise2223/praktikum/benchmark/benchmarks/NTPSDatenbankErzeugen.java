package de.whs.dbi.wise2223.praktikum.benchmark.benchmarks;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static de.whs.dbi.wise2223.praktikum.benchmark.Helpers.takeTime;
import static de.whs.dbi.wise2223.praktikum.benchmark.Helpers.withConnection;

public class NTPSDatenbankErzeugen {

    static final Random RANDOM = new Random();

    private NTPSDatenbankErzeugen() {
    }

    public static void insertAccounts(int n) throws SQLException {
        final int count = n * 100_000;
        final int threadsCount = 10;
        final int tupelsPerThread = count / threadsCount;
        withConnection(connection -> {
            takeTime("Gesamt ohne Connection mit %d Tupeln".formatted(count), () -> {
                final CompletableFuture<Boolean>[] threads = new CompletableFuture[threadsCount];

                for (int i = 0; i < threadsCount; i++) {
                    threads[i] = insertAccountsAsync(tupelsPerThread, n, i * tupelsPerThread, connection);
                }

                for (int i = 0; i < threadsCount; i++) {
                    try {
                        threads[i].get();
                    } catch (InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                    }
                }
            });
            return null;
        });
    }

    public static void insertBranches(int n) throws SQLException {
        withConnection(connection -> {
            takeTime("Gesamt ohne Connection -" + n + " Tupel", () -> {

                for (int i = 0; i < n; i++) {
                    String branchName = "branchname-----%05d".formatted(i);
                    String branchAddress = "branchaddress------------------------------------------------------%05d".formatted(i);

                    String query = ("INSERT INTO branches (branchid, branchname, balance, address) " + "VALUES (%d, '%s', %d, '%s')").formatted(i, branchName, 0, branchAddress);

                    connection.createStatement().execute(query);
                }
            });
            return null;
        });
    }

    public static void insertTellers(int n) throws SQLException {
        withConnection(connection -> {
            takeTime("Gesamt ohne Connection - " + (n * 10) + " Tupel", () -> {
                for (int i = 0; i < n * 10; i++) {
                    int randomBranchId = RANDOM.nextInt(n);

                    String tellersName = "tellername--%08d".formatted(i);
                    String tellersAddress = ("tellersaddress----------------------------------------------%08d").formatted(i);

                    String query = "INSERT INTO tellers (tellerid, tellername, balance, branchid, address) " + "VALUES (%d, '%s', %d, %d, '%s')".formatted(i, tellersName, 0, randomBranchId, tellersAddress);

                    connection.createStatement().execute(query);
                }
            });
            return null;
        });
    }

    public static void insertHistory(int accId, int tellerId, int branchId, int delta, int updatedAccountBalance, String comment) throws SQLException {
        withConnection(connection -> {
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

            return null;
        });
    }

    private static CompletableFuture<Boolean> insertAccountsAsync(final int count, final int n, final int firstIndex, final Connection connection) {
        return CompletableFuture.supplyAsync(() -> {
            final String[] tupels = new String[count];
            for (int i = 0; i < count; i++) {

                int randomBranchId = RANDOM.nextInt(n);

                String accountsName = "name------%010d".formatted(i + firstIndex);
                String accountsAddress = ("accountsaddress-------------------------------------------%010d").formatted(i);

                tupels[i] = "(%d, '%s', %d, %d, '%s')".formatted(i + firstIndex, accountsName, 0, randomBranchId, accountsAddress);
            }

            String query = "INSERT INTO accounts (accid, name, balance, branchid, address) VALUES %s;".formatted(String.join(", ", tupels));

            try {
                return connection.createStatement().execute(query);
            } catch (SQLException e) {
                e.printStackTrace();
                return null;
            }
        });
    }
}
