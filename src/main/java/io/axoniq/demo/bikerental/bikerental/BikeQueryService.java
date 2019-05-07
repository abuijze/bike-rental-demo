package io.axoniq.demo.bikerental.bikerental;

import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Service;

@Service
public class BikeQueryService {

    private final BikeRepository bikeRepository;

    public BikeQueryService(BikeRepository bikeRepository) {
        this.bikeRepository = bikeRepository;
    }

    @QueryHandler(queryName = "findAll")
    public Iterable<Bike> findAll() {
        return bikeRepository.findAll();
    }

    @QueryHandler(queryName = "findOne")
    public Bike findOne(String bikeId) {
        return bikeRepository.findById(bikeId).orElse(null);
    }
}
