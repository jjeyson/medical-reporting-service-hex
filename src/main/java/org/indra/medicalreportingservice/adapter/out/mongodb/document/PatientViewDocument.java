package org.indra.medicalreportingservice.adapter.out.mongodb.document;

import io.quarkus.mongodb.panache.PanacheMongoEntityBase;
import io.quarkus.mongodb.panache.common.MongoEntity;
import org.bson.codecs.pojo.annotations.BsonId;

import java.time.LocalDateTime;

@MongoEntity(collection = "patient_view")
public class PatientViewDocument extends PanacheMongoEntityBase {

    @BsonId
    public String id;
    public String dni;
    public String fullName;
    public LocalDateTime lastUpdatedAt;
}
