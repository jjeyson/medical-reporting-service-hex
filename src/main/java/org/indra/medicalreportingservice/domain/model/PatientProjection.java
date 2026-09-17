package org.indra.medicalreportingservice.domain.model;

import java.time.LocalDateTime;

public record PatientProjection(String id, String dni, String fullName,
                                LocalDateTime lastUpdatedAt) {
}
