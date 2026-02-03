import api from './api';
import {
  Appointment,
  UpdateAppointmentStatusRequest,
  RescheduleAppointmentRequest,
} from '../types';

export const dentistService = {
  /**
   * Lista agendamentos do dentista em um período.
   */
  listAppointments: async (
    startDate: string,
    endDate: string
  ): Promise<Appointment[]> => {
    const response = await api.get<Appointment[]>('/dentist/appointments', {
      params: { startDate, endDate },
    });
    return response.data;
  },

  /**
   * Atualiza status de um agendamento.
   */
  updateAppointmentStatus: async (
    appointmentId: number,
    data: UpdateAppointmentStatusRequest
  ): Promise<Appointment> => {
    const response = await api.put<Appointment>(
      `/dentist/appointments/${appointmentId}/status`,
      data
    );
    return response.data;
  },

  /**
   * Remarca um agendamento.
   */
  rescheduleAppointment: async (
    appointmentId: number,
    data: RescheduleAppointmentRequest
  ): Promise<Appointment> => {
    const response = await api.put<Appointment>(
      `/dentist/appointments/${appointmentId}/reschedule`,
      data
    );
    return response.data;
  },
};
