import React from 'react';
import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import { CssBaseline, ThemeProvider, createTheme } from '@mui/material';
import { ptBR } from '@mui/material/locale';
import { AuthProvider } from './contexts/AuthContext';
import { NotificationProvider } from './contexts/NotificationContext';
import ProtectedRoute from './components/ProtectedRoute';
import Layout from './components/Layout';
import { UserRole } from './types';

// Pages
import LoginPage from './pages/LoginPage';
import RegisterPage from './pages/RegisterPage';
import PatientDashboard from './pages/patient/PatientDashboard';
import NewAppointmentPage from './pages/patient/NewAppointmentPage';

// Tema customizado
const theme = createTheme(
  {
    palette: {
      primary: {
        main: '#1976d2',
      },
      secondary: {
        main: '#dc004e',
      },
    },
  },
  ptBR
);

// Página inicial simples
const HomePage: React.FC = () => {
  return (
    <Layout>
      <div style={{ textAlign: 'center', padding: '50px' }}>
        <h1>Bem-vindo ao OdontoAgenda</h1>
        <p>Sistema profissional de agendamento odontológico</p>
      </div>
    </Layout>
  );
};

// Página de não autorizado
const UnauthorizedPage: React.FC = () => {
  return (
    <Layout>
      <div style={{ textAlign: 'center', padding: '50px' }}>
        <h1>Acesso Negado</h1>
        <p>Você não tem permissão para acessar esta página.</p>
      </div>
    </Layout>
  );
};

function App() {
  return (
    <ThemeProvider theme={theme}>
      <CssBaseline />
      <AuthProvider>
        <NotificationProvider>
          <BrowserRouter>
            <Routes>
              {/* Rotas públicas */}
              <Route path="/" element={<HomePage />} />
              <Route path="/login" element={<LoginPage />} />
              <Route path="/register" element={<RegisterPage />} />
              <Route path="/unauthorized" element={<UnauthorizedPage />} />

              {/* Rotas do paciente */}
              <Route
                path="/patient/dashboard"
                element={
                  <ProtectedRoute allowedRoles={[UserRole.PATIENT]}>
                    <PatientDashboard />
                  </ProtectedRoute>
                }
              />
              <Route
                path="/patient/new-appointment"
                element={
                  <ProtectedRoute allowedRoles={[UserRole.PATIENT]}>
                    <NewAppointmentPage />
                  </ProtectedRoute>
                }
              />

              {/* Rota padrão - redireciona para home */}
              <Route path="*" element={<Navigate to="/" replace />} />
            </Routes>
          </BrowserRouter>
        </NotificationProvider>
      </AuthProvider>
    </ThemeProvider>
  );
}

export default App;
