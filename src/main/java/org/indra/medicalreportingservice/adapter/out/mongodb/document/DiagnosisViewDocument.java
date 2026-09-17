package org.indra.medicalreportingservice.adapter.out.mongodb.document;

public class DiagnosisViewDocument {

    public String diagnosisId;
    public String icd10Code;
    public String type;

    public DiagnosisViewDocument() {
    }

    public DiagnosisViewDocument(String diagnosisId, String icd10Code,
                                 String type) {
        this.diagnosisId = diagnosisId;
        this.icd10Code = icd10Code;
        this.type = type;
    }
}
