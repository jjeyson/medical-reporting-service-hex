package org.indra.medicalreportingservice.application.port.out;

import org.indra.medicalreportingservice.domain.model.DashboardMetrics;

import java.time.LocalDateTime;

public interface DashboardQueryPort {

    DashboardMetrics calculate(LocalDateTime since, int topDiagnosisLimit);
}
