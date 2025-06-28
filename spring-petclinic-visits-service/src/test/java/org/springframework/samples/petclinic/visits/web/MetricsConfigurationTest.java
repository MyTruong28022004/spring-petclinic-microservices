package org.springframework.samples.petclinic.visits.web;

import io.micrometer.core.aop.TimedAspect;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.Test;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.samples.petclinic.visits.config.MetricConfig;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class MetricsConfigurationTest {
    @Test
    public void testMetricsConfig() {
        MetricConfig config = new MetricConfig();
        MeterRegistryCustomizer<MeterRegistry> customizer = config.metricsCommonTags();
        MeterRegistry registry = new SimpleMeterRegistry();
        customizer.customize(registry);

        registry.counter("test.counter").increment();
        assertEquals("petclinic", registry.find("test.counter").tags("application", "petclinic").counter().getId().getTag("application"));

        TimedAspect timedAspect = config.timedAspect(registry);
        assertNotNull(timedAspect, "TimedAspect should not be null");
    }
}
