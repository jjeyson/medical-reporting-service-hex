package org.indra.medicalreportingservice.adapter.in.messaging.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.time.Instant;
import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DiagnosisAddedEvent(UUID consultationId, UUID diagnosisId,
                                  String icd10Code, String type,
                                  Instant occurredAt) {
}
