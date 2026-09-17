package org.indra.medicalreportingservice.adapter.in.rest.dto;

import java.util.List;

public record PageResponse<T>(List<T> content, int pageNumber, int pageSize,
                              long totalElements, int totalPages) {
}
