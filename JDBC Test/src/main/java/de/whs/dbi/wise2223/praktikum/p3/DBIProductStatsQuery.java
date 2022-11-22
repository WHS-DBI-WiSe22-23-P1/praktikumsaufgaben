package de.whs.dbi.wise2223.praktikum.p3;

import java.sql.*;

public class DBIProductStatsQuery {
    private static final String QUERY = "SELECT aname, umsatz FROM (agents JOIN (SELECT aid, SUM(dollars) AS umsatz FROM orders WHERE pid = ? GROUP BY aid) AS umsatz ON agents.aid = umsatz.aid ) ORDER BY umsatz DESC;";
    private Connection connection;

    public DBIProductStatsQuery(Connection connection) {
        this.connection = connection;
    }

    public DBIProductStatsQuery(DatabaseAccessData accessData) {
        try {
            connection = DriverManager.getConnection(accessData.url(), accessData.username(), accessData.password());
        } catch (SQLException e) {
            connection = null;
        }
    }

    public ResultSet getFor(String pid) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(QUERY);
        statement.setString(1, pid);
        return statement.executeQuery();
    }

    public void dispose() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException ignored) {
            }
        }

        connection = null;
    }
}
