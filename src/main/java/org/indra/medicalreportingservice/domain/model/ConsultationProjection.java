package org.indra.medicalreportingservice.domain.model;

import java.time.LocalDateTime;
import java.util.List;

public record ConsultationProjection(
        String id,
        String appointmentId,
        String patientId,
        String patientName,
        String doctorId,
        LocalDateTime consultationDate,
        String reason,
        ConsultationStatus status,
        List<DiagnosisProjection> diagnoses,
        List<PrescriptionProjection> prescriptions,
        List<AttachmentProjection> attachments,
        LocalDateTime createdAt,
        LocalDateTime lastUpdatedAt) {

    public ConsultationProjection {
        diagnoses = diagnoses == null ? List.of() : List.copyOf(diagnoses);
        prescriptions = prescriptions == null ? List.of()
                : List.copyOf(prescriptions);
        attachments = attachments == null ? List.of()
                : List.copyOf(attachments);
    }
}
