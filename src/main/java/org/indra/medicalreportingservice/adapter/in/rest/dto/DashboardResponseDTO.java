package org.indra.medicalreportingservice.adapter.in.rest.dto;

import java.util.List;

public record DashboardResponseDTO(
        long totalConsultations,
        List<StatusCountDTO> consultationsByStatus,
        List<DailyCountDTO> consultationsLast30Days,
        List<TopDiagnosisDTO> topDiagnoses) {

    public record StatusCountDTO(String label, long value) {
    }

    public record DailyCountDTO(String date, long value) {
    }

    public record TopDiagnosisDTO(String icd10Code, long value) {
    }
}
