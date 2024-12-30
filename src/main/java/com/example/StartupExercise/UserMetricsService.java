package com.example.StartupExercise;


import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Service;

@Service
public class UserMetricsService {

    private final Counter userCreatedCounter;
    private final Counter userUpdatedCounter;
    private final Counter userDeletedCounter;

    public UserMetricsService(MeterRegistry meterRegistry) {
        this.userCreatedCounter = Counter.builder("user_created_total")
                .description("Total number of users created")
                .register(meterRegistry);

        this.userUpdatedCounter = Counter.builder("user_updated_total")
                .description("Total number of users updated")
                .register(meterRegistry);

        this.userDeletedCounter = Counter.builder("user_deleted_total")
                .description("Total number of users deleted")
                .register(meterRegistry);
    }

    public void incrementUserCreated() {
        userCreatedCounter.increment();
    }

    public void incrementUserUpdated() {
        userUpdatedCounter.increment();
    }

    public void incrementUserDeleted() {
        userDeletedCounter.increment();
    }
}