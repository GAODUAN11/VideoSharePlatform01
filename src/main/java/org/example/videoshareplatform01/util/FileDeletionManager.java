package org.example.videoshareplatform01.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.Objects;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class FileDeletionManager {
    private static final Logger LOGGER = Logger.getLogger(FileDeletionManager.class.getName());
    private static final ScheduledExecutorService EXECUTOR = Executors.newSingleThreadScheduledExecutor(runnable -> {
        Thread thread = new Thread(runnable, "file-deletion-worker");
        thread.setDaemon(true);
        return thread;
    });

    private FileDeletionManager() {}

    public static void schedule(Path path, int maxAttempts, Duration interval) {
        if (path == null || maxAttempts <= 0 || interval == null || interval.isZero() || interval.isNegative()) {
            return;
        }

        DeletionTask task = new DeletionTask(path, maxAttempts);
        ScheduledFuture<?> future = EXECUTOR.scheduleAtFixedRate(
            task,
            interval.toMillis(),
            interval.toMillis(),
            TimeUnit.MILLISECONDS
        );
        task.setFuture(future);
    }

    private static final class DeletionTask implements Runnable {
        private final Path path;
        private final int maxAttempts;
        private final AtomicInteger attempts = new AtomicInteger();
        private volatile ScheduledFuture<?> future;

        private DeletionTask(Path path, int maxAttempts) {
            this.path = path;
            this.maxAttempts = maxAttempts;
        }

        private void setFuture(ScheduledFuture<?> future) {
            this.future = future;
        }

        @Override
        public void run() {
            int currentAttempt = attempts.incrementAndGet();
            if (tryDelete()) {
                cancel();
                return;
            }

            if (currentAttempt >= maxAttempts) {
                LOGGER.warning("Giving up deleting locked file: " + path);
                cancel();
            }
        }

        private boolean tryDelete() {
            try {
                if (Files.deleteIfExists(path)) {
                    LOGGER.info("Deleted file after retry: " + path);
                    return true;
                }

                if (Files.notExists(path)) {
                    return true;
                }
            } catch (IOException ex) {
                LOGGER.log(Level.FINE, "Retry delete failed for " + path + " attempt " + attempts.get(), ex);
            }
            return false;
        }

        private void cancel() {
            ScheduledFuture<?> scheduled = future;
            if (scheduled != null) {
                scheduled.cancel(false);
            }
        }
    }
}

