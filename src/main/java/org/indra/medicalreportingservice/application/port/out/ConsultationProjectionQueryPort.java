package org.indra.medicalreportingservice.application.port.out;

import org.indra.medicalreportingservice.application.model.PageResult;
import org.indra.medicalreportingservice.application.model.ReportSearchCriteria;
import org.indra.medicalreportingservice.domain.model.ConsultationProjection;

public interface ConsultationProjectionQueryPort {

    PageResult<ConsultationProjection> search(ReportSearchCriteria criteria,
                                              int page, int size);
}
