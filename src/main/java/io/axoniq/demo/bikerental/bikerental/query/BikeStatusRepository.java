package io.axoniq.demo.bikerental.bikerental.query;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository()
public interface BikeStatusRepository extends JpaRepository<BikeStatus, String> {
}
