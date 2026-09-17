package org.indra.medicalreportingservice.adapter.in.messaging;

import io.smallrye.reactive.messaging.annotations.Blocking;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.indra.medicalreportingservice.adapter.in.messaging.dto.*;
import org.indra.medicalreportingservice.application.command.*;
import org.indra.medicalreportingservice.application.port.in.ProjectionEventUseCase;

@ApplicationScoped
public class ReportingEventConsumer {

    private final ProjectionEventUseCase useCase;

    @Inject
    public ReportingEventConsumer(ProjectionEventUseCase useCase) {
        this.useCase = useCase;
    }

    @Incoming("patient-events-in")
    @Blocking
    public void onPatientEvent(PatientChangedEvent event) {
        useCase.syncPatient(new SyncPatientProjectionCommand(event.pacienteId(),
                event.tipoEvento(), event.dni(), event.nombres(),
                event.apellidos(), event.fechaEvento()));
    }

    @Incoming("appointment-created-in")
    @Blocking
    public void onAppointmentCreated(AppointmentCreatedEvent event) {
        useCase.registerAppointment(new RegisterAppointmentProjectionCommand(
                event.appointmentId(), event.patientId(), event.doctorId(),
                event.specialty(), event.appointmentDateTime(), event.reason(),
                event.status(), event.occurredAt()));
    }

    @Incoming("appointment-rescheduled-in")
    @Blocking
    public void onAppointmentRescheduled(AppointmentRescheduledEvent event) {
        useCase.rescheduleAppointment(
                new RescheduleAppointmentProjectionCommand(
                        event.appointmentId(), event.previousDateTime(),
                        event.newDateTime(), event.occurredAt()));
    }

    @Incoming("appointment-cancelled-in")
    @Blocking
    public void onAppointmentCancelled(AppointmentCancelledEvent event) {
        useCase.cancelAppointment(new CancelAppointmentProjectionCommand(
                event.appointmentId(), event.reason(), event.occurredAt()));
    }

    @Incoming("consultation-registered-in")
    @Blocking
    public void onConsultationRegistered(ConsultationRegisteredEvent event) {
        useCase.registerConsultation(new RegisterConsultationProjectionCommand(
                event.consultationId(), event.appointmentId(), event.patientId(),
                event.doctorId(), event.consultationDate(), event.reason(),
                event.occurredAt()));
    }

    @Incoming("diagnosis-added-in")
    @Blocking
    public void onDiagnosisAdded(DiagnosisAddedEvent event) {
        useCase.addDiagnosis(new AddDiagnosisProjectionCommand(
                event.consultationId(), event.diagnosisId(), event.icd10Code(),
                event.type(), event.occurredAt()));
    }

    @Incoming("prescription-issued-in")
    @Blocking
    public void onPrescriptionIssued(PrescriptionIssuedEvent event) {
        useCase.addPrescription(new AddPrescriptionProjectionCommand(
                event.consultationId(), event.prescriptionId(),
                event.medication(), event.occurredAt()));
    }

    @Incoming("document-attached-in")
    @Blocking
    public void onDocumentAttached(DocumentAttachedEvent event) {
        useCase.addAttachment(new AddAttachmentProjectionCommand(
                event.consultationId(), event.attachmentId(), event.fileName(),
                event.occurredAt()));
    }

    @Incoming("consultation-completed-in")
    @Blocking
    public void onConsultationCompleted(ConsultationCompletedEvent event) {
        useCase.completeConsultation(new CompleteConsultationProjectionCommand(
                event.consultationId(), event.occurredAt()));
    }
}
