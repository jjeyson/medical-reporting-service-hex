package org.indra.medicalreportingservice.application.command;

import java.time.Instant;
import java.util.UUID;

public record CompleteConsultationProjectionCommand(
        UUID consultationId,
        Instant occurredAt) {
}
