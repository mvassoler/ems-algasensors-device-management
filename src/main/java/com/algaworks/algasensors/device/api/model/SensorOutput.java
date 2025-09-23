package com.algaworks.algasensors.device.api.model;

import io.hypersistence.tsid.TSID;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class SensorOutput implements Serializable {

    private TSID id;
    private String name;
    private String ip;
    private String location;
    private String protocol;
    private String model;
    private Boolean enable;

}
