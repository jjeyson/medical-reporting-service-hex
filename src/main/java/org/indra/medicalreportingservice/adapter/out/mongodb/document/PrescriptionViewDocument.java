package org.indra.medicalreportingservice.adapter.out.mongodb.document;

public class PrescriptionViewDocument {

    public String prescriptionId;
    public String medication;

    public PrescriptionViewDocument() {
    }

    public PrescriptionViewDocument(String prescriptionId, String medication) {
        this.prescriptionId = prescriptionId;
        this.medication = medication;
    }
}
