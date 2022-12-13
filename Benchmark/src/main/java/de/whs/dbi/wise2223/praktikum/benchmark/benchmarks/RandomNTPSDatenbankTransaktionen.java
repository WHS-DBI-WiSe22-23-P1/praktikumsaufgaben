package de.whs.dbi.wise2223.praktikum.benchmark.benchmarks;

import java.sql.SQLException;
import java.util.Random;

public class RandomNTPSDatenbankTransaktionen {
    public static final int DELTA_LIMIT = 10_000;
    public final int acidLimit;
    public final int branchesLimit;
    public final int tellersLimit;

    private final NTPSDatenbankTransaktion ntpsDatenbankTransaktion;

    private final Random random = new Random();

    public RandomNTPSDatenbankTransaktionen(NTPSDatenbankTransaktion ntpsDatenbankTransaktion, final int n) {
        this.acidLimit = 100_000 * n;
        this.branchesLimit = n;
        this.tellersLimit = 10 * n;
        this.ntpsDatenbankTransaktion = ntpsDatenbankTransaktion;
    }

    public void getBalanceFromAccount() {
        try {
            ntpsDatenbankTransaktion.getBalanceFromAccount(random.nextInt(acidLimit));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateBalance() {
        try {
            ntpsDatenbankTransaktion.updateBalance(random.nextInt(acidLimit), random.nextInt(tellersLimit), random.nextInt(branchesLimit), random.nextInt(1, DELTA_LIMIT), "------------------------------");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void getNumberOfDeltaBalance() {
        try {
            ntpsDatenbankTransaktion.getNumberOfDeltaBalance(random.nextInt(1, DELTA_LIMIT));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
