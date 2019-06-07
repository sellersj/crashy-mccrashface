package com.example.crashymccrashface;

/**
 *
 * Since Micrometer 1.1.0 with a property: <code>management.metrics.tags.application=${spring.application.name}</code>
 *
 * Since we're using a newer version, the rest of this class is not needed. Leaving in place for older applications.
 *
 * @author sellersj
 */
// @Configuration
public class MetricsConfig {

    // private Logger logger = LoggerFactory.getLogger(MetricsConfig.class);
    //
    // /** Application name. Defined in application.propreties. */
    // @Value("${spring.application.name}")
    // String appName;
    //
    // /**
    // * Set any required "common tags".
    // *
    // * @return MeterRegistryCustomizer<MeterRegistry>
    // */
    // @Bean
    // public MeterRegistryCustomizer<MeterRegistry> metricsCommonTags() {
    // logger.info("App name: " + appName);
    // return registry -> registry.config().commonTags("application", appName);
    // }

}