package org.indra.medicalreportingservice.infrastructure;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import org.indra.medicalreportingservice.application.port.in.ProjectionEventUseCase;
import org.indra.medicalreportingservice.application.port.in.ReportingQueryUseCase;
import org.indra.medicalreportingservice.application.port.out.ConsultationProjectionQueryPort;
import org.indra.medicalreportingservice.application.port.out.DashboardQueryPort;
import org.indra.medicalreportingservice.application.port.out.ProjectionWritePort;
import org.indra.medicalreportingservice.application.service.ProjectionEventService;
import org.indra.medicalreportingservice.application.service.ReportingQueryService;

@ApplicationScoped
public class ApplicationBeanConfiguration {

    @Produces
    @ApplicationScoped
    ReportingQueryUseCase reportingQueryUseCase(
            ConsultationProjectionQueryPort consultationQuery,
            DashboardQueryPort dashboardQuery) {
        return new ReportingQueryService(consultationQuery, dashboardQuery);
    }

    @Produces
    @ApplicationScoped
    ProjectionEventUseCase projectionEventUseCase(
            ProjectionWritePort projectionWriter) {
        return new ProjectionEventService(projectionWriter);
    }
}
