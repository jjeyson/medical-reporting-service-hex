package org.indra.medicalreportingservice.application.service;

import org.indra.medicalreportingservice.application.command.*;
import org.indra.medicalreportingservice.application.port.in.ProjectionEventUseCase;
import org.indra.medicalreportingservice.application.port.out.ProjectionWritePort;
import org.indra.medicalreportingservice.domain.model.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Locale;

public final class ProjectionEventService implements ProjectionEventUseCase {

    private final ProjectionWritePort projectionWriter;

    public ProjectionEventService(ProjectionWritePort projectionWriter) {
        this.projectionWriter = projectionWriter;
    }

    @Override
    public void syncPatient(SyncPatientProjectionCommand command) {
        LocalDateTime occurredAt = toDateTime(command.occurredAt());
        String fullName = joinName(command.firstName(), command.lastName());
        String patientId = command.patientId().toString();
        boolean applied = projectionWriter.upsertPatient(new PatientProjection(
                patientId, command.dni(), fullName, occurredAt));
        if (applied) {
            projectionWriter.propagatePatientName(patientId, fullName,
                    occurredAt);
        }
    }

    @Override
    public void registerAppointment(
            RegisterAppointmentProjectionCommand command) {
        LocalDateTime occurredAt = toDateTime(command.occurredAt());
        projectionWriter.registerAppointment(new AppointmentProjection(
                command.appointmentId().toString(),
                command.patientId().toString(), command.doctorId().toString(),
                command.specialty(), command.appointmentDateTime(),
                command.reason(), appointmentStatus(command.status()), null,
                occurredAt, occurredAt));
    }

    @Override
    public void rescheduleAppointment(
            RescheduleAppointmentProjectionCommand command) {
        projectionWriter.rescheduleAppointment(
                command.appointmentId().toString(), command.newDateTime(),
                toDateTime(command.occurredAt()));
    }

    @Override
    public void cancelAppointment(
            CancelAppointmentProjectionCommand command) {
        projectionWriter.cancelAppointment(command.appointmentId().toString(),
                command.reason(), toDateTime(command.occurredAt()));
    }

    @Override
    public void registerConsultation(
            RegisterConsultationProjectionCommand command) {
        LocalDateTime occurredAt = toDateTime(command.occurredAt());
        LocalDateTime consultationDate = command.consultationDate() == null
                ? occurredAt : command.consultationDate();
        String patientId = command.patientId().toString();
        projectionWriter.registerConsultation(new ConsultationProjection(
                command.consultationId().toString(),
                command.appointmentId().toString(), patientId,
                projectionWriter.findPatientName(patientId),
                command.doctorId().toString(), consultationDate,
                command.reason(), ConsultationStatus.IN_PROGRESS,
                List.of(), List.of(), List.of(), occurredAt, occurredAt));
    }

    @Override
    public void addDiagnosis(AddDiagnosisProjectionCommand command) {
        projectionWriter.addDiagnosis(command.consultationId().toString(),
                new DiagnosisProjection(command.diagnosisId().toString(),
                        command.icd10Code(), command.type()),
                toDateTime(command.occurredAt()));
    }

    @Override
    public void addPrescription(AddPrescriptionProjectionCommand command) {
        projectionWriter.addPrescription(command.consultationId().toString(),
                new PrescriptionProjection(command.prescriptionId().toString(),
                        command.medication()), toDateTime(command.occurredAt()));
    }

    @Override
    public void addAttachment(AddAttachmentProjectionCommand command) {
        projectionWriter.addAttachment(command.consultationId().toString(),
                new AttachmentProjection(command.attachmentId().toString(),
                        command.fileName()), toDateTime(command.occurredAt()));
    }

    @Override
    public void completeConsultation(
            CompleteConsultationProjectionCommand command) {
        projectionWriter.completeConsultation(command.consultationId().toString(),
                toDateTime(command.occurredAt()));
    }

    private LocalDateTime toDateTime(Instant instant) {
        return LocalDateTime.ofInstant(
                instant == null ? Instant.now() : instant, ZoneOffset.UTC);
    }

    private String joinName(String firstName, String lastName) {
        return ((firstName == null ? "" : firstName.trim()) + " "
                + (lastName == null ? "" : lastName.trim())).trim();
    }

    private AppointmentStatus appointmentStatus(String status) {
        if (status == null || status.isBlank()) {
            return AppointmentStatus.CONFIRMED;
        }
        try {
            return AppointmentStatus.valueOf(
                    status.trim().toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException exception) {
            return AppointmentStatus.CONFIRMED;
        }
    }
}
