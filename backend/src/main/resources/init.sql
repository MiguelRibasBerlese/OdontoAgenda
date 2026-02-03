-- Script de inicialização do banco de dados OdontoAgenda
-- PostgreSQL 12+

-- Criação do banco de dados (executar como superuser)
-- CREATE DATABASE odontoagenda;

-- Conectar ao banco odontoagenda antes de executar o restante

-- Inserir especialidades padrão
INSERT INTO specialties (name, description, default_duration_minutes, active) VALUES
('Clínica Geral', 'Consultas de rotina, limpeza e procedimentos básicos', 30, true),
('Ortodontia', 'Tratamento com aparelhos ortodônticos e alinhadores', 45, true),
('Endodontia', 'Tratamento de canal e procedimentos relacionados', 60, true),
('Periodontia', 'Tratamento de gengivas e estruturas de suporte dos dentes', 45, true),
('Implantodontia', 'Colocação e manutenção de implantes dentários', 90, true),
('Odontopediatria', 'Atendimento odontológico infantil', 30, true),
('Prótese Dentária', 'Confecção e instalação de próteses', 60, true),
('Estética Dental', 'Clareamento, facetas e procedimentos estéticos', 45, true);

-- Inserir usuário administrador padrão
-- Senha: admin123 (hash BCrypt gerado com strength 10)
INSERT INTO users (email, password, full_name, phone, role, active, created_at, updated_at) VALUES
('admin@odontoagenda.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Administrador', '(11) 99999-9999', 'ADMIN', true, NOW(), NOW());

-- Configuração padrão para banner (será atualizado via upload)
INSERT INTO clinic_settings (setting_key, setting_value, updated_at) VALUES
('BANNER_IMAGE', 'default-banner.jpg', NOW());

-- Índices adicionais para performance
CREATE INDEX IF NOT EXISTS idx_appointments_dentist_date ON appointments(dentist_id, appointment_date_time);
CREATE INDEX IF NOT EXISTS idx_appointments_patient_date ON appointments(patient_id, appointment_date_time);
CREATE INDEX IF NOT EXISTS idx_appointments_status ON appointments(status);
CREATE INDEX IF NOT EXISTS idx_users_email ON users(email);
CREATE INDEX IF NOT EXISTS idx_users_role_active ON users(role, active);
