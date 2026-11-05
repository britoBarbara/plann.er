package br.com.barbara.planner.repository;

import br.com.barbara.planner.domain.entity.Trip;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TripRepository extends JpaRepository<Trip, UUID> {
}
