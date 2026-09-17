package org.indra.medicalreportingservice.adapter.in.rest;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.indra.medicalreportingservice.adapter.in.rest.dto.*;
import org.indra.medicalreportingservice.adapter.in.rest.mapper.RestReportingMapper;
import org.indra.medicalreportingservice.adapter.in.rest.query.ReportingSearchParams;
import org.indra.medicalreportingservice.application.port.in.ReportingQueryUseCase;

import static org.indra.medicalreportingservice.adapter.in.rest.security.SecurityRoles.*;

@Path("/api/reports")
@Produces(MediaType.APPLICATION_JSON)
public class ReportingResource {

    @Inject
    ReportingQueryUseCase useCase;

    @GET
    @Path("/consultations")
    @RolesAllowed({MEDICO, RECEPCIONISTA, ADMINISTRADOR,
            DOCTOR, RECEPTIONIST, ADMINISTRATOR})
    public PageResponse<ConsultationViewResponseDTO> searchConsultations(
            @BeanParam @Valid ReportingSearchParams params) {
        return RestReportingMapper.toResponse(useCase.search(
                RestReportingMapper.toCriteria(params), params.page, params.size));
    }

    @GET
    @Path("/patients/{patientId}/history")
    @RolesAllowed({MEDICO, PACIENTE, ADMINISTRADOR,
            DOCTOR, PATIENT, ADMINISTRATOR})
    public PageResponse<ConsultationViewResponseDTO> patientHistory(
            @PathParam("patientId") String patientId,
            @QueryParam("page") @DefaultValue("0") @Min(0) int page,
            @QueryParam("size") @DefaultValue("20") @Min(1) @Max(100) int size) {
        return RestReportingMapper.toResponse(
                useCase.patientHistory(patientId, page, size));
    }

    @GET
    @Path("/dashboard")
    @RolesAllowed({MEDICO, ADMINISTRADOR, DOCTOR, ADMINISTRATOR})
    public DashboardResponseDTO dashboard() {
        return RestReportingMapper.toResponse(useCase.dashboard());
    }
}
