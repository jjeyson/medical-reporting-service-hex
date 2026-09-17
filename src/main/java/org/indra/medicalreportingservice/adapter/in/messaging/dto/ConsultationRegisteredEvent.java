package org.indra.medicalreportingservice.adapter.in.messaging.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ConsultationRegisteredEvent(
        UUID consultationId,
        UUID appointmentId,
        UUID patientId,
        UUID doctorId,
        LocalDateTime consultationDate,
        String reason,
        Instant occurredAt) {
}
