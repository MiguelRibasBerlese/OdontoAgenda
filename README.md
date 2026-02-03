# ODONTOAGENDA - Sistema de Agendamento Odontológico

Sistema web profissional para gerenciamento de agendamentos em consultórios odontológicos.

## 🚀 Tecnologias

### Backend
- Java 17
- Spring Boot 3.2.2
- Spring Security + JWT
- Spring Data JPA
- PostgreSQL
- Bean Validation
- Lombok

### Frontend
- React 18
- TypeScript
- Vite
- Material UI (MUI)
- React Router DOM
- Axios
- React Hook Form + Zod

## 📋 Pré-requisitos

- JDK 17+
- Node.js 18+
- PostgreSQL 12+
- Maven 3.8+

## 🔧 Instalação e Configuração

### 1. Banco de Dados

```bash
# Criar banco de dados
createdb odontoagenda

# Executar script de inicialização
psql -d odontoagenda -f backend/src/main/resources/init.sql
```

### 2. Backend

```bash
cd backend

# Configurar application.properties (já configurado por padrão)
# Ajustar credenciais do banco se necessário

# Compilar e executar
mvn clean install
mvn spring-boot:run
```

O backend estará disponível em: `http://localhost:8080`

### 3. Frontend

```bash
cd frontend

# Instalar dependências
npm install

# Executar em modo desenvolvimento
npm run dev
```

O frontend estará disponível em: `http://localhost:5173`

## 👥 Perfis de Usuário

### PATIENT (Paciente)
- Cadastro e autenticação
- Visualizar dentistas e especialidades
- Agendar consultas
- Visualizar histórico de consultas
- Cancelar/remarcar consultas

### DENTIST (Dentista)
- Visualizar agenda
- Gerenciar consultas
- Atualizar status de consultas
- Remarcar consultas

### ADMIN (Administrador)
- Todas as funcionalidades de dentista
- Cadastrar dentistas
- Gerenciar especialidades
- Atualizar banner institucional

## 🔐 Credenciais Padrão

**Administrador:**
- Email: `admin@odontoagenda.com`
- Senha: `admin123`

> ⚠️ **Importante**: Altere a senha padrão do administrador após primeiro acesso!

## 📁 Estrutura do Projeto

```
OdontoAgenda/
├── backend/
│   ├── src/main/java/com/odontoagenda/
│   │   ├── config/          # Configurações (Security, CORS)
│   │   ├── controller/      # Controllers REST
│   │   ├── dto/             # DTOs (request/response)
│   │   ├── exception/       # Exceções e handlers
│   │   ├── model/           # Entidades JPA
│   │   ├── repository/      # Repositories JPA
│   │   ├── security/        # JWT e filtros
│   │   └── service/         # Lógica de negócio
│   └── src/main/resources/
│       ├── application.properties
│       └── init.sql
└── frontend/
    ├── src/
    │   ├── components/      # Componentes reutilizáveis
    │   ├── contexts/        # Contexts (Auth, etc)
    │   ├── pages/           # Páginas da aplicação
    │   ├── services/        # API services
    │   └── types/           # TypeScript types
    └── package.json
```

## 🔒 Segurança

- Autenticação via JWT
- Senhas criptografadas com BCrypt
- Controle de acesso baseado em roles
- Validação de entrada em backend e frontend
- Proteção contra upload malicioso
- CORS configurado

## 📝 Endpoints Principais

### Autenticação
- `POST /api/auth/register` - Cadastro de paciente
- `POST /api/auth/login` - Login
- `POST /api/auth/forgot-password` - Recuperação de senha

### Público
- `GET /api/public/specialties` - Lista especialidades
- `GET /api/public/dentists` - Lista dentistas
- `GET /api/public/clinic/banner` - Banner institucional

### Paciente
- `POST /api/patient/appointments` - Criar agendamento
- `GET /api/patient/appointments` - Listar consultas
- `PUT /api/patient/appointments/{id}/cancel` - Cancelar consulta

### Dentista
- `GET /api/dentist/appointments` - Agenda
- `PUT /api/dentist/appointments/{id}/status` - Atualizar status
- `PUT /api/dentist/appointments/{id}/reschedule` - Remarcar

### Admin
- `POST /api/admin/dentists` - Cadastrar dentista
- `POST /api/admin/specialties` - Cadastrar especialidade
- `PUT /api/admin/clinic/banner` - Atualizar banner

## 🎨 Banner Institucional

O sistema suporta upload de banner institucional:
- Formatos aceitos: JPG, PNG, WEBP
- Tamanho máximo: 2MB
- Proporção recomendada: 3:1
- Apenas administradores podem alterar

## 📦 Build para Produção

### Backend
```bash
cd backend
mvn clean package -DskipTests
java -jar target/odontoagenda-backend-1.0.0.jar
```

### Frontend
```bash
cd frontend
npm run build
# Os arquivos estarão em dist/
```

## 🧪 Testes

```bash
# Backend
cd backend
mvn test

# Frontend
cd frontend
npm test
```

## 📄 Licença

Este projeto foi desenvolvido para fins educacionais e profissionais.

## 👨‍💻 Desenvolvimento

Sistema desenvolvido seguindo boas práticas de:
- Clean Code
- SOLID
- Design Patterns
- REST API
- Segurança
- UX/UI

---

**OdontoAgenda** - Simplificando o agendamento odontológico 🦷
