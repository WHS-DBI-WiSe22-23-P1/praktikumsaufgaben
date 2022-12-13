package de.whs.dbi.wise2223.praktikum.benchmark.test;

import de.whs.dbi.wise2223.praktikum.benchmark.Benchmark;
import de.whs.dbi.wise2223.praktikum.benchmark.benchmarks.NTPSDatenbankTransaktion;
import static de.whs.dbi.wise2223.praktikum.benchmark.Helpers.withIntInputResult;

public class TransaktionTest implements Benchmark {

    @Override
    public void run() throws Exception {
        System.out.print("Eingabe der accId: ");
        System.out.println("accountsBalance " + withIntInputResult(NTPSDatenbankTransaktion::getBalanceFromAccount));

        System.out.println("Eingabe der deltaBalance");
        System.out.println("numberOfDeltaBalances " + withIntInputResult(NTPSDatenbankTransaktion::getNumberOfDeltaBalance));
    }
}
