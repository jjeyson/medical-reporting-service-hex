package org.indra.medicalreportingservice.application.command;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

public record RegisterAppointmentProjectionCommand(
        UUID appointmentId,
        UUID patientId,
        UUID doctorId,
        String specialty,
        LocalDateTime appointmentDateTime,
        String reason,
        String status,
        Instant occurredAt) {
}
