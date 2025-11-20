package com.algaworks.algasensors.device.api.controller;

import com.algaworks.algasensors.device.api.model.SensorDetailOutput;
import com.algaworks.algasensors.device.api.model.SensorInput;
import com.algaworks.algasensors.device.api.model.SensorMonitoringOutput;
import com.algaworks.algasensors.device.api.model.SensorOutput;
import com.algaworks.algasensors.device.client.SensorMonitoringClient;
import com.algaworks.algasensors.device.common.IdGenerator;
import com.algaworks.algasensors.device.domain.model.Sensor;
import com.algaworks.algasensors.device.domain.model.SensorId;
import com.algaworks.algasensors.device.domain.repository.SensorRepository;
import io.hypersistence.tsid.TSID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
    private final SensorMonitoringClient sensorMonitoringClient;

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

    public Page<SensorOutput> search(@PageableDefault Pageable pageable) {
        Page<Sensor> sensors = sensorRepository.findAll(pageable);
        return sensors.map(this::convertToSensorOutput);
    }

    @PutMapping("/{sensorId}")
    @ResponseStatus(HttpStatus.OK)
    public SensorOutput update(@PathVariable TSID sensorId, @RequestBody SensorInput sensorInput) {
        Sensor sensor = sensorRepository.findById(new SensorId(sensorId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        var sensorUpdate = SensorInput.convertToSensorUpdate(sensor, sensorInput);
        var sensorPersistido = sensorRepository.save(sensorUpdate);
        return this.convertToSensorOutput(sensorPersistido);
    }

    @DeleteMapping("/{sensorId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable TSID sensorId) {
        Sensor sensor = sensorRepository.findById(new SensorId(sensorId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        sensorRepository.delete(sensor);
        sensorMonitoringClient.disableMonotoring(sensorId);
    }

    @PutMapping("/{sensorId}/enable")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void ativarSensor(@PathVariable TSID sensorId) {
        Sensor sensor = sensorRepository.findById(new SensorId(sensorId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        sensor.setEnable(Boolean.TRUE);
        sensorRepository.save(sensor);
        sensorMonitoringClient.enableMonotoring(sensorId);
    }

    @DeleteMapping("/{sensorId}/enable")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void desativarSensor(@PathVariable TSID sensorId) {
        Sensor sensor = sensorRepository.findById(new SensorId(sensorId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        sensor.setEnable(Boolean.FALSE);
        sensorRepository.save(sensor);
    }

    @GetMapping("/{sensorId}/detail")
    public SensorDetailOutput getOneWithDetail(@PathVariable("sensorId") SensorId sensorId) {
        var sensor = sensorRepository.findById(new SensorId(sensorId.getValue()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        SensorMonitoringOutput sensorMonitoringOutput = sensorMonitoringClient.getDetail(sensorId.getValue());
        SensorOutput sensorOutput = this.convertToSensorOutput(sensor);
        return SensorDetailOutput.builder()
                .sensor(sensorOutput)
                .monitoring(sensorMonitoringOutput)
                .build();
    }

}
