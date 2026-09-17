package org.indra.medicalreportingservice.adapter.out.mongodb;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.Updates;
import jakarta.enterprise.context.ApplicationScoped;
import org.bson.conversions.Bson;
import org.indra.medicalreportingservice.adapter.out.mongodb.document.*;
import org.indra.medicalreportingservice.adapter.out.mongodb.repository.ConsultationViewMongoRepository;
import org.indra.medicalreportingservice.adapter.out.mongodb.repository.AppointmentViewMongoRepository;
import org.indra.medicalreportingservice.adapter.out.mongodb.repository.PatientViewMongoRepository;
import org.indra.medicalreportingservice.application.port.out.ProjectionWritePort;
import org.indra.medicalreportingservice.domain.model.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class MongoProjectionWriteAdapter implements ProjectionWritePort {

    private static final UpdateOptions UPSERT = new UpdateOptions().upsert(true);
    private final PatientViewMongoRepository patientRepository;
    private final AppointmentViewMongoRepository appointmentRepository;
    private final ConsultationViewMongoRepository consultationRepository;

    public MongoProjectionWriteAdapter(
            PatientViewMongoRepository patientRepository,
            AppointmentViewMongoRepository appointmentRepository,
            ConsultationViewMongoRepository consultationRepository) {

        this.patientRepository = patientRepository;
        this.appointmentRepository = appointmentRepository;
        this.consultationRepository = consultationRepository;
    }

    @Override
    public boolean upsertPatient(PatientProjection patient) {
        PatientViewDocument existing =
                patientRepository.mongoCollection()
                        .find(Filters.eq("_id", patient.id()))
                        .first();
        if (existing != null && existing.lastUpdatedAt != null
                && existing.lastUpdatedAt.isAfter(patient.lastUpdatedAt())) {
            return false;
        }
        PatientViewDocument document = new PatientViewDocument();
        document.id = patient.id();
        document.dni = patient.dni();
        document.fullName = patient.fullName();
        document.lastUpdatedAt = patient.lastUpdatedAt();
        patientRepository.mongoCollection().replaceOne(
                Filters.eq("_id", patient.id()),
                document,
                new ReplaceOptions().upsert(true)
        );
        return true;
    }

    @Override
    public void propagatePatientName(String patientId, String fullName,
                                     LocalDateTime updatedAt) {
        ConsultationViewDocument.mongoCollection().updateMany(
                Filters.and(Filters.eq("patientId", patientId),
                        Filters.or(Filters.exists("patientUpdatedAt", false),
                                Filters.lte("patientUpdatedAt", updatedAt))),
                Updates.combine(Updates.set("patientName", fullName),
                        Updates.set("patientUpdatedAt", updatedAt),
                        Updates.max("lastUpdatedAt", updatedAt)));
    }

    @Override
    public String findPatientName(String patientId) {
        PatientViewDocument patient = patientRepository.mongoCollection()
                .find(Filters.eq("_id", patientId))
                .first();
        return patient == null ? null : patient.fullName;
    }

    @Override
    public void registerAppointment(AppointmentProjection appointment) {
        ensureAppointment(appointment.id(), appointment.createdAt());

        List<Bson> identityUpdates = new ArrayList<>();
        identityUpdates.add(Updates.set("patientId", appointment.patientId()));
        identityUpdates.add(Updates.set("doctorId", appointment.doctorId()));
        identityUpdates.add(Updates.min("createdAt", appointment.createdAt()));
        if (appointment.specialty() != null) {
            identityUpdates.add(Updates.set("specialty",
                    appointment.specialty()));
        }
        if (appointment.reason() != null) {
            identityUpdates.add(Updates.set("reason", appointment.reason()));
        }
        appointmentRepository.mongoCollection().updateOne(
                Filters.eq("_id", appointment.id()),
                Updates.combine(identityUpdates));

        List<Bson> currentUpdates = new ArrayList<>();
        if (appointment.appointmentDateTime() != null) {
            currentUpdates.add(Updates.set("appointmentDateTime",
                    appointment.appointmentDateTime()));
        }
        currentUpdates.add(Updates.set("status", appointment.status().name()));
        currentUpdates.add(Updates.set("lastUpdatedAt",
                appointment.lastUpdatedAt()));
        appointmentRepository.mongoCollection().updateOne(
                currentAppointment(appointment.id(),
                        appointment.lastUpdatedAt()),
                Updates.combine(currentUpdates));
    }

    @Override
    public void rescheduleAppointment(String appointmentId,
                                      LocalDateTime newDateTime,
                                      LocalDateTime updatedAt) {
        ensureAppointment(appointmentId, updatedAt);
        appointmentRepository.mongoCollection().updateOne(
                currentAppointment(appointmentId, updatedAt),
                Updates.combine(
                        Updates.set("appointmentDateTime", newDateTime),
                        Updates.set("status",
                                AppointmentStatus.RESCHEDULED.name()),
                        Updates.unset("cancellationReason"),
                        Updates.set("lastUpdatedAt", updatedAt)));
    }

    @Override
    public void cancelAppointment(String appointmentId, String reason,
                                  LocalDateTime updatedAt) {
        ensureAppointment(appointmentId, updatedAt);
        appointmentRepository.mongoCollection().updateOne(
                currentAppointment(appointmentId, updatedAt),
                Updates.combine(
                        Updates.set("status", AppointmentStatus.CANCELLED.name()),
                        Updates.set("cancellationReason", reason),
                        Updates.set("lastUpdatedAt", updatedAt)));
    }

    @Override
    public void registerConsultation(ConsultationProjection consultation) {
        ensureConsultation(consultation.id(), consultation.createdAt());
        List<Bson> updates = new ArrayList<>();
        updates.add(Updates.set("appointmentId", consultation.appointmentId()));
        updates.add(Updates.set("patientId", consultation.patientId()));
        if (consultation.patientName() != null) {
            updates.add(Updates.set("patientName", consultation.patientName()));
        }
        updates.addAll(List.of(
                Updates.set("doctorId", consultation.doctorId()),
                Updates.set("consultationDate", consultation.consultationDate()),
                Updates.set("reason", consultation.reason()),
                Updates.min("createdAt", consultation.createdAt()),
                Updates.max("lastUpdatedAt", consultation.lastUpdatedAt())));
        consultationRepository.mongoCollection().updateOne(
                Filters.eq("_id", consultation.id()), Updates.combine(updates));
    }

    @Override
    public void addDiagnosis(String consultationId,
                             DiagnosisProjection diagnosis,
                             LocalDateTime updatedAt) {
        ensureConsultation(consultationId, updatedAt);
        consultationRepository.mongoCollection().updateOne(
                Filters.and(Filters.eq("_id", consultationId),
                        Filters.ne("diagnoses.diagnosisId",
                                diagnosis.diagnosisId())),
                Updates.combine(Updates.push("diagnoses",
                                new DiagnosisViewDocument(diagnosis.diagnosisId(),
                                        diagnosis.icd10Code(), diagnosis.type())),
                        Updates.max("lastUpdatedAt", updatedAt)));
    }

    @Override
    public void addPrescription(String consultationId,
                                PrescriptionProjection prescription,
                                LocalDateTime updatedAt) {
        ensureConsultation(consultationId, updatedAt);
        consultationRepository.mongoCollection().updateOne(
                Filters.and(Filters.eq("_id", consultationId),
                        Filters.ne("prescriptions.prescriptionId",
                                prescription.prescriptionId())),
                Updates.combine(Updates.push("prescriptions",
                                new PrescriptionViewDocument(
                                        prescription.prescriptionId(),
                                        prescription.medication())),
                        Updates.max("lastUpdatedAt", updatedAt)));
    }

    @Override
    public void addAttachment(String consultationId,
                              AttachmentProjection attachment,
                              LocalDateTime updatedAt) {
        ensureConsultation(consultationId, updatedAt);
        consultationRepository.mongoCollection().updateOne(
                Filters.and(Filters.eq("_id", consultationId),
                        Filters.ne("attachments.attachmentId",
                                attachment.attachmentId())),
                Updates.combine(Updates.push("attachments",
                                new AttachmentViewDocument(
                                        attachment.attachmentId(),
                                        attachment.fileName())),
                        Updates.max("lastUpdatedAt", updatedAt)));
    }

    @Override
    public void completeConsultation(String consultationId,
                                     LocalDateTime updatedAt) {
        ensureConsultation(consultationId, updatedAt);
        consultationRepository.mongoCollection().updateOne(
                Filters.eq("_id", consultationId),
                Updates.combine(Updates.set("status",
                                ConsultationStatus.COMPLETED.name()),
                        Updates.max("lastUpdatedAt", updatedAt)));
    }

    private void ensureConsultation(String consultationId,
                                    LocalDateTime createdAt) {
        consultationRepository.mongoCollection().updateOne(
                Filters.eq("_id", consultationId),
                Updates.combine(
                        Updates.setOnInsert("status",
                                ConsultationStatus.IN_PROGRESS.name()),
                        Updates.setOnInsert("diagnoses", List.of()),
                        Updates.setOnInsert("prescriptions", List.of()),
                        Updates.setOnInsert("attachments", List.of()),
                        Updates.setOnInsert("createdAt", createdAt),
                        Updates.setOnInsert("lastUpdatedAt", createdAt)), UPSERT);
    }

    private void ensureAppointment(String appointmentId,
                                   LocalDateTime occurredAt) {
        appointmentRepository.mongoCollection().updateOne(
                Filters.eq("_id", appointmentId),
                Updates.combine(
                        Updates.setOnInsert("status",
                                AppointmentStatus.PENDING.name()),
                        Updates.setOnInsert("createdAt", occurredAt),
                        Updates.setOnInsert("lastUpdatedAt", occurredAt)),
                UPSERT);
    }

    private Bson currentAppointment(String appointmentId,
                                    LocalDateTime occurredAt) {
        return Filters.and(Filters.eq("_id", appointmentId),
                Filters.or(Filters.exists("lastUpdatedAt", false),
                        Filters.lte("lastUpdatedAt", occurredAt)));
    }
}
