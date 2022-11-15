package de.whs.dbi.wise2223.praktikum.benchmark;

import de.whs.dbi.wise2223.praktikum.benchmark.benchmarks.Create;
import de.whs.dbi.wise2223.praktikum.benchmark.benchmarks.Drop;
import de.whs.dbi.wise2223.praktikum.benchmark.benchmarks.NTPSDatenbankErzeugen;

import java.util.HashMap;
import java.util.Map;

import static de.whs.dbi.wise2223.praktikum.benchmark.Helpers.withInput;
import static de.whs.dbi.wise2223.praktikum.benchmark.Helpers.withIntInput;

public class Main {
    static final Map<String, Benchmark> benchmarks = new HashMap<>() {{
        put("create", new Create());
        put("drop", new Drop());
        put("ntps.accounts", withIntInput(NTPSDatenbankErzeugen::insertAccounts));
        put("ntps.branches", withIntInput(NTPSDatenbankErzeugen::insertBranches));
        put("ntps.tellers", withIntInput(NTPSDatenbankErzeugen::insertTellers));
    }};

    public static void main(String[] args) throws Exception {
        withInput(input -> {
            while (input.hasNext()) {
                Benchmark benchmark = benchmarks.get(input.next());
                if (benchmark != null) {
                    benchmark.run();
                }
            }
        });
    }
}
