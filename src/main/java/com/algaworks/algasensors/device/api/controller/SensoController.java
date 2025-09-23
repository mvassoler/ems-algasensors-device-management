package com.algaworks.algasensors.device.api.controller;

import com.algaworks.algasensors.device.api.model.SensorInput;
import com.algaworks.algasensors.device.api.model.SensorOutput;
import com.algaworks.algasensors.device.common.IdGenerator;
import com.algaworks.algasensors.device.domain.model.Sensor;
import com.algaworks.algasensors.device.domain.model.SensorId;
import com.algaworks.algasensors.device.domain.repository.SensorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/sensors")
@RequiredArgsConstructor
public class SensoController {

    private final SensorRepository sensorRepository;

    private SensorOutput convertToSensorOutput(Sensor sensorPersistido) {
        return SensorOutput.builder()
                .id(sensorPersistido.getId().getValue())
                .name(sensorPersistido.getName())
                .ip(sensorPersistido.getIp())
                .location(sensorPersistido.getLocation())
                .protocol(sensorPersistido.getProtocol())
                .model(sensorPersistido.getModel())
                .enable(sensorPersistido.getEnable())
                .build();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SensorOutput create(@RequestBody SensorInput sensorInput) {
        var sensor = Sensor.builder()
                .id(new SensorId(IdGenerator.generateTSID()))
                .name(sensorInput.getName())
                .ip(sensorInput.getIp())
                .location(sensorInput.getLocation())
                .protocol(sensorInput.getProtocol())
                .model(sensorInput.getModel())
                .enable(Boolean.FALSE)
                .build();

        var sensorPersistido = sensorRepository.saveAndFlush(sensor);

        return this.convertToSensorOutput(sensorPersistido);
    }

    @GetMapping("/{sensorId}")
    public SensorOutput getOne(@PathVariable("sensorId") SensorId sensorId) {
        var sensor = sensorRepository.findById(new SensorId(sensorId.getValue()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return this.convertToSensorOutput(sensor);


    }

}
