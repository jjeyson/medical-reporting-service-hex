package org.indra.medicalreportingservice.adapter.out.mongodb.repository;

import io.quarkus.mongodb.panache.PanacheMongoRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import org.indra.medicalreportingservice.adapter.out.mongodb.document.AppointmentViewDocument;

@ApplicationScoped
public class AppointmentViewMongoRepository
        implements PanacheMongoRepositoryBase<AppointmentViewDocument, String> {
}
