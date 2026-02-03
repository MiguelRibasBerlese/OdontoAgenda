import React, { useEffect, useState } from 'react';
import {
  Container,
  Typography,
  Button,
  Grid,
  Card,
  CardContent,
  CardActions,
  Chip,
  Box,
} from '@mui/material';
import { Add, Event, History } from '@mui/icons-material';
import { useNavigate } from 'react-router-dom';
import { patientService } from '../../services/patientService';
import { Appointment, AppointmentStatus } from '../../types';
import { useNotification } from '../../contexts/NotificationContext';
import Layout from '../../components/Layout';
import Loading from '../../components/Loading';
import BannerHeader from '../../components/BannerHeader';
import { format } from 'date-fns';
import { ptBR } from 'date-fns/locale';

const PatientDashboard: React.FC = () => {
  const navigate = useNavigate();
  const { showError } = useNotification();
  const [loading, setLoading] = useState(true);
  const [upcomingAppointments, setUpcomingAppointments] = useState<Appointment[]>([]);

  useEffect(() => {
    loadUpcomingAppointments();
  }, []);

  const loadUpcomingAppointments = async () => {
    try {
      const appointments = await patientService.listUpcomingAppointments();
      setUpcomingAppointments(appointments);
    } catch (error: any) {
      showError('Erro ao carregar consultas');
    } finally {
      setLoading(false);
    }
  };

  const getStatusColor = (status: AppointmentStatus) => {
    const colors: Record<AppointmentStatus, 'default' | 'success' | 'warning' | 'error' | 'info'> = {
      PENDING: 'warning',
      CONFIRMED: 'success',
      RESCHEDULED: 'info',
      CANCELED: 'error',
      COMPLETED: 'default',
      NO_SHOW: 'error',
    };
    return colors[status];
  };

  const getStatusLabel = (status: AppointmentStatus) => {
    const labels: Record<AppointmentStatus, string> = {
      PENDING: 'Pendente',
      CONFIRMED: 'Confirmada',
      RESCHEDULED: 'Remarcada',
      CANCELED: 'Cancelada',
      COMPLETED: 'Concluída',
      NO_SHOW: 'Não Compareceu',
    };
    return labels[status];
  };

  if (loading) {
    return (
      <Layout>
        <Loading />
      </Layout>
    );
  }

  return (
    <Layout>
      <Container maxWidth="lg">
        <BannerHeader />

        <Box sx={{ mb: 4 }}>
          <Typography variant="h4" gutterBottom>
            Meus Agendamentos
          </Typography>
          <Typography variant="body1" color="text.secondary">
            Gerencie suas consultas odontológicas
          </Typography>
        </Box>

        <Grid container spacing={3} sx={{ mb: 4 }}>
          <Grid item xs={12} md={4}>
            <Card>
              <CardContent>
                <Typography variant="h6" gutterBottom>
                  Nova Consulta
                </Typography>
                <Typography variant="body2" color="text.secondary">
                  Agende uma nova consulta com nossos dentistas
                </Typography>
              </CardContent>
              <CardActions>
                <Button
                  startIcon={<Add />}
                  variant="contained"
                  fullWidth
                  onClick={() => navigate('/patient/new-appointment')}
                >
                  Agendar Consulta
                </Button>
              </CardActions>
            </Card>
          </Grid>

          <Grid item xs={12} md={4}>
            <Card>
              <CardContent>
                <Typography variant="h6" gutterBottom>
                  Minhas Consultas
                </Typography>
                <Typography variant="body2" color="text.secondary">
                  Visualize suas consultas futuras
                </Typography>
              </CardContent>
              <CardActions>
                <Button
                  startIcon={<Event />}
                  variant="outlined"
                  fullWidth
                  onClick={() => navigate('/patient/appointments')}
                >
                  Ver Consultas
                </Button>
              </CardActions>
            </Card>
          </Grid>

          <Grid item xs={12} md={4}>
            <Card>
              <CardContent>
                <Typography variant="h6" gutterBottom>
                  Histórico
                </Typography>
                <Typography variant="body2" color="text.secondary">
                  Consulte o histórico completo
                </Typography>
              </CardContent>
              <CardActions>
                <Button
                  startIcon={<History />}
                  variant="outlined"
                  fullWidth
                  onClick={() => navigate('/patient/history')}
                >
                  Ver Histórico
                </Button>
              </CardActions>
            </Card>
          </Grid>
        </Grid>

        <Typography variant="h5" gutterBottom sx={{ mt: 4 }}>
          Próximas Consultas
        </Typography>

        {upcomingAppointments.length === 0 ? (
          <Card>
            <CardContent>
              <Typography variant="body1" color="text.secondary" align="center">
                Você não tem consultas agendadas
              </Typography>
            </CardContent>
          </Card>
        ) : (
          <Grid container spacing={2}>
            {upcomingAppointments.map((appointment) => (
              <Grid item xs={12} key={appointment.id}>
                <Card>
                  <CardContent>
                    <Grid container spacing={2} alignItems="center">
                      <Grid item xs={12} sm={6}>
                        <Typography variant="h6">
                          {appointment.dentistName}
                        </Typography>
                        <Typography variant="body2" color="text.secondary">
                          {appointment.specialtyName}
                        </Typography>
                      </Grid>
                      <Grid item xs={12} sm={3}>
                        <Typography variant="body2">
                          {format(
                            new Date(appointment.appointmentDateTime),
                            "dd 'de' MMMM 'de' yyyy",
                            { locale: ptBR }
                          )}
                        </Typography>
                        <Typography variant="h6">
                          {format(
                            new Date(appointment.appointmentDateTime),
                            'HH:mm'
                          )}
                        </Typography>
                      </Grid>
                      <Grid item xs={12} sm={3}>
                        <Chip
                          label={getStatusLabel(appointment.status)}
                          color={getStatusColor(appointment.status)}
                        />
                      </Grid>
                    </Grid>
                  </CardContent>
                </Card>
              </Grid>
            ))}
          </Grid>
        )}
      </Container>
    </Layout>
  );
};

export default PatientDashboard;
