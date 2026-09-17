package org.indra.medicalreportingservice.adapter.out.mongodb.repository;

import io.quarkus.mongodb.panache.PanacheMongoRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import org.indra.medicalreportingservice.adapter.out.mongodb.document.ConsultationViewDocument;

@ApplicationScoped
public class ConsultationViewMongoRepository
        implements PanacheMongoRepositoryBase<ConsultationViewDocument, String> {
}