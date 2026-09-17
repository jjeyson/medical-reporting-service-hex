package org.indra.medicalreportingservice.application.command;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

public record RescheduleAppointmentProjectionCommand(
        UUID appointmentId,
        LocalDateTime previousDateTime,
        LocalDateTime newDateTime,
        Instant occurredAt) {
}
