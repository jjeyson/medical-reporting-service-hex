package org.indra.medicalreportingservice.adapter.in.messaging.deserializer;

import io.quarkus.kafka.client.serialization.ObjectMapperDeserializer;
import org.indra.medicalreportingservice.adapter.in.messaging.dto.AppointmentCancelledEvent;

public class AppointmentCancelledEventDeserializer
        extends ObjectMapperDeserializer<AppointmentCancelledEvent> {

    public AppointmentCancelledEventDeserializer() {
        super(AppointmentCancelledEvent.class);
    }
}
