package org.indra.medicalreportingservice.adapter.in.messaging.deserializer;

import io.quarkus.kafka.client.serialization.ObjectMapperDeserializer;
import org.indra.medicalreportingservice.adapter.in.messaging.dto.ConsultationCompletedEvent;

public class ConsultationCompletedEventDeserializer
        extends ObjectMapperDeserializer<ConsultationCompletedEvent> {

    public ConsultationCompletedEventDeserializer() {
        super(ConsultationCompletedEvent.class);
    }
}
