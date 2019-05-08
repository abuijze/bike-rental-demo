package io.axoniq.demo.bikerental.bikerental.query;

import org.springframework.context.annotation.Profile;

import javax.persistence.Entity;
import javax.persistence.Id;

@Profile("query")
@Entity
public class BikeStatus {

    @Id
    private String bikeId;
    private String location;
    private String renter;

    public BikeStatus() {
    }

    public BikeStatus(String bikeId, String location) {
        this.bikeId = bikeId;
        this.location = location;
    }

    public String getBikeId() {
        return bikeId;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getRenter() {
        return renter;
    }

    public void setRenter(String renter) {
        this.renter = renter;
    }

}
