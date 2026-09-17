package org.indra.medicalreportingservice.application.command;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

public record RegisterConsultationProjectionCommand(
        UUID consultationId,
        UUID appointmentId,
        UUID patientId,
        UUID doctorId,
        LocalDateTime consultationDate,
        String reason,
        Instant occurredAt) {
}
