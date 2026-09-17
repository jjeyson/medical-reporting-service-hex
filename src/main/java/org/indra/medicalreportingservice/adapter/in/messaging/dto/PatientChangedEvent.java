package org.indra.medicalreportingservice.adapter.in.messaging.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.time.Instant;
import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
public record PatientChangedEvent(UUID pacienteId, String tipoEvento, String dni,
                                  String nombres, String apellidos,
                                  Instant fechaEvento) {
}
