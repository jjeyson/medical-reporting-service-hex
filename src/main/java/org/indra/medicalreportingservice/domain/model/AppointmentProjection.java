package org.indra.medicalreportingservice.domain.model;

import java.time.LocalDateTime;

public record AppointmentProjection(
        String id,
        String patientId,
        String doctorId,
        String specialty,
        LocalDateTime appointmentDateTime,
        String reason,
        AppointmentStatus status,
        String cancellationReason,
        LocalDateTime createdAt,
        LocalDateTime lastUpdatedAt) {
}
