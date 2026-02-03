import api from './api';
import {
  Appointment,
  CreateAppointmentRequest,
} from '../types';

export const patientService = {
  /**
   * Cria um novo agendamento.
   */
  createAppointment: async (
    data: CreateAppointmentRequest
  ): Promise<Appointment> => {
    const response = await api.post<Appointment>('/patient/appointments', data);
    return response.data;
  },

  /**
   * Lista todos os agendamentos do paciente.
   */
  listAppointments: async (): Promise<Appointment[]> => {
    const response = await api.get<Appointment[]>('/patient/appointments');
    return response.data;
  },

  /**
   * Lista agendamentos futuros do paciente.
   */
  listUpcomingAppointments: async (): Promise<Appointment[]> => {
    const response = await api.get<Appointment[]>(
      '/patient/appointments/upcoming'
    );
    return response.data;
  },

  /**
   * Cancela um agendamento.
   */
  cancelAppointment: async (
    appointmentId: number,
    reason?: string
  ): Promise<Appointment> => {
    const response = await api.put<Appointment>(
      `/patient/appointments/${appointmentId}/cancel`,
      { reason }
    );
    return response.data;
  },

  /**
   * Solicita remarcação de um agendamento.
   */
  requestReschedule: async (
    appointmentId: number,
    newDateTime: string,
    reason?: string
  ): Promise<Appointment> => {
    const response = await api.put<Appointment>(
      `/patient/appointments/${appointmentId}/request-reschedule`,
      { newAppointmentDateTime: newDateTime, reason }
    );
    return response.data;
  },
};
