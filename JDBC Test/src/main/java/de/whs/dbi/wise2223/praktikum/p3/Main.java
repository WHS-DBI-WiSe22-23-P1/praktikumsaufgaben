package de.whs.dbi.wise2223.praktikum.p3;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws SQLException {
        DatabaseAccessData databaseAccessData = new DatabaseAccessDataBuilder().setUrl("jdbc:mysql://localhost:49153/dbi").setUsername("dbi").setPassword("dbi_pass").createDatabaseAccessData();

        DBIProductStatsQuery dbiProductStatsQuery = new DBIProductStatsQuery(databaseAccessData);

        System.out.println("Bitte gib die zu analysierende pid ein:\n");
        ResultSet results = dbiProductStatsQuery.getFor(new Scanner(System.in).next());

        while (results.next()) {
            System.out.printf("%s: %s%n", results.getString("aname"), results.getString("umsatz"));
        }

        dbiProductStatsQuery.dispose();
    }
}
