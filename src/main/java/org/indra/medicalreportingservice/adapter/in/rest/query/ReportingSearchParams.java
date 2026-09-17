package org.indra.medicalreportingservice.adapter.in.rest.query;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.QueryParam;
import org.indra.medicalreportingservice.domain.model.ConsultationStatus;

import java.time.LocalDateTime;

public class ReportingSearchParams {

    @QueryParam("patientId")
    public String patientId;

    @QueryParam("doctorId")
    public String doctorId;

    @QueryParam("status")
    public ConsultationStatus status;

    @QueryParam("dateFrom")
    public LocalDateTime dateFrom;

    @QueryParam("dateTo")
    public LocalDateTime dateTo;

    @QueryParam("page")
    @DefaultValue("0")
    @Min(0)
    public int page;

    @QueryParam("size")
    @DefaultValue("20")
    @Min(1)
    @Max(100)
    public int size;
}
