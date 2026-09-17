package org.indra.medicalreportingservice.adapter.in.rest.security;

public final class SecurityRoles {

    public static final String MEDICO = "MEDICO";
    public static final String RECEPCIONISTA = "RECEPCIONISTA";
    public static final String ADMINISTRADOR = "ADMINISTRADOR";
    public static final String PACIENTE = "PACIENTE";

    // Compatibilidad temporal con los roles del servicio original.
    public static final String DOCTOR = "DOCTOR";
    public static final String RECEPTIONIST = "RECEPTIONIST";
    public static final String ADMINISTRATOR = "ADMINISTRATOR";
    public static final String PATIENT = "PATIENT";

    private SecurityRoles() {
    }
}
