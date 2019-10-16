package io.axoniq.demo.bikerental.bikerental.history;

import org.axonframework.config.EventProcessingConfigurer;
import org.axonframework.eventhandling.TrackingEventProcessorConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HistoryConfig {

    @Autowired
    public void configure(EventProcessingConfigurer config) {
        config.registerTrackingEventProcessor(
                "io.axoniq.demo.bikerental.bikerental.history",
                org.axonframework.config.Configuration::eventStore,
                c -> TrackingEventProcessorConfiguration
                        .forParallelProcessing(2)
                        .andBatchSize(200)
        );
    }
}
