package org.indra.medicalreportingservice.adapter.in.rest.dto;

import java.time.LocalDateTime;
import java.util.List;

public record ConsultationViewResponseDTO(
        String id,
        String appointmentId,
        String patientId,
        String patientName,
        String doctorId,
        LocalDateTime consultationDate,
        String reason,
        String status,
        List<DiagnosisItemDTO> diagnoses,
        List<PrescriptionItemDTO> prescriptions,
        List<AttachmentItemDTO> attachments) {

    public record DiagnosisItemDTO(String diagnosisId, String icd10Code,
                                   String type) {
    }

    public record PrescriptionItemDTO(String prescriptionId,
                                      String medication) {
    }

    public record AttachmentItemDTO(String attachmentId, String fileName) {
    }
}
