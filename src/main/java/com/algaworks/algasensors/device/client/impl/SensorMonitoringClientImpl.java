package com.algaworks.algasensors.device.client.impl;

import com.algaworks.algasensors.device.api.model.SensorMonitoringOutput;
import com.algaworks.algasensors.device.client.RestClientFactory;
import com.algaworks.algasensors.device.client.SensorMonitoringClient;
import io.hypersistence.tsid.TSID;
import org.springframework.web.client.RestClient;

//@Component
public class SensorMonitoringClientImpl implements SensorMonitoringClient {

    private final RestClient restClient;

    public SensorMonitoringClientImpl(RestClientFactory factory) {
        this.restClient = factory.temperatureMonitoringClient();
    }

    @Override
    public void enableMonotoring(TSID tsid) {
        restClient.put()
                .uri("/api/sensors/{sensorId}/monitoring/enable", tsid)
                .retrieve()
                .toBodilessEntity();
    }

    @Override
    public void disableMonotoring(TSID tsid) {
        restClient.delete()
                .uri("/api/sensors/{sensorId}/monitoring/disable", tsid)
                .retrieve()
                .toBodilessEntity();
    }

    @Override
    public SensorMonitoringOutput getDetail(TSID tsid) {
        return restClient.get()
                .uri("/api/sensors/{sensorId}/monitoring", tsid)
                .retrieve()
                .body(SensorMonitoringOutput.class);
    }

}
