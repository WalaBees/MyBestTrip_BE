package com.example.mybesttrip_be.domain.destination.repository;

import com.example.mybesttrip_be.domain.destination.entity.Destination;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DestinationRepository extends JpaRepository<Destination, Long> {}
