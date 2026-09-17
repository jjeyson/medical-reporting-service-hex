package org.indra.medicalreportingservice.application.command;

import java.time.Instant;
import java.util.UUID;

public record SyncPatientProjectionCommand(
        UUID patientId,
        String eventType,
        String dni,
        String firstName,
        String lastName,
        Instant occurredAt) {
}
