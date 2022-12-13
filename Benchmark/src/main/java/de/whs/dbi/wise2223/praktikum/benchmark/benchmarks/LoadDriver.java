package de.whs.dbi.wise2223.praktikum.benchmark.benchmarks;

import javafx.util.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.Map;
import java.util.Random;
import static de.whs.dbi.wise2223.praktikum.benchmark.Helpers.withConnection;

public class LoadDriver {
    private static final Random random = new Random();
    private final Duration thinkTime;
    private final Pair<Double, Runnable>[] transactions;
    private final Map<Phases, Duration> phases;
    private Phases phase;
    private LocalDateTime phaseStartedAt;
    private int transactionsRun = 0;
    private Duration measurePhaseTime;

    public LoadDriver(Duration thinkTime, Pair<Double, Runnable>[] transactions, Map<Phases, Duration> phases) {
        this.thinkTime = thinkTime;
        this.transactions = transactions;
        this.phases = phases;
    }

    public static void main(String[] args) {
        for(int i = 0; i < 5; i++)
            new Thread(() -> {
                try {
                    withConnection(connection -> {
                        RandomNTPSDatenbankTransaktionen ntpsDatenbankTransaktion = new RandomNTPSDatenbankTransaktionen(new NTPSDatenbankTransaktion(connection), 100);
                        try {
                            new LoadDriver(Duration.ofMillis(50), new Pair[]{new Pair<Double, Runnable>(35.0, ntpsDatenbankTransaktion::getBalanceFromAccount),
                                    new Pair<Double, Runnable>(50.0, ntpsDatenbankTransaktion::updateBalance), new Pair<Double, Runnable>(15.0, ntpsDatenbankTransaktion::getNumberOfDeltaBalance)}, Phases.defaults()).drive();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                        return null;
                    });
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }).start();

    }

    public void drive() throws InterruptedException {
        do {
            updatePhase();

            runRandomTransaction();
            if (phase == Phases.MEASURE) transactionsRun++;

            Thread.sleep(thinkTime.toMillis());
        } while (phase != null);

        printResult();
    }

    private void printResult() {
        System.out.printf("Transactions run: %d%n Transactions per second: %f%n", transactionsRun, transactionsRun / measurePhaseTime.toSeconds());
    }

    private void updatePhase() {
        if(phase == null) {
            phase = Phases.after(null);
            phaseStartedAt = LocalDateTime.now();

            return;
        }

        final Duration phaseLength = Duration.between(phaseStartedAt, LocalDateTime.now()).abs();
        if (phases.get(phase).compareTo(phaseLength) < 0) {
            if (phase == Phases.MEASURE) measurePhaseTime = phaseLength;

            phase = Phases.after(phase);
            phaseStartedAt = LocalDateTime.now();
        }
    }

    private void runRandomTransaction() {
        selectRandomTransaction().run();
    }

    private double getTransactionsSum() {
        return Arrays.stream(transactions).map(transactions -> transactions.getKey()).reduce(Double::sum).orElse(0.0);
    }

    private Runnable selectRandomTransaction() {
        double selected = random.nextDouble(getTransactionsSum());
        for (final Pair<Double, Runnable> transaction : transactions) {
            selected -= transaction.getKey();
            if (selected < 0) return transaction.getValue();
        }

        throw new IllegalStateException("");
    }

    public enum Phases {
        WARMUP, MEASURE, TEARDOWN;

        public static @Nullable Phases after(@Nullable Phases prev) {
            if (prev == null) {
                return WARMUP;
            }

            return switch (prev) {
                case WARMUP -> MEASURE;
                case MEASURE -> TEARDOWN;
                case TEARDOWN -> null;
            };
        }

        public static @NotNull Map<Phases, Duration> defaults() {
            final Map<Phases, Duration> defaults = new EnumMap<>(Phases.class);
            defaults.put(WARMUP, Duration.ofMinutes(4));
            defaults.put(MEASURE, Duration.ofMinutes(5));
            defaults.put(TEARDOWN, Duration.ofMinutes(1));

            return defaults;
        }
    }
}
