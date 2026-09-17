package org.indra.medicalreportingservice.adapter.in.messaging.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.time.Instant;
import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
public record PrescriptionIssuedEvent(UUID consultationId, UUID prescriptionId,
                                      String medication, Instant occurredAt) {
}
