import api from './api';
import { Dentist, Specialty } from '../types';

export const publicService = {
  /**
   * Lista todas as especialidades disponíveis.
   */
  listSpecialties: async (): Promise<Specialty[]> => {
    const response = await api.get<Specialty[]>('/public/specialties');
    return response.data;
  },

  /**
   * Lista todos os dentistas ativos.
   */
  listDentists: async (): Promise<Dentist[]> => {
    const response = await api.get<Dentist[]>('/public/dentists');
    return response.data;
  },

  /**
   * Lista dentistas por especialidade.
   */
  listDentistsBySpecialty: async (specialtyId: number): Promise<Dentist[]> => {
    const response = await api.get<Dentist[]>(
      `/public/dentists/specialty/${specialtyId}`
    );
    return response.data;
  },

  /**
   * Obtém a URL do banner institucional.
   */
  getBannerUrl: (): string => {
    return '/api/public/clinic/banner';
  },
};
