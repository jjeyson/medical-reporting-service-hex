package org.indra.medicalreportingservice.adapter.in.messaging.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.time.Instant;
import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DocumentAttachedEvent(UUID consultationId, UUID attachmentId,
                                    String fileName, Instant occurredAt) {
}
