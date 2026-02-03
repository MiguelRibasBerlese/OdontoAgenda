import api from './api';
import {
  CreateDentistRequest,
  CreateSpecialtyRequest,
  Dentist,
  Specialty,
} from '../types';

export const adminService = {
  /**
   * Cria um novo dentista.
   */
  createDentist: async (data: CreateDentistRequest): Promise<Dentist> => {
    const response = await api.post<Dentist>('/admin/dentists', data);
    return response.data;
  },

  /**
   * Lista todos os dentistas.
   */
  listDentists: async (): Promise<Dentist[]> => {
    const response = await api.get<Dentist[]>('/admin/dentists');
    return response.data;
  },

  /**
   * Desativa um dentista.
   */
  deactivateDentist: async (dentistId: number): Promise<void> => {
    await api.delete(`/admin/dentists/${dentistId}`);
  },

  /**
   * Cria uma nova especialidade.
   */
  createSpecialty: async (data: CreateSpecialtyRequest): Promise<Specialty> => {
    const response = await api.post<Specialty>('/admin/specialties', data);
    return response.data;
  },

  /**
   * Desativa uma especialidade.
   */
  deactivateSpecialty: async (specialtyId: number): Promise<void> => {
    await api.delete(`/admin/specialties/${specialtyId}`);
  },

  /**
   * Atualiza o banner institucional.
   */
  updateBanner: async (file: File): Promise<string> => {
    const formData = new FormData();
    formData.append('file', file);

    const response = await api.put<string>('/admin/clinic/banner', formData, {
      headers: {
        'Content-Type': 'multipart/form-data',
      },
    });
    return response.data;
  },
};
