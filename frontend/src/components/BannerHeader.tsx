import React from 'react';
import { Box, Paper } from '@mui/material';
import { publicService } from '../services/publicService';

/**
 * Componente BannerHeader exibe o banner institucional da clínica.
 * Proporção fixa 3:1 para manter consistência visual.
 */
const BannerHeader: React.FC = () => {
  const bannerUrl = publicService.getBannerUrl();

  return (
    <Paper
      elevation={3}
      sx={{
        width: '100%',
        position: 'relative',
        paddingTop: '33.33%', // Proporção 3:1
        overflow: 'hidden',
        borderRadius: 2,
        mb: 3,
      }}
    >
      <Box
        component="img"
        src={bannerUrl}
        alt="Banner da Clínica"
        sx={{
          position: 'absolute',
          top: 0,
          left: 0,
          width: '100%',
          height: '100%',
          objectFit: 'cover',
        }}
        onError={(e) => {
          // Fallback caso a imagem não carregue
          const target = e.target as HTMLImageElement;
          target.src = '/default-banner.jpg';
        }}
      />
    </Paper>
  );
};

export default BannerHeader;
