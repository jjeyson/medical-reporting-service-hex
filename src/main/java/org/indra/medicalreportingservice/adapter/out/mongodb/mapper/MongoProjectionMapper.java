package org.indra.medicalreportingservice.adapter.out.mongodb.mapper;

import org.indra.medicalreportingservice.adapter.out.mongodb.document.*;
import org.indra.medicalreportingservice.domain.model.*;

import java.util.List;

public final class MongoProjectionMapper {

    private MongoProjectionMapper() {
    }

    public static ConsultationProjection toDomain(ConsultationViewDocument source) {
        return new ConsultationProjection(source.id, source.appointmentId,
                source.patientId, source.patientName, source.doctorId,
                source.consultationDate, source.reason, toStatus(source.status),
                mapDiagnoses(source.diagnoses),
                mapPrescriptions(source.prescriptions),
                mapAttachments(source.attachments), source.createdAt,
                source.lastUpdatedAt);
    }

    private static List<DiagnosisProjection> mapDiagnoses(
            List<DiagnosisViewDocument> source) {
        if (source == null) {
            return List.of();
        }
        return source.stream().map(item -> new DiagnosisProjection(
                item.diagnosisId, item.icd10Code, item.type)).toList();
    }

    private static List<PrescriptionProjection> mapPrescriptions(
            List<PrescriptionViewDocument> source) {
        if (source == null) {
            return List.of();
        }
        return source.stream().map(item -> new PrescriptionProjection(
                item.prescriptionId, item.medication)).toList();
    }

    private static List<AttachmentProjection> mapAttachments(
            List<AttachmentViewDocument> source) {
        if (source == null) {
            return List.of();
        }
        return source.stream().map(item -> new AttachmentProjection(
                item.attachmentId, item.fileName)).toList();
    }

    private static ConsultationStatus toStatus(String status) {
        return status == null ? null : ConsultationStatus.valueOf(status);
    }
}
