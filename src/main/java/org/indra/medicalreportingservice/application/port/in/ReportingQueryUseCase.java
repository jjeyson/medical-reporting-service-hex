package org.indra.medicalreportingservice.application.port.in;

import org.indra.medicalreportingservice.application.model.PageResult;
import org.indra.medicalreportingservice.application.model.ReportSearchCriteria;
import org.indra.medicalreportingservice.domain.model.ConsultationProjection;
import org.indra.medicalreportingservice.domain.model.DashboardMetrics;

public interface ReportingQueryUseCase {

    PageResult<ConsultationProjection> search(ReportSearchCriteria criteria,
                                              int page, int size);

    PageResult<ConsultationProjection> patientHistory(String patientId,
                                                      int page, int size);

    DashboardMetrics dashboard();
}
