package com.dbi;

import java.sql.*;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws SQLException {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Product Id eingeben: ");

        if(!scanner.hasNext()) {
            System.out.println("Fehler: Keine Product Id eingeben!");
            return;
        }

        String productId = scanner.next();

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:33060/dbi",
                    "dbi",
                    "dbi_pass");

            System.out.printf("%-5s %-8s %-8s%n", "aid", "aname", "umsatz");

            String query = "SELECT agents.aid, aname, SUM(dollars) AS umsatz FROM orders " +
                    "INNER JOIN agents ON agents.aid = orders.aid WHERE pid = ? " +
                    "GROUP BY aid ORDER BY umsatz DESC";

            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, productId);

            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()) {
                System.out.printf("%-5s %-8s %-6.2f%n",
                        resultSet.getString(1),
                        resultSet.getString(2),
                        resultSet.getDouble(3));
            }
            resultSet.close();
        }
        catch (SQLException e) {
            System.out.println("SQLException: " + e);
        }
        finally {
            if(connection != null)
                connection.close();

            if(preparedStatement != null)
               preparedStatement.close();
        }
    }
}
