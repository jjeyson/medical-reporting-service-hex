package org.indra.medicalreportingservice.adapter.out.mongodb;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Accumulators;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import jakarta.enterprise.context.ApplicationScoped;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.indra.medicalreportingservice.adapter.out.mongodb.document.ConsultationViewDocument;
import org.indra.medicalreportingservice.application.port.out.DashboardQueryPort;
import org.indra.medicalreportingservice.domain.model.DashboardMetrics;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class MongoDashboardQueryAdapter implements DashboardQueryPort {

    @Override
    public DashboardMetrics calculate(LocalDateTime since,
                                      int topDiagnosisLimit) {
        MongoCollection<Document> collection = ConsultationViewDocument
                .mongoCollection().withDocumentClass(Document.class);
        return new DashboardMetrics(collection.countDocuments(),
                countByStatus(collection), countByDay(collection, since),
                topDiagnoses(collection, topDiagnosisLimit));
    }

    private List<DashboardMetrics.StatusCount> countByStatus(
            MongoCollection<Document> collection) {
        List<DashboardMetrics.StatusCount> result = new ArrayList<>();
        for (Document document : collection.aggregate(List.of(
                Aggregates.group("$status", Accumulators.sum("count", 1))))) {
            result.add(new DashboardMetrics.StatusCount(
                    document.getString("_id"), number(document, "count")));
        }
        return result;
    }

    private List<DashboardMetrics.DailyCount> countByDay(
            MongoCollection<Document> collection, LocalDateTime since) {
        List<Bson> pipeline = List.of(
                Aggregates.match(Filters.gte("consultationDate", since)),
                Aggregates.group(new Document("$dateToString",
                                new Document("format", "%Y-%m-%d")
                                        .append("date", "$consultationDate")),
                        Accumulators.sum("count", 1)),
                Aggregates.sort(Sorts.ascending("_id")));
        List<DashboardMetrics.DailyCount> result = new ArrayList<>();
        for (Document document : collection.aggregate(pipeline)) {
            result.add(new DashboardMetrics.DailyCount(
                    document.getString("_id"), number(document, "count")));
        }
        return result;
    }

    private List<DashboardMetrics.TopDiagnosis> topDiagnoses(
            MongoCollection<Document> collection, int limit) {
        List<Bson> pipeline = List.of(
                Aggregates.unwind("$diagnoses"),
                Aggregates.group("$diagnoses.icd10Code",
                        Accumulators.sum("count", 1)),
                Aggregates.sort(Sorts.descending("count")),
                Aggregates.limit(limit));
        List<DashboardMetrics.TopDiagnosis> result = new ArrayList<>();
        for (Document document : collection.aggregate(pipeline)) {
            result.add(new DashboardMetrics.TopDiagnosis(
                    document.getString("_id"), number(document, "count")));
        }
        return result;
    }

    private long number(Document document, String field) {
        Number value = document.get(field, Number.class);
        return value == null ? 0 : value.longValue();
    }
}
