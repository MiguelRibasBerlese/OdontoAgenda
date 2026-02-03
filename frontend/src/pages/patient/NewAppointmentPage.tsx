import React, { useEffect, useState } from 'react';
import {
  Container,
  Paper,
  Typography,
  Button,
  Grid,
  Card,
  CardContent,
  CardActions,
  Stepper,
  Step,
  StepLabel,
  Box,
  FormControl,
  InputLabel,
  Select,
  MenuItem,
  TextField,
} from '@mui/material';
import { useNavigate } from 'react-router-dom';
import { useForm, Controller } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { z } from 'zod';
import { publicService } from '../../services/publicService';
import { patientService } from '../../services/patientService';
import { Specialty, Dentist } from '../../types';
import { useNotification } from '../../contexts/NotificationContext';
import Layout from '../../components/Layout';
import Loading from '../../components/Loading';
import BannerHeader from '../../components/BannerHeader';
import { LocalizationProvider, DateTimePicker } from '@mui/x-date-pickers';
import { AdapterDateFns } from '@mui/x-date-pickers/AdapterDateFns';
import { ptBR } from 'date-fns/locale';
import { format } from 'date-fns';

const appointmentSchema = z.object({
  specialtyId: z.number().min(1, 'Selecione uma especialidade'),
  dentistId: z.number().min(1, 'Selecione um dentista'),
  appointmentDateTime: z.date().min(new Date(), 'Data deve estar no futuro'),
  notes: z.string().optional(),
});

type AppointmentFormData = z.infer<typeof appointmentSchema>;

const NewAppointmentPage: React.FC = () => {
  const navigate = useNavigate();
  const { showSuccess, showError } = useNotification();
  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);
  const [activeStep, setActiveStep] = useState(0);
  const [specialties, setSpecialties] = useState<Specialty[]>([]);
  const [dentists, setDentists] = useState<Dentist[]>([]);
  const [filteredDentists, setFilteredDentists] = useState<Dentist[]>([]);

  const {
    control,
    handleSubmit,
    watch,
    formState: { errors },
  } = useForm<AppointmentFormData>({
    resolver: zodResolver(appointmentSchema),
    defaultValues: {
      specialtyId: 0,
      dentistId: 0,
      appointmentDateTime: new Date(),
      notes: '',
    },
  });

  const selectedSpecialtyId = watch('specialtyId');

  useEffect(() => {
    loadData();
  }, []);

  useEffect(() => {
    if (selectedSpecialtyId > 0) {
      const filtered = dentists.filter((d) =>
        d.specialties.some((s) => s.id === selectedSpecialtyId)
      );
      setFilteredDentists(filtered);
    } else {
      setFilteredDentists([]);
    }
  }, [selectedSpecialtyId, dentists]);

  const loadData = async () => {
    try {
      const [specialtiesData, dentistsData] = await Promise.all([
        publicService.listSpecialties(),
        publicService.listDentists(),
      ]);
      setSpecialties(specialtiesData);
      setDentists(dentistsData);
    } catch (error: any) {
      showError('Erro ao carregar dados');
    } finally {
      setLoading(false);
    }
  };

  const onSubmit = async (data: AppointmentFormData) => {
    setSaving(true);
    try {
      await patientService.createAppointment({
        specialtyId: data.specialtyId,
        dentistId: data.dentistId,
        appointmentDateTime: data.appointmentDateTime.toISOString(),
        notes: data.notes,
      });
      showSuccess('Consulta agendada com sucesso!');
      navigate('/patient/dashboard');
    } catch (error: any) {
      showError(
        error.response?.data?.message || 'Erro ao agendar consulta'
      );
    } finally {
      setSaving(false);
    }
  };

  const steps = ['Especialidade', 'Dentista', 'Data e Hora'];

  if (loading) {
    return (
      <Layout>
        <Loading />
      </Layout>
    );
  }

  return (
    <Layout>
      <Container maxWidth="md">
        <BannerHeader />

        <Typography variant="h4" gutterBottom>
          Novo Agendamento
        </Typography>

        <Stepper activeStep={activeStep} sx={{ mb: 4 }}>
          {steps.map((label) => (
            <Step key={label}>
              <StepLabel>{label}</StepLabel>
            </Step>
          ))}
        </Stepper>

        <Paper elevation={3} sx={{ p: 4 }}>
          <Box component="form" onSubmit={handleSubmit(onSubmit)}>
            <Grid container spacing={3}>
              <Grid item xs={12}>
                <Controller
                  name="specialtyId"
                  control={control}
                  render={({ field }) => (
                    <FormControl fullWidth error={!!errors.specialtyId}>
                      <InputLabel>Especialidade</InputLabel>
                      <Select {...field} label="Especialidade">
                        <MenuItem value={0}>Selecione...</MenuItem>
                        {specialties.map((specialty) => (
                          <MenuItem key={specialty.id} value={specialty.id}>
                            {specialty.name}
                          </MenuItem>
                        ))}
                      </Select>
                      {errors.specialtyId && (
                        <Typography variant="caption" color="error">
                          {errors.specialtyId.message}
                        </Typography>
                      )}
                    </FormControl>
                  )}
                />
              </Grid>

              <Grid item xs={12}>
                <Controller
                  name="dentistId"
                  control={control}
                  render={({ field }) => (
                    <FormControl
                      fullWidth
                      error={!!errors.dentistId}
                      disabled={!selectedSpecialtyId}
                    >
                      <InputLabel>Dentista</InputLabel>
                      <Select {...field} label="Dentista">
                        <MenuItem value={0}>Selecione...</MenuItem>
                        {filteredDentists.map((dentist) => (
                          <MenuItem key={dentist.id} value={dentist.id}>
                            {dentist.fullName} - CRO: {dentist.cro}
                          </MenuItem>
                        ))}
                      </Select>
                      {errors.dentistId && (
                        <Typography variant="caption" color="error">
                          {errors.dentistId.message}
                        </Typography>
                      )}
                    </FormControl>
                  )}
                />
              </Grid>

              <Grid item xs={12}>
                <Controller
                  name="appointmentDateTime"
                  control={control}
                  render={({ field }) => (
                    <LocalizationProvider
                      dateAdapter={AdapterDateFns}
                      adapterLocale={ptBR}
                    >
                      <DateTimePicker
                        label="Data e Hora"
                        {...field}
                        slotProps={{
                          textField: {
                            fullWidth: true,
                            error: !!errors.appointmentDateTime,
                            helperText: errors.appointmentDateTime?.message,
                          },
                        }}
                      />
                    </LocalizationProvider>
                  )}
                />
              </Grid>

              <Grid item xs={12}>
                <Controller
                  name="notes"
                  control={control}
                  render={({ field }) => (
                    <TextField
                      {...field}
                      label="Observações (opcional)"
                      multiline
                      rows={4}
                      fullWidth
                    />
                  )}
                />
              </Grid>

              <Grid item xs={12}>
                <Box sx={{ display: 'flex', gap: 2, justifyContent: 'flex-end' }}>
                  <Button
                    variant="outlined"
                    onClick={() => navigate('/patient/dashboard')}
                    disabled={saving}
                  >
                    Cancelar
                  </Button>
                  <Button
                    type="submit"
                    variant="contained"
                    disabled={saving}
                    size="large"
                  >
                    {saving ? 'Agendando...' : 'Agendar Consulta'}
                  </Button>
                </Box>
              </Grid>
            </Grid>
          </Box>
        </Paper>
      </Container>
    </Layout>
  );
};

export default NewAppointmentPage;
