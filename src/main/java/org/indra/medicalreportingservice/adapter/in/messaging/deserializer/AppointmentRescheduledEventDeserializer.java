package org.indra.medicalreportingservice.adapter.in.messaging.deserializer;

import io.quarkus.kafka.client.serialization.ObjectMapperDeserializer;
import org.indra.medicalreportingservice.adapter.in.messaging.dto.AppointmentRescheduledEvent;

public class AppointmentRescheduledEventDeserializer
        extends ObjectMapperDeserializer<AppointmentRescheduledEvent> {

    public AppointmentRescheduledEventDeserializer() {
        super(AppointmentRescheduledEvent.class);
    }
}
