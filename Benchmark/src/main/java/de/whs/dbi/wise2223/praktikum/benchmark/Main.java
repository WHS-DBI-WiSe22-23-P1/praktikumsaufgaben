package de.whs.dbi.wise2223.praktikum.benchmark;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Scanner;

import java.sql.Connection;
import java.sql.DriverManager;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Random;
import java.util.concurrent.Callable;

public class Main {

    public static final String URL = "jdbc:mysql://localhost:33060/benchmark";
    public static final String URL_REMOTE = "jdbc:mysql://192.168.122.45:3306/benchmark";
    public static final String USER = "dbi";
    public static final String PASSWORD = "dbi_pass";

    public static final Random RANDOM = new Random();

    public static void main(String[] args) throws Exception {
        //final Scanner BufferedReader  = new Scanner (new BufferedReader(new InputStreamReader(System.in)));

        //System.out.print("Eingabe von n: ");
        int n = 1;//BufferedReader.nextInt();

        //dropTable();
        //createTables();

        //insertBranches(n);
        insertAccounts(n);
        //insertTellers(n);
    }


    public static void createTables() throws Exception {
        takeTime("Gesamt mit Connection", () -> {
            withConnection(connection -> {

                String[] queries = new String[]{"create table branches( branchid int not null, branchname char(20) not null, balance int not null, address char(72) not null, primary key (branchid) );",
                        "create table accounts( accid int not null, name char(20) not null, balance int not null, branchid int not null, address char(68) not null, primary key (accid), foreign key (branchid) references branches(branchid) );",
                        "create table tellers( tellerid int not null, tellername char(20) not null, balance int not null, branchid int not null, address char(68) not null, primary key (tellerid), foreign key (branchid) references branches(branchid) );",
                        "create table history( accid int not null, tellerid int not null, delta int not null, branchid int not null, accbalance int not null, cmmnt char(30) not null, foreign key (accid) references accounts(accid), foreign key (tellerid) references tellers(tellerid), foreign key (branchid) references branches(branchid) );"};

                takeTime("Gesamt ohne Connection", () -> {

                    for (final String query : queries) {
                        takeTime(query, () -> {
                            connection.createStatement().execute(query);
                            return null;
                        });
                    }
                    return null;
                });
            });

            return null;
        });
    }

    public static void dropTable() throws Exception {
        withConnection(connection -> {
            connection.createStatement().execute("DROP TABLE accounts, branches, tellers, history");
        });
    }


    public static void insertAccounts(int n) throws Exception {
        takeTime("Gesamt mit Connection bei INSERT ACCOUNTS mit " + (n * 100000) + " Tupeln", () -> {

            withConnection(connection -> {

                takeTime("Gesamt ohne Connection bei INSERT ACCOUNTS mit " + (n * 100000) + " Tupeln", () -> {

                    for(int i = 0;i < n * 100000;i++) {
                        int randomBranchId = 0; //RANDOM.nextInt(n);

                        String accountsName = "name------%010d".formatted(i);
                        String accountsAddress = ("accountsaddress-------------------------------------------%010d").formatted(i);

                        String query = "INSERT INTO accounts (accid, name, balance, branchid, address) " +
                                "VALUES (%d, '%s', %d, %d, '%s');".formatted(i, accountsName, 0, randomBranchId, accountsAddress);

                        connection.createStatement().execute(query);
                    }

                    connection.close();

                    return null;
                });
            });

            return null;
        });
    }

    public static void insertBranches(int n) throws Exception {
        takeTime("Gesamt mit Connection bei INSERT BRANCHES mit " + n + " Tupeln", () -> {

            withConnection(connection -> {

                takeTime("Gesamt ohne Connection bei INSERT BRANCHES mit " + n + " Tupeln", () -> {

                    for(int i = 0;i < n;i++) {
                        String branchName = "branchname-----%05d".formatted(i);
                        String branchAddress = "branchaddress------------------------------------------------------%05d".formatted(i);

                        String query = ("INSERT INTO branches (branchid, branchname, balance, address) " +
                                "VALUES (%d, '%s', %d, '%s')").formatted(i, branchName, 0, branchAddress);

                        connection.createStatement().execute(query);
                    }

                    connection.close();

                    return null;
                });
            });

            return null;
        });
    }

    public static void insertTellers(int n) throws Exception {
        takeTime("Gesamt mit Connection bei INSERT TELLERS mit " + (n * 10) + " Tupeln", () -> {

            withConnection(connection -> {

                takeTime("Gesamt ohne Connection bei INSERT TELLERS mit " + (n * 10) + " Tupeln", () -> {

                    for(int i = 0;i < n * 10;i++) {
                        int randomBranchId = RANDOM.nextInt(n);

                        String tellersName = "tellername--%08d".formatted(i);
                        String tellersAddress = ("tellersaddress----------------------------------------------%08d").formatted(i);

                        String query = "INSERT INTO tellers (tellerid, tellername, balance, branchid, address) " +
                                "VALUES (%d, '%s', %d, %d, '%s')".formatted(i, tellersName, 0, randomBranchId, tellersAddress);

                        connection.createStatement().execute(query);
                    }

                    connection.close();

                    return null;
                });
            });

            return null;
        });
    }


    static Duration takeTime(String name, Callable<Void> action) throws Exception {
        final LocalDateTime startTime = LocalDateTime.now();

        action.call();

        final LocalDateTime endTime = LocalDateTime.now();
        final Duration duration = Duration.between(startTime, endTime);
        System.out.printf("%d Sekunden %d Millisekunden fuer %s%n", duration.getSeconds(), duration.getNano() / 1_000_000, name);

        return duration;
    }

    static void withConnection(ConnectionFunction action) throws Exception {
        Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
        action.call(connection);
        connection.close();
    }


    interface ConnectionFunction {
        void call(Connection connection) throws Exception;
    }
}
