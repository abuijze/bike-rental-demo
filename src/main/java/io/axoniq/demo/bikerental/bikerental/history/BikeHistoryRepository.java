package io.axoniq.demo.bikerental.bikerental.history;

import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

@Profile("history")
public interface BikeHistoryRepository extends JpaRepository<BikeHistory, Long> {

    List<BikeHistory> findByBikeIdOrderById(String bikeId);

}
