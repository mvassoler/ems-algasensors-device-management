package com.algaworks.algasensors.device.domain.repository;

import com.algaworks.algasensors.device.domain.model.Sensor;
import com.algaworks.algasensors.device.domain.model.SensorId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SensorRepository extends JpaRepository<Sensor, SensorId> {

}
