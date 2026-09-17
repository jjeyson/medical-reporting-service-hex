package org.indra.medicalreportingservice.application.service;

import org.indra.medicalreportingservice.application.model.PageResult;
import org.indra.medicalreportingservice.application.model.ReportSearchCriteria;
import org.indra.medicalreportingservice.application.port.in.ReportingQueryUseCase;
import org.indra.medicalreportingservice.application.port.out.ConsultationProjectionQueryPort;
import org.indra.medicalreportingservice.application.port.out.DashboardQueryPort;
import org.indra.medicalreportingservice.domain.exception.InvalidRequestException;
import org.indra.medicalreportingservice.domain.model.ConsultationProjection;
import org.indra.medicalreportingservice.domain.model.DashboardMetrics;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

public final class ReportingQueryService implements ReportingQueryUseCase {

    private static final int MAX_PAGE_SIZE = 100;
    private static final int DASHBOARD_DAYS = 30;
    private static final int TOP_DIAGNOSES_LIMIT = 5;

    private final ConsultationProjectionQueryPort consultationQuery;
    private final DashboardQueryPort dashboardQuery;

    public ReportingQueryService(ConsultationProjectionQueryPort consultationQuery,
                                 DashboardQueryPort dashboardQuery) {
        this.consultationQuery = consultationQuery;
        this.dashboardQuery = dashboardQuery;
    }

    @Override
    public PageResult<ConsultationProjection> search(
            ReportSearchCriteria criteria, int page, int size) {
        validatePagination(page, size);
        if (criteria.dateFrom() != null && criteria.dateTo() != null
                && criteria.dateFrom().isAfter(criteria.dateTo())) {
            throw new InvalidRequestException(
                    "dateFrom no puede ser posterior a dateTo");
        }
        return consultationQuery.search(criteria, page, size);
    }

    @Override
    public PageResult<ConsultationProjection> patientHistory(
            String patientId, int page, int size) {
        if (patientId == null || patientId.isBlank()) {
            throw new InvalidRequestException("patientId es obligatorio");
        }
        return search(new ReportSearchCriteria(patientId, null, null,
                null, null), page, size);
    }

    @Override
    public DashboardMetrics dashboard() {
        return dashboardQuery.calculate(
                LocalDateTime.now(ZoneOffset.UTC).minusDays(DASHBOARD_DAYS),
                TOP_DIAGNOSES_LIMIT);
    }

    private void validatePagination(int page, int size) {
        if (page < 0) {
            throw new InvalidRequestException("La página no puede ser negativa");
        }
        if (size < 1 || size > MAX_PAGE_SIZE) {
            throw new InvalidRequestException(
                    "El tamaño debe estar entre 1 y " + MAX_PAGE_SIZE);
        }
    }
}
