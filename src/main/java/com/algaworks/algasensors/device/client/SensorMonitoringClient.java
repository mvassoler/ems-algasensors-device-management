package com.algaworks.algasensors.device.client;


import com.algaworks.algasensors.device.api.model.SensorMonitoringOutput;
import io.hypersistence.tsid.TSID;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.DeleteExchange;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PutExchange;

@HttpExchange("/api/sensors/{sensorId}/monitoring")
public interface SensorMonitoringClient {

    @PutExchange("/enable")
    void enableMonotoring(@PathVariable TSID tsid);

    @DeleteExchange("/disable")
    void disableMonotoring(@PathVariable TSID tsid);

    @GetExchange
    SensorMonitoringOutput getDetail(@PathVariable TSID tsid);

}
