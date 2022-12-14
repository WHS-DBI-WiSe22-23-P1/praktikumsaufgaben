public class LoadDriver {
    private static final Random random = new Random();
    private final Duration thinkTime;
    private final Map<String, Pair<Double, Runnable>> transactions;
    private final Map<Phases, Duration> phases;
    private Phases phase;
    private LocalDateTime phaseStartedAt;
    private int transactionsRun = 0;
    private Duration measurePhaseTime;

    private final Map<String, Pair<Integer, Duration>> stats = new HashMap<>();

    public LoadDriver(final Duration thinkTime, final Map<String, Pair<Double, Runnable>> transactions, final Map<Phases, Duration> phases) {
        this.thinkTime = thinkTime;
        this.transactions = transactions;
        this.phases = phases;
    }

    public static void main(String[] args) {
        final Duration thinkTime = Duration.ofMillis(50);
        final Map<Phases, Duration> phases = Phases.defaults();

        System.out.printf("Start LoadDriver: %s \n",  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(System.currentTimeMillis())));

        for(int i = 0; i < 5; i++)
            new Thread(() -> {
                try {
                    withConnection(connection -> {
                        RandomNTPSDatenbankTransaktionen ntpsDatenbankTransaktion = new RandomNTPSDatenbankTransaktionen(new NTPSDatenbankTransaktion(connection), 100);
                        final Map<String, Pair<Double, Runnable>> transactions = new HashMap<>();
                        transactions.put("Kontostands TX", new Pair<>(35.0, ntpsDatenbankTransaktion::getBalanceFromAccount));
                        transactions.put("Einzahlungs TX", new Pair<>(50.0, ntpsDatenbankTransaktion::updateBalance));
                        transactions.put("Analyse TX", new Pair<>(15.0, ntpsDatenbankTransaktion::getNumberOfDeltaBalance));
                        try {
                            new LoadDriver(thinkTime, transactions, phases).drive();
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

            final LocalDateTime transactionStartetAt = LocalDateTime.now();
            final String name = runRandomTransaction();
            if (phase == Phases.MEASURE) {
                Duration transactionDuration = Duration.between(transactionStartetAt, LocalDateTime.now()).abs();
                transactionsRun++;
                Pair<Integer, Duration> myStats = stats.getOrDefault(name, new Pair<>(0, Duration.ZERO));
                stats.put(name, new Pair<>(myStats.getKey() + 1, myStats.getValue().plus(transactionDuration)));
            }

            Thread.sleep(thinkTime.toMillis());
        } while (phase != null);

        printResult();
    }

    private void printResult() {
        System.out.printf("Run %d transactions in %d seconds, that is %d transactions per second!%n", transactionsRun, measurePhaseTime.toSeconds(), transactionsRun / measurePhaseTime.toSeconds());
        for(final String transactionName : stats.keySet()) {
            final Pair<Integer, Duration> myStats = stats.get(transactionName);
            System.out.printf("\tRun %d %s in %d seconds, that is %d transactions per second!%n", myStats.getKey(), transactionName, myStats.getValue().toSeconds(), myStats.getKey() / myStats.getValue().toSeconds());
        }
    }

    private void updatePhase() {
        if (phase == null) {
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

    private String runRandomTransaction() {
        Pair<String, Runnable> namedTransaction = selectRandomTransaction();
        namedTransaction.getValue().run();
        return namedTransaction.getKey();
    }

    private double getTransactionsSum() {
        return transactions.values().stream().map(Pair::getKey).reduce(Double::sum).orElse(0.0);
    }

    private Pair<String, Runnable> selectRandomTransaction() {
        double selected = random.nextDouble(getTransactionsSum());
        for (final String transactionName : transactions.keySet()) {
            Pair<Double, Runnable> transaction = transactions.get(transactionName);
            selected -= transaction.getKey();
            if (selected < 0) return new Pair<>(transactionName, transaction.getValue());
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

        public static @NotNull Map<Phases, Duration> test() {
            final Map<Phases, Duration> defaults = new EnumMap<>(Phases.class);
            defaults.put(WARMUP, Duration.ofMinutes(4/3));
            defaults.put(MEASURE, Duration.ofMinutes(5/3));
            defaults.put(TEARDOWN, Duration.ofMinutes(1/3));

            return defaults;
        }
    }
}
