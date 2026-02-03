package com.odontoagenda.model.enums;

/**
 * Status possíveis para uma consulta.
 */
public enum AppointmentStatus {
    PENDING,        // Aguardando confirmação
    CONFIRMED,      // Confirmada
    RESCHEDULED,    // Remarcada
    CANCELED,       // Cancelada
    COMPLETED,      // Concluída
    NO_SHOW         // Paciente não compareceu
}
