package org.indra.medicalreportingservice.adapter.out.mongodb.repository;

import io.quarkus.mongodb.panache.PanacheMongoRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import org.indra.medicalreportingservice.adapter.out.mongodb.document.PatientViewDocument;

@ApplicationScoped
public class PatientViewMongoRepository
        implements PanacheMongoRepositoryBase<PatientViewDocument, String> {
}