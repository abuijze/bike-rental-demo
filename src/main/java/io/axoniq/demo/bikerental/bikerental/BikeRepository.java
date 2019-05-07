package io.axoniq.demo.bikerental.bikerental;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("bikeQueryRepository")
public interface BikeRepository extends JpaRepository<Bike, String> {
}
