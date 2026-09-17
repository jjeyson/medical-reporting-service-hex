package org.indra.medicalreportingservice.adapter.out.mongodb;

import com.mongodb.client.FindIterable;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import jakarta.enterprise.context.ApplicationScoped;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.indra.medicalreportingservice.adapter.out.mongodb.document.ConsultationViewDocument;
import org.indra.medicalreportingservice.adapter.out.mongodb.mapper.MongoProjectionMapper;
import org.indra.medicalreportingservice.adapter.out.mongodb.repository.ConsultationViewMongoRepository;
import org.indra.medicalreportingservice.application.model.PageResult;
import org.indra.medicalreportingservice.application.model.ReportSearchCriteria;
import org.indra.medicalreportingservice.application.port.out.ConsultationProjectionQueryPort;
import org.indra.medicalreportingservice.domain.model.ConsultationProjection;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class MongoConsultationQueryAdapter
        implements ConsultationProjectionQueryPort {

    private final ConsultationViewMongoRepository repository;

    public MongoConsultationQueryAdapter(
            ConsultationViewMongoRepository repository) {
        this.repository = repository;
    }

    @Override
    public PageResult<ConsultationProjection> search(
            ReportSearchCriteria criteria, int page, int size) {

        List<Bson> filters = new ArrayList<>();

        addEqual(filters, "patientId", criteria.patientId());
        addEqual(filters, "doctorId", criteria.doctorId());

        if (criteria.status() != null) {
            filters.add( Filters.eq("status",criteria.status().name()));
        }

        if (criteria.dateFrom() != null) {
            filters.add( Filters.gte("consultationDate", criteria.dateFrom()));
        }

        if (criteria.dateTo() != null) {
            filters.add( Filters.lte("consultationDate", criteria.dateTo()));
        }

        Bson filter = filters.isEmpty() ? new Document() : Filters.and(filters);

        long total = repository.mongoCollection().countDocuments(filter);

        FindIterable<ConsultationViewDocument> result =
                repository.mongoCollection()
                        .find(filter)
                        .sort(Sorts.descending("consultationDate","_id"))
                        .skip(Math.toIntExact((long) page * size))
                        .limit(size);

        List<ConsultationProjection> content =
                new ArrayList<>();

        for (ConsultationViewDocument document : result) {
            content.add(
                    MongoProjectionMapper.toDomain(document)
            );
        }

        return PageResult.of( content,page,size,total);
    }

    private void addEqual( List<Bson> filters, String field, String value) {

        if (value != null && !value.isBlank()) {
            filters.add( Filters.eq( field, value.trim()));
        }
    }
}