package io.axoniq.demo.bikerental.bikerental;

import io.axoniq.demo.bikerental.bikerental.coreapi.RegisterBikeCommand;
import io.axoniq.demo.bikerental.bikerental.coreapi.RentBikeCommand;
import io.axoniq.demo.bikerental.bikerental.coreapi.ReturnBikeCommand;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.spring.stereotype.Aggregate;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Aggregate
public class Bike {

    @Id
    private String bikeId;

    @Basic
    private String location;

    @Basic
    private String renter;

    public Bike() {
    }

    @CommandHandler
    public Bike(RegisterBikeCommand command) {
        this.bikeId = command.getBikeId();
        this.location = command.getLocation();
    }

    @CommandHandler
    public void handle(RentBikeCommand command) {
        if (this.renter != null) {
            throw new IllegalStateException("Bike is already rented");
        }
        this.renter = command.getRenter();
    }

    @CommandHandler
    public void handle(ReturnBikeCommand command) {
        if (this.renter == null) {
            throw new IllegalStateException("Bike was already returned");
        }
        this.renter = null;
        this.location = command.getLocation();
    }

    public String getBikeId() {
        return bikeId;
    }

    public String getLocation() {
        return location;
    }

    public String getRenter() {
        return renter;
    }
}
