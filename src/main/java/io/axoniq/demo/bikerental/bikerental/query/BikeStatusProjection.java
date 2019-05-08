package io.axoniq.demo.bikerental.bikerental.query;

import io.axoniq.demo.bikerental.bikerental.coreapi.BikeRegisteredEvent;
import io.axoniq.demo.bikerental.bikerental.coreapi.BikeRentedEvent;
import io.axoniq.demo.bikerental.bikerental.coreapi.BikeReturnedEvent;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile("query")
@Component
public class BikeStatusProjection {

    private final BikeStatusRepository bikeStatusRepository;

    public BikeStatusProjection(BikeStatusRepository bikeStatusRepository) {
        this.bikeStatusRepository = bikeStatusRepository;
    }

    @EventHandler
    public void on(BikeRegisteredEvent event) {
        bikeStatusRepository.save(new BikeStatus(event.getBikeId(), event.getLocation()));
    }

    @EventHandler
    public void on(BikeRentedEvent event) {
        bikeStatusRepository.findById(event.getBikeId()).map(bs -> {
            bs.setRenter(event.getRenter());
            return bs;
        });
    }

    @EventHandler
    public void on(BikeReturnedEvent event) {
        bikeStatusRepository.findById(event.getBikeId()).map(bs -> {
            bs.setRenter(null);
            bs.setLocation(event.getLocation());
            return bs;
        });
    }

    @QueryHandler(queryName = "findAll")
    public Iterable<BikeStatus> findAll() {
        return bikeStatusRepository.findAll();
    }

    @QueryHandler(queryName = "findOne")
    public BikeStatus findOne(String bikeId) {
        return bikeStatusRepository.findById(bikeId).orElse(null);
    }
}
