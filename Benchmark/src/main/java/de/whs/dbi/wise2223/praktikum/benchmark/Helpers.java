package de.whs.dbi.wise2223.praktikum.benchmark;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Scanner;

import static de.whs.dbi.wise2223.praktikum.benchmark.ConnectionData.*;

public class Helpers {

    public static Benchmark withIntInput(BenchmarkWithIntInput benchmark) {
        return () -> withInput(input -> benchmark.run(input.nextInt()));
    }

    public static void withInput(InputFunction action) throws Exception {
        Scanner scanner = new Scanner(new BufferedReader(new InputStreamReader(System.in)));
        action.call(scanner);
        scanner.close();
    }

    public static void takeTime(String name, TimedFunction action) throws SQLException {
        final LocalDateTime startTime = LocalDateTime.now();

        action.call();

        final LocalDateTime endTime = LocalDateTime.now();
        final Duration duration = Duration.between(startTime, endTime);
        System.out.printf("%d Sekunden %d Millisekunden for %s%n", duration.getSeconds(), duration.getNano() / 1_000_000, name);
    }

    public static void withConnection(ConnectionFunction action) throws SQLException {
        Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
        action.call(connection);
        connection.close();
    }

    public interface BenchmarkWithIntInput {
        void run(int input) throws Exception;
    }

    public interface InputFunction {
        void call(Scanner input) throws Exception;
    }

    public interface TimedFunction {
        void call() throws SQLException;
    }

    public interface ConnectionFunction {
        void call(Connection connection) throws SQLException;
    }
}
