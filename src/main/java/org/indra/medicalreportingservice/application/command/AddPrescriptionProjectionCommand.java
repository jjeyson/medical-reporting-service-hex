package org.indra.medicalreportingservice.application.command;

import java.time.Instant;
import java.util.UUID;

public record AddPrescriptionProjectionCommand(
        UUID consultationId,
        UUID prescriptionId,
        String medication,
        Instant occurredAt) {
}
