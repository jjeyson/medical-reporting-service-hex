package org.indra.medicalreportingservice.application.command;

import java.time.Instant;
import java.util.UUID;

public record CancelAppointmentProjectionCommand(
        UUID appointmentId,
        String reason,
        Instant occurredAt) {
}
