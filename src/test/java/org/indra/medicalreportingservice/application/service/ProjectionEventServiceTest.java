package org.indra.medicalreportingservice.application.service;

import org.indra.medicalreportingservice.application.command.*;
import org.indra.medicalreportingservice.application.port.out.ProjectionWritePort;
import org.indra.medicalreportingservice.domain.model.*;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ProjectionEventServiceTest {

    @Test
    void shouldSynchronizePatientAndPropagateFullName() {
        CapturingWriter writer = new CapturingWriter();
        ProjectionEventService service = new ProjectionEventService(writer);
        UUID patientId = UUID.randomUUID();

        service.syncPatient(new SyncPatientProjectionCommand(patientId,
                "ACTUALIZADO", "12345678", " Ana ", " Torres ",
                Instant.parse("2026-09-14T10:00:00Z")));

        assertEquals(patientId.toString(), writer.patient.id());
        assertEquals("Ana Torres", writer.patient.fullName());
        assertEquals("Ana Torres", writer.propagatedName);
    }

    @Test
    void shouldUseOccurredAtWhenLegacyRegistrationHasNoConsultationDate() {
        CapturingWriter writer = new CapturingWriter();
        writer.patientName = "Ana Torres";
        ProjectionEventService service = new ProjectionEventService(writer);
        Instant occurredAt = Instant.parse("2026-09-14T10:00:00Z");

        service.registerConsultation(new RegisterConsultationProjectionCommand(
                UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(),
                UUID.randomUUID(), null, null, occurredAt));

        assertEquals(LocalDateTime.ofInstant(occurredAt, ZoneOffset.UTC),
                writer.consultation.consultationDate());
        assertEquals(ConsultationStatus.IN_PROGRESS,
                writer.consultation.status());
    }

    @Test
    void shouldProjectLegacyAppointmentCreatedEventAsConfirmed() {
        CapturingWriter writer = new CapturingWriter();
        ProjectionEventService service = new ProjectionEventService(writer);
        UUID appointmentId = UUID.randomUUID();

        service.registerAppointment(new RegisterAppointmentProjectionCommand(
                appointmentId, UUID.randomUUID(), UUID.randomUUID(), null,
                LocalDateTime.of(2026, 9, 20, 10, 0), null, null,
                Instant.parse("2026-09-14T10:00:00Z")));

        assertEquals(appointmentId.toString(), writer.appointment.id());
        assertEquals(AppointmentStatus.CONFIRMED,
                writer.appointment.status());
    }

    @Test
    void shouldProjectAppointmentLifecycleChanges() {
        CapturingWriter writer = new CapturingWriter();
        ProjectionEventService service = new ProjectionEventService(writer);
        UUID appointmentId = UUID.randomUUID();
        LocalDateTime newDate = LocalDateTime.of(2026, 9, 22, 15, 30);

        service.rescheduleAppointment(
                new RescheduleAppointmentProjectionCommand(appointmentId,
                        LocalDateTime.of(2026, 9, 20, 10, 0), newDate,
                        Instant.parse("2026-09-15T10:00:00Z")));
        service.cancelAppointment(new CancelAppointmentProjectionCommand(
                appointmentId, "Solicitud del paciente",
                Instant.parse("2026-09-16T10:00:00Z")));

        assertEquals(appointmentId.toString(), writer.appointmentId);
        assertEquals(newDate, writer.rescheduledDate);
        assertEquals("Solicitud del paciente", writer.cancellationReason);
    }

    private static final class CapturingWriter implements ProjectionWritePort {
        private PatientProjection patient;
        private String propagatedName;
        private String patientName;
        private ConsultationProjection consultation;
        private AppointmentProjection appointment;
        private String appointmentId;
        private LocalDateTime rescheduledDate;
        private String cancellationReason;

        @Override
        public boolean upsertPatient(PatientProjection patient) {
            this.patient = patient;
            return true;
        }

        @Override
        public void propagatePatientName(String patientId, String fullName,
                                         LocalDateTime updatedAt) {
            this.propagatedName = fullName;
        }

        @Override
        public String findPatientName(String patientId) {
            return patientName;
        }

        @Override
        public void registerAppointment(AppointmentProjection appointment) {
            this.appointment = appointment;
        }

        @Override
        public void rescheduleAppointment(String appointmentId,
                                          LocalDateTime newDateTime,
                                          LocalDateTime updatedAt) {
            this.appointmentId = appointmentId;
            this.rescheduledDate = newDateTime;
        }

        @Override
        public void cancelAppointment(String appointmentId, String reason,
                                      LocalDateTime updatedAt) {
            this.appointmentId = appointmentId;
            this.cancellationReason = reason;
        }

        @Override
        public void registerConsultation(ConsultationProjection consultation) {
            this.consultation = consultation;
        }

        @Override
        public void addDiagnosis(String consultationId,
                                 DiagnosisProjection diagnosis,
                                 LocalDateTime updatedAt) {
        }

        @Override
        public void addPrescription(String consultationId,
                                    PrescriptionProjection prescription,
                                    LocalDateTime updatedAt) {
        }

        @Override
        public void addAttachment(String consultationId,
                                  AttachmentProjection attachment,
                                  LocalDateTime updatedAt) {
        }

        @Override
        public void completeConsultation(String consultationId,
                                         LocalDateTime updatedAt) {
        }
    }
}
