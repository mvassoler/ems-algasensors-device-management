package com.algaworks.algasensors.device.client;


import com.algaworks.algasensors.device.api.model.SensorMonitoringOutput;
import io.hypersistence.tsid.TSID;

public interface SensorMonitoringClient {

    void enableMonotoring(TSID tsid);

    void disableMonotoring(TSID tsid);

    SensorMonitoringOutput getDetail(TSID tsid);

}
