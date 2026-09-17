package org.indra.medicalreportingservice.adapter.in.messaging.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
public record AppointmentRescheduledEvent(
        UUID appointmentId,
        LocalDateTime previousDateTime,
        LocalDateTime newDateTime,
        Instant occurredAt) {
}
