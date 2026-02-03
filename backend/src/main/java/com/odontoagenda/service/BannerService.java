package com.odontoagenda.service;

import com.odontoagenda.exception.BusinessException;
import com.odontoagenda.model.entity.ClinicSettings;
import com.odontoagenda.repository.ClinicSettingsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * Serviço responsável pelo gerenciamento do banner institucional da clínica.
 * Implementa validação, upload e recuperação do banner.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class BannerService {

    private static final String BANNER_SETTING_KEY = "CLINIC_BANNER";
    private static final List<String> ALLOWED_EXTENSIONS = Arrays.asList("jpg", "jpeg", "png", "webp");
    private static final long MAX_FILE_SIZE = 2 * 1024 * 1024; // 2MB

    private final ClinicSettingsRepository clinicSettingsRepository;

    @Value("${upload.dir}")
    private String uploadDir;

    /**
     * Obtém a URL do banner atual ou retorna o banner padrão.
     */
    @Transactional(readOnly = true)
    public String getBannerUrl() {
        return clinicSettingsRepository.findBySettingKey(BANNER_SETTING_KEY)
                .map(ClinicSettings::getSettingValue)
                .orElse("/images/default-banner.jpg");
    }

    /**
     * Carrega o arquivo do banner como Resource.
     */
    @Transactional(readOnly = true)
    public Resource loadBanner() {
        try {
            String bannerUrl = getBannerUrl();
            
            // Se for o banner padrão, retorna do classpath
            if (bannerUrl.startsWith("/images/")) {
                return new UrlResource(getClass().getResource(bannerUrl));
            }
            
            // Se for um banner customizado, retorna do sistema de arquivos
            String filename = bannerUrl.substring(bannerUrl.lastIndexOf("/") + 1);
            Path filePath = Paths.get(uploadDir, filename);
            
            if (!Files.exists(filePath)) {
                // Se não existir, retorna banner padrão
                return new UrlResource(getClass().getResource("/images/default-banner.jpg"));
            }
            
            return new UrlResource(filePath.toUri());
        } catch (Exception e) {
            log.error("Erro ao carregar banner", e);
            throw new BusinessException("Erro ao carregar banner");
        }
    }

    /**
     * Atualiza o banner (alias para uploadBanner para manter compatibilidade).
     */
    @Transactional
    public void updateBanner(MultipartFile file) {
        uploadBanner(file);
    }

    /**
     * Faz upload de um novo banner.
     * Valida tipo de arquivo, tamanho e substitui o banner anterior.
     */
    @Transactional
    public String uploadBanner(MultipartFile file) {
        // Valida se o arquivo não está vazio
        if (file.isEmpty()) {
            throw new BusinessException("Arquivo vazio");
        }

        // Valida tamanho do arquivo
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new BusinessException("Arquivo muito grande. Tamanho máximo: 2MB");
        }

        // Valida extensão do arquivo
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || !hasValidExtension(originalFilename)) {
            throw new BusinessException("Formato de arquivo inválido. Use JPG, PNG ou WEBP");
        }

        try {
            // Cria o diretório se não existir
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // Gera nome único para o arquivo
            String extension = getFileExtension(originalFilename);
            String filename = "banner-" + UUID.randomUUID() + "." + extension;
            Path filePath = uploadPath.resolve(filename);

            // Salva o arquivo
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            // Atualiza ou cria a configuração
            String bannerUrl = "/" + uploadDir + "/" + filename;
            ClinicSettings settings = clinicSettingsRepository.findBySettingKey(BANNER_SETTING_KEY)
                    .orElse(ClinicSettings.builder()
                            .settingKey(BANNER_SETTING_KEY)
                            .build());

            // Remove banner antigo se existir
            if (settings.getSettingValue() != null && !settings.getSettingValue().equals("/images/default-banner.jpg")) {
                deleteOldBanner(settings.getSettingValue());
            }

            settings.setSettingValue(bannerUrl);
            clinicSettingsRepository.save(settings);

            log.info("Banner atualizado com sucesso: {}", bannerUrl);
            return bannerUrl;

        } catch (IOException e) {
            log.error("Erro ao fazer upload do banner", e);
            throw new BusinessException("Erro ao fazer upload do arquivo");
        }
    }

    /**
     * Remove o banner antigo do sistema de arquivos.
     */
    private void deleteOldBanner(String bannerUrl) {
        try {
            String filename = bannerUrl.substring(bannerUrl.lastIndexOf("/") + 1);
            Path oldFilePath = Paths.get(uploadDir, filename);
            if (Files.exists(oldFilePath)) {
                Files.delete(oldFilePath);
                log.info("Banner antigo removido: {}", filename);
            }
        } catch (IOException e) {
            log.warn("Não foi possível remover o banner antigo", e);
        }
    }

    /**
     * Verifica se o arquivo tem uma extensão válida.
     */
    private boolean hasValidExtension(String filename) {
        String extension = getFileExtension(filename);
        return ALLOWED_EXTENSIONS.contains(extension.toLowerCase());
    }

    /**
     * Extrai a extensão do arquivo.
     */
    private String getFileExtension(String filename) {
        int lastDotIndex = filename.lastIndexOf(".");
        if (lastDotIndex == -1) {
            return "";
        }
        return filename.substring(lastDotIndex + 1);
    }
}
