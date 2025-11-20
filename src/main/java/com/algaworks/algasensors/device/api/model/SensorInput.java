package com.algaworks.algasensors.device.api.model;

import com.algaworks.algasensors.device.domain.model.Sensor;
import lombok.Data;

@Data
public class SensorInput {

    private String name;
    private String ip;
    private String location;
    private String protocol;
    private String model;

    public static Sensor convertToSensorUpdate(Sensor sensor, SensorInput sensorInput) {
        sensor.setName(sensorInput.getName());
        sensor.setIp(sensorInput.getIp());
        sensor.setLocation(sensorInput.getLocation());
        sensor.setProtocol(sensorInput.getProtocol());
        sensor.setModel(sensorInput.getModel());
        return sensor;
    }
}
