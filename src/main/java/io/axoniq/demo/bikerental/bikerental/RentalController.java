package io.axoniq.demo.bikerental.bikerental;

import io.axoniq.demo.bikerental.bikerental.coreapi.RegisterBikeCommand;
import io.axoniq.demo.bikerental.bikerental.coreapi.RentBikeCommand;
import io.axoniq.demo.bikerental.bikerental.coreapi.ReturnBikeCommand;
import io.axoniq.demo.bikerental.bikerental.history.BikeHistory;
import io.axoniq.demo.bikerental.bikerental.query.BikeStatus;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.axonframework.queryhandling.SubscriptionQueryBackpressure;
import org.axonframework.queryhandling.SubscriptionQueryResult;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;

@RestController
@RequestMapping("/bikes")
public class RentalController {

    private final CommandGateway commandGateway;
    private final QueryGateway queryGateway;

    public RentalController(CommandGateway commandGateway, QueryGateway queryGateway) {
        this.commandGateway = commandGateway;
        this.queryGateway = queryGateway;
    }

    @PutMapping
    public CompletableFuture<String> register(@RequestParam("location") String location) {
        return register(UUID.randomUUID().toString(), location);
    }

    @PutMapping("{bikeId}")
    public CompletableFuture<String> register(@PathVariable("bikeId") String bikeId,
                                              @RequestParam("location") String location) {
        return commandGateway.send(new RegisterBikeCommand(bikeId, location));
    }

    @PostMapping("/{bikeId}/rent")
    public CompletableFuture<String> rent(@PathVariable("bikeId") String bikeId,
                                          @RequestParam("renter") String renter) {
        return commandGateway.send(new RentBikeCommand(bikeId, renter))
                             .thenApply(r -> "Bike rented to " + renter)
                             .exceptionally(Throwable::getMessage);
    }

    @PostMapping("/{bikeId}/return")
    public CompletableFuture<String> returnBike(@PathVariable("bikeId") String bikeId,
                                                @RequestParam("location") String location) {
        return commandGateway.send(new ReturnBikeCommand(bikeId, location))
                             .thenApply(r -> "Bike returned in " + location)
                             .exceptionally(Throwable::getMessage);
    }

    @GetMapping()
    public CompletableFuture<List<BikeStatus>> findAll() {
        return queryGateway.query("findAll", null, ResponseTypes.multipleInstancesOf(BikeStatus.class));
    }

    @GetMapping("/{bikeId}")
    public CompletableFuture<BikeStatus> findOne(@PathVariable("bikeId") String bikeId) {
        return queryGateway.query("findOne", bikeId, BikeStatus.class);
    }

    @GetMapping("/{bikeId}/history")
    public CompletableFuture<List<BikeHistory>> findMovements(@PathVariable("bikeId") String bikeId) {
        return queryGateway.query("locationHistory", bikeId, ResponseTypes.multipleInstancesOf(BikeHistory.class));
    }

    @GetMapping(value = "/{bikeId}/watch", produces = "text/event-stream")
    public Flux<BikeHistory> watch(@PathVariable("bikeId") String bikeId) {
        SubscriptionQueryResult<List<BikeHistory>, BikeHistory> response = queryGateway.subscriptionQuery("locationHistory", bikeId,
                                                                                                          ResponseTypes.multipleInstancesOf(BikeHistory.class),
                                                                                                          ResponseTypes.instanceOf(BikeHistory.class),
                                                                                                          SubscriptionQueryBackpressure.defaultBackpressure()
        );
        return response.initialResult().flatMapMany(Flux::fromIterable).concatWith(response.updates());
    }

    @PostMapping(value = "/generate")
    public void generateData(@RequestParam("bikes") int bikeCount, @RequestParam("rentals") int rentalCount) {
        for (int i = 0; i < bikeCount; i++) {
            String bikeId = UUID.randomUUID().toString();
            CompletableFuture<?> result = commandGateway.send(new RegisterBikeCommand(bikeId, randomLocation()));
            for (int c = 0; c < rentalCount; c++) {
                result = result.thenCompose(r -> commandGateway.send(new RentBikeCommand(bikeId, randomRenter())))
                               .thenCompose(r -> commandGateway.send(new ReturnBikeCommand(bikeId, randomLocation())));
            }
        }
    }

    private String randomRenter() {
        List<String> renters = Arrays.asList("Allard", "Steven", "Josh", "Jakub", "Ben", "Marc", "Sara", "Jeroen", "Greg", "Marina");
        return renters.get(ThreadLocalRandom.current().nextInt(renters.size()));
    }

    private String randomLocation() {
        List<String> locations = Arrays.asList("Amsterdam", "Paris", "Vilnius", "Barcelona", "London", "New York", "Berlin", "Milan", "Rome", "Belgrade");
        return locations.get(ThreadLocalRandom.current().nextInt(locations.size()));
    }

}
