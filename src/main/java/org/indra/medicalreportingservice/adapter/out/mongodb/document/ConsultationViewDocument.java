package org.indra.medicalreportingservice.adapter.out.mongodb.document;

import io.quarkus.mongodb.panache.PanacheMongoEntityBase;
import io.quarkus.mongodb.panache.common.MongoEntity;
import org.bson.codecs.pojo.annotations.BsonId;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@MongoEntity(collection = "consultation_view")
public class ConsultationViewDocument extends PanacheMongoEntityBase {

    @BsonId
    public String id;
    public String appointmentId;
    public String patientId;
    public String patientName;
    public String doctorId;
    public LocalDateTime consultationDate;
    public String reason;
    public String status;
    public List<DiagnosisViewDocument> diagnoses = new ArrayList<>();
    public List<PrescriptionViewDocument> prescriptions = new ArrayList<>();
    public List<AttachmentViewDocument> attachments = new ArrayList<>();
    public LocalDateTime createdAt;
    public LocalDateTime lastUpdatedAt;
    public LocalDateTime patientUpdatedAt;
}
