package com.example.crashymccrashface;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.micrometer.core.instrument.MeterRegistry;

/**
 *
 * @author sellersj
 */
@Configuration
public class MetricsConfig {

    /** Application name. Defined in application.propreties. */
    @Value("${spring.application.name}")
    String appName;

    /**
     * Set any required "common tags".
     *
     * @return MeterRegistryCustomizer<MeterRegistry>
     */
    @Bean
    public MeterRegistryCustomizer<MeterRegistry> metricsCommonTags() {
        System.out.println("App name: " + appName);
        return registry -> registry.config().commonTags("application", appName);
    }

}