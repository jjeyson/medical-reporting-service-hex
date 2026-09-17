package org.indra.medicalreportingservice.adapter.in.messaging.deserializer;

import io.quarkus.kafka.client.serialization.ObjectMapperDeserializer;
import org.indra.medicalreportingservice.adapter.in.messaging.dto.PatientChangedEvent;

public class PatientChangedEventDeserializer
        extends ObjectMapperDeserializer<PatientChangedEvent> {

    public PatientChangedEventDeserializer() {
        super(PatientChangedEvent.class);
    }
}
