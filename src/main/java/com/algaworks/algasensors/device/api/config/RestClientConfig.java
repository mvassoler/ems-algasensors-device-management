package com.algaworks.algasensors.device.api.config;

import com.algaworks.algasensors.device.client.RestClientFactory;
import com.algaworks.algasensors.device.client.SensorMonitoringClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class RestClientConfig {

    @Bean
    public SensorMonitoringClient getSensorMonitoringClient(RestClientFactory factory) {
        RestClient restClient = factory.temperatureMonitoringClient();
        RestClientAdapter restClientAdapter = RestClientAdapter.create(restClient);
        HttpServiceProxyFactory proxyFactory = HttpServiceProxyFactory.builderFor(restClientAdapter).build();
        return proxyFactory.createClient(SensorMonitoringClient.class);

    }
}
