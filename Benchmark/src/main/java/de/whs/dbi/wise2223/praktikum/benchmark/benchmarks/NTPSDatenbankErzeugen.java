package de.whs.dbi.wise2223.praktikum.benchmark.benchmarks;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import static de.whs.dbi.wise2223.praktikum.benchmark.Helpers.takeTime;
import static de.whs.dbi.wise2223.praktikum.benchmark.Helpers.withConnection;

public class NTPSDatenbankErzeugen {

    static final Random RANDOM = new Random();

    private NTPSDatenbankErzeugen() {
    }

    public static void insertAccounts(int n) throws SQLException {
        final int queries = n * 100_000;
        final int duffConstant = 10;
        withConnection(connection -> takeTime("Gesamt ohne Connection mit %d Tupeln".formatted(queries), () -> {
            for (int i = 0; i < queries; i+= duffConstant) {
                List<String> tupels = new ArrayList<>(duffConstant);

                for(int j = 0; j < duffConstant; j++) {
                    int randomBranchId = RANDOM.nextInt(n);

                    String accountsName = "name------%010d".formatted(i);
                    String accountsAddress = ("accountsaddress-------------------------------------------%010d").formatted(i);

                    tupels.add("(%d, '%s', %d, %d, '%s')".formatted(i + j, accountsName, 0, randomBranchId, accountsAddress));
                }
                String query = "INSERT INTO accounts (accid, name, balance, branchid, address) VALUES %s;".formatted(String.join(", ", tupels));

                connection.createStatement().execute(query);
            }
        }));
    }

    public static void insertBranches(int n) throws SQLException {
        withConnection(connection -> takeTime("Gesamt ohne Connection -" + n + " Tupel", () -> {

            for (int i = 0; i < n; i++) {
                String branchName = "branchname-----%05d".formatted(i);
                String branchAddress = "branchaddress------------------------------------------------------%05d".formatted(i);

                String query = ("INSERT INTO branches (branchid, branchname, balance, address) " + "VALUES (%d, '%s', %d, '%s')").formatted(i, branchName, 0, branchAddress);

                connection.createStatement().execute(query);
            }
        }));
    }

    public static void insertTellers(int n) throws SQLException {
        withConnection(connection -> takeTime("Gesamt ohne Connection - " + (n*10) + " Tupel", () -> {
            for (int i = 0; i < n * 10; i++) {
                int randomBranchId = RANDOM.nextInt(n);

                String tellersName = "tellername--%08d".formatted(i);
                String tellersAddress = ("tellersaddress----------------------------------------------%08d").formatted(i);

                String query = "INSERT INTO tellers (tellerid, tellername, balance, branchid, address) " + "VALUES (%d, '%s', %d, %d, '%s')".formatted(i, tellersName, 0, randomBranchId, tellersAddress);

                connection.createStatement().execute(query);
            }
        }));
    }
}
