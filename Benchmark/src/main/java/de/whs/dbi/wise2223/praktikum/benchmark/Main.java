package de.whs.dbi.wise2223.praktikum.benchmark;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Scanner;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.Callable;

public class Main {

    public static final String URL = "jdbc:mysql://localhost:49153/benchmark";
    public static final String USER = "dbi";
    public static final String PASSWORD = "dbi_pass";

    public static void main(String[] args) throws Exception {
        Scanner BufferedReader  = new Scanner (new BufferedReader(new InputStreamReader(System.in)));
        int n = BufferedReader.nextInt();
        createTables();
    }

    public static void createTables() throws Exception {
        takeTime("Gesamt mit Connection", () -> {
            Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);

            String[] queries = new String[]{"create table branches( branchid int not null, branchname char(20) not null, balance int not null, address char(72) not null, primary key (branchid) );", "create table accounts( accid int not null, name char(20) not null, balance int not null, branchid int not null, address char(68) not null, primary key (accid), foreign key (branchid) references branches(branchid) );", "create table tellers( tellerid int not null, tellername char(20) not null, balance int not null, branchid int not null, address char(68) not null, primary key (tellerid), foreign key (branchid) references branches(branchid) );", "create table history( accid int not null, tellerid int not null, delta int not null, branchid int not null, accbalance int not null, cmmnt char(30) not null, foreign key (accid) references accounts(accid), foreign key (tellerid) references tellers(tellerid), foreign key (branchid) references branches(branchid) );"};

            takeTime("Gesamt ohne Connection", () -> {

                for (final String query : queries) {
                    takeTime(query, () -> {
                        connection.createStatement().execute(query);
                        return null;
                    });
                }
                return null;
            });
            return null;
        });
    }

    static Duration takeTime(String name, Callable<Void> action) throws Exception {
        final LocalDateTime startTime = LocalDateTime.now();

        action.call();

        final LocalDateTime endTime = LocalDateTime.now();
        final Duration duration = Duration.between(startTime, endTime);
        System.out.printf("%d Sekunden %d Millisekunden für %s%n", duration.getSeconds(), duration.getNano() / 1_000_000, name);

        return duration;
    }

    public static void insertAccounts(int n) throws SQLException {
        Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);

        final Random random = new Random();

        for(int i = 0;i < n * 100000;i++) {
            int randomBranchId = random.nextInt(n);

            String randomAccountsName = UUID.randomUUID().toString().substring(0, 20);
            String randomAccountsAddress = UUID.randomUUID().toString().substring(0, 68);

            String query = "INSERT TO accounts (accid, name, balance, branchid, address) " +
                    "VALUES (%d, %s, %d, %d, %s);".formatted(i, randomAccountsName, 0, randomBranchId, randomAccountsAddress);

            connection.createStatement().execute(query);
        }

        connection.close();
    }

    public static void insertBranches(int n) throws SQLException {
        Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);

        for(int i = 0;i < n;i++) {
            String randomBranchesName = UUID.randomUUID().toString().substring(0, 20);
            String randomAccountsAddress = UUID.randomUUID().toString().substring(0, 72);

            String query = ("INSERT TO branches (branchid, branchname, balance, address) " +
                    "VALUES (%d, %s, %d, %s)").formatted(i, randomBranchesName, 0, randomAccountsAddress);

            connection.createStatement().execute(query);
        }

        connection.close();
    }

    public static void insertTellers(int n) throws SQLException {
        Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);

        final Random random = new Random();

        for(int i = 0;i < n * 10;i++) {
            int randomBranchId = random.nextInt(n);

            String randomTellersName = UUID.randomUUID().toString().substring(0, 20);
            String randomTellersAddress = UUID.randomUUID().toString().substring(0, 68);

            String query = "INSERT TO tellers (tellerid, tellername, balance, branchid, address) " +
                    "VALUES (%d, %s, %d, %d, %s)".formatted(i, randomTellersName, 0, randomBranchId, randomTellersAddress);

            connection.createStatement().execute("");
        }

        connection.close();
    }

}
