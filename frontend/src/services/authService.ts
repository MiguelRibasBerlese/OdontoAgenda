import api from './api';
import {
  AuthResponse,
  LoginRequest,
  RegisterPatientRequest,
} from '../types';

export const authService = {
  /**
   * Realiza login no sistema.
   */
  login: async (credentials: LoginRequest): Promise<AuthResponse> => {
    const response = await api.post<AuthResponse>('/auth/login', credentials);
    return response.data;
  },

  /**
   * Registra um novo paciente.
   */
  register: async (data: RegisterPatientRequest): Promise<AuthResponse> => {
    const response = await api.post<AuthResponse>('/auth/register', data);
    return response.data;
  },

  /**
   * Solicita recuperação de senha.
   */
  forgotPassword: async (email: string): Promise<void> => {
    await api.post('/auth/forgot-password', { email });
  },

  /**
   * Faz logout removendo credenciais do localStorage.
   */
  logout: () => {
    localStorage.removeItem('token');
    localStorage.removeItem('user');
  },
};
