package org.indra.medicalreportingservice.application.port.out;

import org.indra.medicalreportingservice.domain.model.*;

import java.time.LocalDateTime;

public interface ProjectionWritePort {

    boolean upsertPatient(PatientProjection patient);

    void propagatePatientName(String patientId, String fullName,
                              LocalDateTime updatedAt);

    String findPatientName(String patientId);

    void registerAppointment(AppointmentProjection appointment);

    void rescheduleAppointment(String appointmentId,
                               LocalDateTime newDateTime,
                               LocalDateTime updatedAt);

    void cancelAppointment(String appointmentId, String reason,
                           LocalDateTime updatedAt);

    void registerConsultation(ConsultationProjection consultation);

    void addDiagnosis(String consultationId, DiagnosisProjection diagnosis,
                      LocalDateTime updatedAt);

    void addPrescription(String consultationId,
                         PrescriptionProjection prescription,
                         LocalDateTime updatedAt);

    void addAttachment(String consultationId, AttachmentProjection attachment,
                       LocalDateTime updatedAt);

    void completeConsultation(String consultationId,
                              LocalDateTime updatedAt);
}
