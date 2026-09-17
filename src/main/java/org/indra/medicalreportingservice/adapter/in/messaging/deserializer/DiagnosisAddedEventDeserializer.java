package org.indra.medicalreportingservice.adapter.in.messaging.deserializer;

import io.quarkus.kafka.client.serialization.ObjectMapperDeserializer;
import org.indra.medicalreportingservice.adapter.in.messaging.dto.DiagnosisAddedEvent;

public class DiagnosisAddedEventDeserializer
        extends ObjectMapperDeserializer<DiagnosisAddedEvent> {

    public DiagnosisAddedEventDeserializer() {
        super(DiagnosisAddedEvent.class);
    }
}
