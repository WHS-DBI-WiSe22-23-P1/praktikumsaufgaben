package de.whs.dbi.wise2223.praktikum.benchmark;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

import static de.whs.dbi.wise2223.praktikum.benchmark.ConnectionData.*;

public class Helpers {
    private static Scanner scanner = null;

    public static Benchmark withIntInput(BenchmarkWithIntInput benchmark) {
        return () -> withInput(input -> benchmark.run(input.nextInt()));
    }

    public static <T> T withIntInputResult(BenchmarkWithIntInputResult<T> benchmark) {
        try {
            return withInputResult(input -> benchmark.run(input.nextInt()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void withInput(InputFunction action) throws Exception {
        if(scanner != null) {
            action.call(scanner);
            return;
        }

        scanner = new Scanner(new BufferedReader(new InputStreamReader(System.in)));
        action.call(scanner);
        scanner.close();
        scanner = null;
    }

    public static <T> T withInputResult(InputFunctionResult<T> action) throws Exception {
        if(scanner != null)
            return action.call(scanner);

        scanner = new Scanner(new BufferedReader(new InputStreamReader(System.in)));
        T result = action.call(scanner);
        scanner.close();
        scanner = null;

        return result;
    }

    public static void takeTime(String name, TimedFunction action) throws SQLException {
        final LocalDateTime startTime = LocalDateTime.now();

        action.call();

        final LocalDateTime endTime = LocalDateTime.now();
        final Duration duration = Duration.between(startTime, endTime);
        System.out.printf("%d Sekunden %d Millisekunden for %s%n", duration.getSeconds(), duration.getNano() / 1_000_000, name);
    }

    public static <T> T withConnection(ConnectionFunction<T> action) throws SQLException {
        Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
        final T res = action.call(connection);
        connection.close();

        return res;
    }

    public interface BenchmarkWithIntInput {
        void run(int input) throws Exception;
    }

    public interface BenchmarkWithIntInputResult<T> {
        T run(int input) throws Exception;
    }

    public interface InputFunction {
        void call(Scanner input) throws Exception;
    }

    public interface InputFunctionResult<T> {
        T call(Scanner input) throws Exception;
    }

    public interface TimedFunction {
        void call() throws SQLException;
    }

    public interface ConnectionFunction<T> {
        T call(Connection connection) throws SQLException;
    }
}
