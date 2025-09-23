package com.algaworks.algasensors.device;

import com.algaworks.algasensors.device.common.IdGenerator;
import io.hypersistence.tsid.TSID;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class TsidTest {

    @Test
    void shoudGenerateTSID() {
        TSID tsid = TSID.fast(); //fast somente para teste
        System.out.println(tsid);
        System.out.println(tsid.toLong());
        System.out.println(tsid.getInstant());
    }

    @Test
    void shoudGenerateTSIDbyFactory() {
        TSID tsid = TSID.Factory.getTsid(); //le propriedades do sistema
        System.out.println(tsid);
        System.out.println(tsid.toLong());
        System.out.println(tsid.getInstant());
    }

    @Test
    void shoudGenerateTSIDbyFactory_whenPropertiesAreChanged() {
        System.setProperty("tsid.node", "2");
        System.setProperty("tsid.node.count", "32");

        TSID.Factory factory = TSID.Factory.builder().build();
        TSID tsid = factory.generate();

        System.out.println(tsid);
        System.out.println(tsid.toLong());
        System.out.println(tsid.getInstant());
    }

    @Test
    void shoudGenerateTSIDwithUtilClass() {
        TSID tsid = IdGenerator.generateTSID();
        Assertions.assertThat(tsid.getInstant()).isCloseTo(Instant.now(), Assertions.within(1, ChronoUnit.MINUTES));
    }


}
