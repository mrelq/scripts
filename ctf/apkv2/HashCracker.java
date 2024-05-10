import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class HashCracker {
    private static final Set<Long> targetHashes = new HashSet<>();
    static {
        targetHashes.add(216406515827922L);
        targetHashes.add(240658437888736L);
        targetHashes.add(13348376939555L);
    }

    private static final int NUM_THREADS = Runtime.getRuntime().availableProcessors();

    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        ExecutorService executorService = Executors.newFixedThreadPool(NUM_THREADS);

        long rangeSize = Long.MAX_VALUE / NUM_THREADS;
        for (int i = 0; i < NUM_THREADS; i++) {
            long startRange = i * rangeSize;
            long endRange = (i == NUM_THREADS - 1) ? Long.MAX_VALUE : startRange + rangeSize;
            executorService.execute(new BruteforceTask(startRange, endRange));
        }

        executorService.shutdown();
        try {
            executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        long end = System.currentTimeMillis();
        System.out.println("Time taken: " + (end - start) + " milliseconds");
    }

    private static class BruteforceTask implements Runnable {
        private final long startRange;
        private final long endRange;

        public BruteforceTask(long startRange, long endRange) {
            this.startRange = startRange;
            this.endRange = endRange;
        }

        @Override
        public void run() {
            for (long value = startRange; value < endRange; value++) {
                long hashedValue = hash(value);
                if (targetHashes.contains(hashedValue)) {
                    System.out.println("Original value: " + value + ", Hashed value: " + hashedValue);
                }
            }
        }
    }

    public static Long hash(Long l) {
        Long valueOf = Long.valueOf(((l.longValue() & 2863311530L) >>> 1) | ((l.longValue() & 1431655765) << 1));
        Long valueOf2 = Long.valueOf(((valueOf.longValue() & 3435973836L) >>> 2) | ((valueOf.longValue() & 858993459) << 2));
        Long valueOf3 = Long.valueOf(((valueOf2.longValue() & 4042322160L) >>> 4) | ((valueOf2.longValue() & 252645135) << 4));
        Long valueOf4 = Long.valueOf(((valueOf3.longValue() & 4278255360L) >>> 8) | ((valueOf3.longValue() & 16711935) << 8));
        return Long.valueOf((valueOf4.longValue() >>> 16) | (valueOf4.longValue() << 16));
    }
}