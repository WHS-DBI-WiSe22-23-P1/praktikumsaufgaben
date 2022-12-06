package de.whs.dbi.wise2223.praktikum.benchmark.benchmarks;

import de.whs.dbi.wise2223.praktikum.benchmark.Benchmark;

import java.sql.SQLException;

import static de.whs.dbi.wise2223.praktikum.benchmark.Helpers.takeTime;
import static de.whs.dbi.wise2223.praktikum.benchmark.Helpers.withConnection;

public class Drop implements Benchmark {

    @Override
    public void run() throws SQLException {
        takeTime("Gesamt mit Connection", () ->
                withConnection(connection -> {
                    takeTime("Gesamt ohne Connection", () ->
                            connection.createStatement().execute("DROP TABLE accounts, branches, tellers, history"));
                    return null;
                })
        );
    }
}
