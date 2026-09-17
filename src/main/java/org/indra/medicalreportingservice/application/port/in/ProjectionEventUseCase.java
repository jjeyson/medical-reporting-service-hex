package org.indra.medicalreportingservice.application.port.in;

import org.indra.medicalreportingservice.application.command.*;

public interface ProjectionEventUseCase {

    void syncPatient(SyncPatientProjectionCommand command);

    void registerAppointment(RegisterAppointmentProjectionCommand command);

    void rescheduleAppointment(RescheduleAppointmentProjectionCommand command);

    void cancelAppointment(CancelAppointmentProjectionCommand command);

    void registerConsultation(RegisterConsultationProjectionCommand command);

    void addDiagnosis(AddDiagnosisProjectionCommand command);

    void addPrescription(AddPrescriptionProjectionCommand command);

    void addAttachment(AddAttachmentProjectionCommand command);

    void completeConsultation(CompleteConsultationProjectionCommand command);
}
