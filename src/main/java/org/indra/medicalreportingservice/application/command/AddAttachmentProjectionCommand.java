package org.indra.medicalreportingservice.application.command;

import java.time.Instant;
import java.util.UUID;

public record AddAttachmentProjectionCommand(
        UUID consultationId,
        UUID attachmentId,
        String fileName,
        Instant occurredAt) {
}
