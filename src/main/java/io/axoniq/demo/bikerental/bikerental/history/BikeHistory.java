package io.axoniq.demo.bikerental.bikerental.history;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.Instant;

@Entity
public class BikeHistory {

    @Id
    @GeneratedValue
    private Long id;

    private String bikeId;
    private String description;
    private String timestamp;

    public BikeHistory() {
    }

    public BikeHistory(String bikeId, Instant timestamp, String description) {
        this.bikeId = bikeId;
        this.timestamp = timestamp.toString();
        this.description = description;
    }

    public String getBikeId() {
        return bikeId;
    }

    public String getDescription() {
        return description;
    }

    public String getTimestamp() {
        return timestamp;
    }
}
