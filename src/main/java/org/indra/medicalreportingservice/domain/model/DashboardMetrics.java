package org.indra.medicalreportingservice.domain.model;

import java.util.List;

public record DashboardMetrics(
        long totalConsultations,
        List<StatusCount> consultationsByStatus,
        List<DailyCount> consultationsLast30Days,
        List<TopDiagnosis> topDiagnoses) {

    public DashboardMetrics {
        consultationsByStatus = List.copyOf(consultationsByStatus);
        consultationsLast30Days = List.copyOf(consultationsLast30Days);
        topDiagnoses = List.copyOf(topDiagnoses);
    }

    public record StatusCount(String label, long value) {
    }

    public record DailyCount(String date, long value) {
    }

    public record TopDiagnosis(String icd10Code, long value) {
    }
}
