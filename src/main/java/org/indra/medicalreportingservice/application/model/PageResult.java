package org.indra.medicalreportingservice.application.model;

import java.util.List;

public record PageResult<T>(List<T> content, int pageNumber, int pageSize,
                            long totalElements, int totalPages) {

    public static <T> PageResult<T> of(List<T> content, int pageNumber,
                                      int pageSize, long totalElements) {
        int totalPages = pageSize == 0 ? 0
                : (int) Math.ceil((double) totalElements / pageSize);
        return new PageResult<>(List.copyOf(content), pageNumber, pageSize,
                totalElements, totalPages);
    }
}
