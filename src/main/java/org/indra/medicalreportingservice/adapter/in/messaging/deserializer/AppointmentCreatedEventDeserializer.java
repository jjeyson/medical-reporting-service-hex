package org.indra.medicalreportingservice.adapter.in.messaging.deserializer;

import io.quarkus.kafka.client.serialization.ObjectMapperDeserializer;
import org.indra.medicalreportingservice.adapter.in.messaging.dto.AppointmentCreatedEvent;

public class AppointmentCreatedEventDeserializer
        extends ObjectMapperDeserializer<AppointmentCreatedEvent> {

    public AppointmentCreatedEventDeserializer() {
        super(AppointmentCreatedEvent.class);
    }
}
