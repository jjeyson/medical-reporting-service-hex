package org.indra.medicalreportingservice.adapter.in.messaging.deserializer;

import io.quarkus.kafka.client.serialization.ObjectMapperDeserializer;
import org.indra.medicalreportingservice.adapter.in.messaging.dto.PrescriptionIssuedEvent;

public class PrescriptionIssuedEventDeserializer
        extends ObjectMapperDeserializer<PrescriptionIssuedEvent> {

    public PrescriptionIssuedEventDeserializer() {
        super(PrescriptionIssuedEvent.class);
    }
}
