package io.axoniq.demo.bikerental.bikerental.history;

import io.axoniq.demo.bikerental.bikerental.coreapi.BikeRegisteredEvent;
import io.axoniq.demo.bikerental.bikerental.coreapi.BikeRentedEvent;
import io.axoniq.demo.bikerental.bikerental.coreapi.BikeReturnedEvent;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.eventhandling.Timestamp;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;

@Component
public class BikeHistoryProjection {

    private final BikeHistoryRepository bikeHistoryRepository;

    public BikeHistoryProjection(BikeHistoryRepository bikeHistoryRepository) {
        this.bikeHistoryRepository = bikeHistoryRepository;
    }

    @EventHandler
    public void handle(BikeRegisteredEvent event, @Timestamp Instant timestamp) {
        bikeHistoryRepository.save(new BikeHistory(event.getBikeId(), timestamp, "Bike registered in " + event.getLocation()));
    }

    @EventHandler
    public void handle(BikeRentedEvent event, @Timestamp Instant timestamp) {
        bikeHistoryRepository.save(new BikeHistory(event.getBikeId(), timestamp, "Bike rented out to " + event.getRenter()));
    }

    @EventHandler
    public void handle(BikeReturnedEvent event, @Timestamp Instant timestamp) {
        bikeHistoryRepository.save(new BikeHistory(event.getBikeId(), timestamp, "Bike returned in " + event.getLocation()));
    }

    @QueryHandler(queryName = "locationHistory")
    public List<BikeHistory> findMovements(String bikeId) {
        return bikeHistoryRepository.findByBikeIdOrderById(bikeId);
    }

}
