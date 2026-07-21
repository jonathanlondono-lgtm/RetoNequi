package co.com.nequi.r2dbc.config;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryRegistry;
import io.github.resilience4j.timelimiter.TimeLimiter;
import io.github.resilience4j.timelimiter.TimeLimiterRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CircuitBreakerConfig {
    private static final String INSTANCE_NAME = "databaseNequi";

    @Bean
    public CircuitBreaker databaseCircuitBreaker(CircuitBreakerRegistry registry) {
        return registry.circuitBreaker(INSTANCE_NAME);
    }

    @Bean
    public TimeLimiter databaseTimeLimiter(TimeLimiterRegistry registry) {
        return registry.timeLimiter(INSTANCE_NAME);
    }

    @Bean
    public Retry databaseRetry(RetryRegistry registry) {
        return registry.retry(INSTANCE_NAME);
    }
}
