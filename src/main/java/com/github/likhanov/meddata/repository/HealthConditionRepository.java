package com.github.likhanov.meddata.repository;

import com.github.likhanov.meddata.domain.HealthCondition;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HealthConditionRepository extends JpaRepository<HealthCondition, Long> {

    Optional<HealthCondition> findByCode(String code);
}
