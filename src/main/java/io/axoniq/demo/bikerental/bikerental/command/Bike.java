package io.axoniq.demo.bikerental.bikerental.command;

import io.axoniq.demo.bikerental.bikerental.coreapi.*;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.spring.stereotype.Aggregate;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Id;

import static org.axonframework.modelling.command.AggregateLifecycle.apply;

@Entity
@Aggregate
public class Bike {

    @Id
    private String bikeId;

    @Basic
    private boolean isAvailable;

    public Bike() {
    }

    @CommandHandler
    public Bike(RegisterBikeCommand command) {
        this.bikeId = command.getBikeId();
        this.isAvailable = true;
        apply(new BikeRegisteredEvent(command.getBikeId(), command.getLocation()));
    }

    @CommandHandler
    public void handle(RentBikeCommand command) {
        if (!this.isAvailable) {
            throw new IllegalStateException("Bike is already rented");
        }
        this.isAvailable = false;
        apply(new BikeRentedEvent(command.getBikeId(), command.getRenter()));
    }

    @CommandHandler
    public void handle(ReturnBikeCommand command) {
        if (this.isAvailable) {
            throw new IllegalStateException("Bike was already returned");
        }
        this.isAvailable = true;
        apply(new BikeReturnedEvent(command.getBikeId(), command.getLocation()));
    }
}
