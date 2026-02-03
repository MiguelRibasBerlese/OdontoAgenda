package com.odontoagenda.controller;

import com.odontoagenda.service.BannerService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * Controller para gerenciamento do banner institucional da clínica.
 */
@RestController
@RequiredArgsConstructor
public class BannerController {

    private final BannerService bannerService;

    /**
     * Endpoint público para obter o banner atual.
     */
    @GetMapping("/api/public/clinic/banner")
    public ResponseEntity<Resource> getBanner() {
        Resource banner = bannerService.loadBanner();
        
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"banner.jpg\"")
                .body(banner);
    }

    /**
     * Endpoint administrativo para atualizar o banner (apenas ADMIN).
     */
    @PutMapping("/api/admin/clinic/banner")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> updateBanner(@RequestParam("file") MultipartFile file) {
        bannerService.updateBanner(file);
        return ResponseEntity.ok("Banner atualizado com sucesso");
    }
}
