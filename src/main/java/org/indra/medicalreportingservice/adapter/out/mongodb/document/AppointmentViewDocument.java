package org.indra.medicalreportingservice.adapter.out.mongodb.document;

import io.quarkus.mongodb.panache.PanacheMongoEntityBase;
import io.quarkus.mongodb.panache.common.MongoEntity;
import org.bson.codecs.pojo.annotations.BsonId;

import java.time.LocalDateTime;

@MongoEntity(collection = "appointment_view")
public class AppointmentViewDocument extends PanacheMongoEntityBase {

    @BsonId
    public String id;
    public String patientId;
    public String doctorId;
    public String specialty;
    public LocalDateTime appointmentDateTime;
    public String reason;
    public String status;
    public String cancellationReason;
    public LocalDateTime createdAt;
    public LocalDateTime lastUpdatedAt;
}
