package org.indra.medicalreportingservice.application.model;

import org.indra.medicalreportingservice.domain.model.ConsultationStatus;

import java.time.LocalDateTime;

public record ReportSearchCriteria(
        String patientId,
        String doctorId,
        ConsultationStatus status,
        LocalDateTime dateFrom,
        LocalDateTime dateTo) {
}
