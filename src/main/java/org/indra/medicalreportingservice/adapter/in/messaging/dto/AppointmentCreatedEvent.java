package org.indra.medicalreportingservice.adapter.in.messaging.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
public record AppointmentCreatedEvent(
        UUID appointmentId,
        UUID patientId,
        UUID doctorId,
        String specialty,
        LocalDateTime appointmentDateTime,
        String reason,
        String status,
        Instant occurredAt) {
}
