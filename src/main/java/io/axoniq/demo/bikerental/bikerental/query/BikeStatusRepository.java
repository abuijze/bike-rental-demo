package io.axoniq.demo.bikerental.bikerental.query;

import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Profile("query")
@Repository()
public interface BikeStatusRepository extends JpaRepository<BikeStatus, String> {
}
