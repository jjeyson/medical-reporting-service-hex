package org.indra.medicalreportingservice.adapter.in.rest.mapper;

import org.indra.medicalreportingservice.adapter.in.rest.dto.*;
import org.indra.medicalreportingservice.adapter.in.rest.query.ReportingSearchParams;
import org.indra.medicalreportingservice.application.model.PageResult;
import org.indra.medicalreportingservice.application.model.ReportSearchCriteria;
import org.indra.medicalreportingservice.domain.model.*;

public final class RestReportingMapper {

    private RestReportingMapper() {
    }

    public static ReportSearchCriteria toCriteria(ReportingSearchParams params) {
        return new ReportSearchCriteria(params.patientId, params.doctorId,
                params.status, params.dateFrom, params.dateTo);
    }

    public static ConsultationViewResponseDTO toResponse(
            ConsultationProjection projection) {
        return new ConsultationViewResponseDTO(projection.id(),
                projection.appointmentId(), projection.patientId(),
                projection.patientName(), projection.doctorId(),
                projection.consultationDate(), projection.reason(),
                projection.status() == null ? null : projection.status().name(),
                projection.diagnoses().stream()
                        .map(RestReportingMapper::toResponse).toList(),
                projection.prescriptions().stream()
                        .map(RestReportingMapper::toResponse).toList(),
                projection.attachments().stream()
                        .map(RestReportingMapper::toResponse).toList());
    }

    public static PageResponse<ConsultationViewResponseDTO> toResponse(
            PageResult<ConsultationProjection> page) {
        return new PageResponse<>(page.content().stream()
                .map(RestReportingMapper::toResponse).toList(),
                page.pageNumber(), page.pageSize(), page.totalElements(),
                page.totalPages());
    }

    public static DashboardResponseDTO toResponse(DashboardMetrics metrics) {
        return new DashboardResponseDTO(metrics.totalConsultations(),
                metrics.consultationsByStatus().stream()
                        .map(item -> new DashboardResponseDTO.StatusCountDTO(
                                item.label(), item.value())).toList(),
                metrics.consultationsLast30Days().stream()
                        .map(item -> new DashboardResponseDTO.DailyCountDTO(
                                item.date(), item.value())).toList(),
                metrics.topDiagnoses().stream()
                        .map(item -> new DashboardResponseDTO.TopDiagnosisDTO(
                                item.icd10Code(), item.value())).toList());
    }

    private static ConsultationViewResponseDTO.DiagnosisItemDTO toResponse(
            DiagnosisProjection diagnosis) {
        return new ConsultationViewResponseDTO.DiagnosisItemDTO(
                diagnosis.diagnosisId(), diagnosis.icd10Code(), diagnosis.type());
    }

    private static ConsultationViewResponseDTO.PrescriptionItemDTO toResponse(
            PrescriptionProjection prescription) {
        return new ConsultationViewResponseDTO.PrescriptionItemDTO(
                prescription.prescriptionId(), prescription.medication());
    }

    private static ConsultationViewResponseDTO.AttachmentItemDTO toResponse(
            AttachmentProjection attachment) {
        return new ConsultationViewResponseDTO.AttachmentItemDTO(
                attachment.attachmentId(), attachment.fileName());
    }
}
