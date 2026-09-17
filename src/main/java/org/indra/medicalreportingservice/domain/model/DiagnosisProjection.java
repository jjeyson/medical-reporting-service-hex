package org.indra.medicalreportingservice.domain.model;

public record DiagnosisProjection(String diagnosisId, String icd10Code,
                                  String type) {
}
