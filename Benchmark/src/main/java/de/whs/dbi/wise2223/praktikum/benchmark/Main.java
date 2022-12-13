package de.whs.dbi.wise2223.praktikum.benchmark;

import de.whs.dbi.wise2223.praktikum.benchmark.benchmarks.Create;
import de.whs.dbi.wise2223.praktikum.benchmark.benchmarks.Drop;
import de.whs.dbi.wise2223.praktikum.benchmark.benchmarks.NTPSDatenbankErzeugen;

import java.util.HashMap;
import java.util.Map;

import static de.whs.dbi.wise2223.praktikum.benchmark.Helpers.*;

public class Main {
    static final Map<String, Benchmark> benchmarks = new HashMap<>() {{
        put("create", new Create());
        put("drop", new Drop());
        put("ntps.branches", withIntInput(NTPSDatenbankErzeugen::insertBranches));
        put("ntps.accounts", withIntInput(NTPSDatenbankErzeugen::insertAccounts));
        put("ntps.tellers", withIntInput(NTPSDatenbankErzeugen::insertTellers));
    }};

    public static void main(String[] args) throws Exception {
        printOptions();
        withInput(input -> {
            while (input.hasNext()) {
                final String prompt = input.next();

                if(benchmarks.containsKey(prompt))
                    benchmarks.get(prompt).run();
                else if("exit".equals(prompt))
                    break;
                else if ("options".equals(prompt))
                    printOptions();
                else
                    System.out.printf("%s is not a valid option.%n", prompt);
            }
        });
    }

    private static void printOptions() {
        System.out.printf("Wähle zwischen %s:%n", String.join(", ", benchmarks.keySet()));
    }
}
