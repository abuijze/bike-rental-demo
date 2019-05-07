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

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

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

}
