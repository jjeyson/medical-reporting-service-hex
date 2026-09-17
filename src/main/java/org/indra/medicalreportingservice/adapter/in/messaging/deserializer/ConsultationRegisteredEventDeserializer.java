package org.indra.medicalreportingservice.adapter.in.messaging.deserializer;

import io.quarkus.kafka.client.serialization.ObjectMapperDeserializer;
import org.indra.medicalreportingservice.adapter.in.messaging.dto.ConsultationRegisteredEvent;

public class ConsultationRegisteredEventDeserializer
        extends ObjectMapperDeserializer<ConsultationRegisteredEvent> {

    public ConsultationRegisteredEventDeserializer() {
        super(ConsultationRegisteredEvent.class);
    }
}
