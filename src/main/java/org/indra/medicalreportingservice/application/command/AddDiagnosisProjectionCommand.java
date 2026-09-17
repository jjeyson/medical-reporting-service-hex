package org.indra.medicalreportingservice.application.command;

import java.time.Instant;
import java.util.UUID;

public record AddDiagnosisProjectionCommand(
        UUID consultationId,
        UUID diagnosisId,
        String icd10Code,
        String type,
        Instant occurredAt) {
}
