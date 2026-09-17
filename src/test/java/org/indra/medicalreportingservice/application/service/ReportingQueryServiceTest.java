package org.indra.medicalreportingservice.application.service;

import org.indra.medicalreportingservice.application.model.PageResult;
import org.indra.medicalreportingservice.application.model.ReportSearchCriteria;
import org.indra.medicalreportingservice.application.port.out.ConsultationProjectionQueryPort;
import org.indra.medicalreportingservice.application.port.out.DashboardQueryPort;
import org.indra.medicalreportingservice.domain.exception.InvalidRequestException;
import org.indra.medicalreportingservice.domain.model.ConsultationProjection;
import org.indra.medicalreportingservice.domain.model.DashboardMetrics;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ReportingQueryServiceTest {

    @Test
    void shouldDelegateSearchToOutputPort() {
        CapturingQuery query = new CapturingQuery();
        ReportingQueryService service = new ReportingQueryService(query,
                (since, limit) -> new DashboardMetrics(0, List.of(),
                        List.of(), List.of()));
        ReportSearchCriteria criteria = new ReportSearchCriteria(
                "patient-1", null, null, null, null);

        PageResult<ConsultationProjection> result =
                service.search(criteria, 2, 25);

        assertSame(criteria, query.criteria);
        assertEquals(2, query.page);
        assertEquals(25, query.size);
        assertEquals(0, result.totalElements());
    }

    @Test
    void shouldRejectInvalidDateRange() {
        ReportingQueryService service = new ReportingQueryService(
                new CapturingQuery(), emptyDashboard());
        ReportSearchCriteria criteria = new ReportSearchCriteria(null, null,
                null, LocalDateTime.of(2026, 9, 30, 0, 0),
                LocalDateTime.of(2026, 9, 1, 0, 0));

        assertThrows(InvalidRequestException.class,
                () -> service.search(criteria, 0, 20));
    }

    @Test
    void shouldRejectOversizedPage() {
        ReportingQueryService service = new ReportingQueryService(
                new CapturingQuery(), emptyDashboard());

        assertThrows(InvalidRequestException.class,
                () -> service.patientHistory("patient-1", 0, 101));
    }

    private DashboardQueryPort emptyDashboard() {
        return (since, limit) -> new DashboardMetrics(0, List.of(),
                List.of(), List.of());
    }

    private static final class CapturingQuery
            implements ConsultationProjectionQueryPort {
        private ReportSearchCriteria criteria;
        private int page;
        private int size;

        @Override
        public PageResult<ConsultationProjection> search(
                ReportSearchCriteria criteria, int page, int size) {
            this.criteria = criteria;
            this.page = page;
            this.size = size;
            return PageResult.of(List.of(), page, size, 0);
        }
    }
}
