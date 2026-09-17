package org.indra.medicalreportingservice.adapter.in.rest.exception;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.indra.medicalreportingservice.domain.exception.InvalidRequestException;

import java.util.Map;

@Provider
public class InvalidRequestExceptionMapper
        implements ExceptionMapper<InvalidRequestException> {

    @Override
    public Response toResponse(InvalidRequestException exception) {
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(Map.of("message", exception.getMessage())).build();
    }
}
