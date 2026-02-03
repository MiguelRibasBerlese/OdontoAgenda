// Enums
export enum UserRole {
  PATIENT = 'PATIENT',
  DENTIST = 'DENTIST',
  ADMIN = 'ADMIN',
}

export enum AppointmentStatus {
  PENDING = 'PENDING',
  CONFIRMED = 'CONFIRMED',
  RESCHEDULED = 'RESCHEDULED',
  CANCELED = 'CANCELED',
  COMPLETED = 'COMPLETED',
  NO_SHOW = 'NO_SHOW',
}

// Tipos de usuário
export interface User {
  id: number;
  email: string;
  fullName: string;
  phone: string;
  role: UserRole;
}

export interface AuthResponse {
  token: string;
  email: string;
  fullName: string;
  role: UserRole;
  userId: number;
}

// Especialidades
export interface Specialty {
  id: number;
  name: string;
  description: string;
  defaultDurationMinutes: number;
}

// Dentistas
export interface Dentist {
  id: number;
  fullName: string;
  email: string;
  phone: string;
  cro: string;
  bio?: string;
  specialties: Specialty[];
}

// Agendamentos
export interface Appointment {
  id: number;
  patientId: number;
  patientName: string;
  dentistId: number;
  dentistName: string;
  specialtyId: number;
  specialtyName: string;
  appointmentDateTime: string;
  durationMinutes: number;
  status: AppointmentStatus;
  notes?: string;
  createdAt: string;
}

// DTOs de requisição
export interface RegisterPatientRequest {
  fullName: string;
  email: string;
  password: string;
  phone: string;
  cpf?: string;
}

export interface LoginRequest {
  email: string;
  password: string;
}

export interface CreateAppointmentRequest {
  dentistId: number;
  specialtyId: number;
  appointmentDateTime: string;
  notes?: string;
}

export interface UpdateAppointmentStatusRequest {
  status: AppointmentStatus;
  reason?: string;
}

export interface RescheduleAppointmentRequest {
  newAppointmentDateTime: string;
  reason?: string;
}

export interface CreateDentistRequest {
  fullName: string;
  email: string;
  password: string;
  phone: string;
  cro: string;
  specialtyIds: number[];
  bio?: string;
}

export interface CreateSpecialtyRequest {
  name: string;
  description?: string;
  defaultDurationMinutes: number;
}

// Tipo para erro de API
export interface ApiError {
  message: string;
  status: number;
  timestamp: string;
}
