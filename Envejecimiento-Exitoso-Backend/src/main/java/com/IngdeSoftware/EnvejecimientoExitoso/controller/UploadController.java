package com.IngdeSoftware.EnvejecimientoExitoso.controller;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.*;
import java.util.Map;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api")
public class UploadController {

    /** Se inyecta desde application.properties */
    @Value("${app.upload.dir}")
    private String uploadDir;

    @PostConstruct
    public void init() {
        log.info("▶ UploadController iniciado. uploadDir={}", uploadDir);
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> upload(@RequestParam("file") MultipartFile file) {
        log.info("→ Recibido POST /api/upload, fichero original='{}'", file.getOriginalFilename());

        try {
            // 1) Validaciones básicas
            if (file.isEmpty()) {
                log.warn("El fichero está vacío");
                return ResponseEntity.badRequest()
                        .body(Map.of("message", "No se recibió un fichero válido"));
            }

            // 2) Crea la carpeta (debe apuntar a /tmp/uploads en Render)
            Path dir = Paths.get(uploadDir).toAbsolutePath().normalize();
            log.info("Intentando crear directorio: {}", dir);
            Files.createDirectories(dir);

            // 3) Nombre seguro
            String original = Path.of(file.getOriginalFilename())
                    .getFileName().toString();
            String ext = "";
            int dot = original.lastIndexOf('.');
            if (dot > -1) ext = original.substring(dot).toLowerCase();
            String filename = UUID.randomUUID() + ext;
            Path target = dir.resolve(filename);
            log.info("Guardando fichero como: {}", target);

            // 4) Copia
            Files.copy(file.getInputStream(),
                    target,
                    StandardCopyOption.REPLACE_EXISTING);

            // 5) Construye la URL relativa
            String url = "/uploads/" + filename;
            log.info("Imagen subida correctamente: {}", url);

            return ResponseEntity.ok(Map.of("url", url));

        } catch (IOException ex) {
            log.error("❌ Error de E/S al subir fichero", ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Error de E/S: " + ex.getMessage()));
        } catch (Exception ex) {
            log.error("❌ Error inesperado al subir fichero", ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Error inesperado: " + ex.getMessage()));
        }
    }

    @GetMapping("/uploads/{file:.+}")
    public ResponseEntity<Resource> serve(@PathVariable String file) {
        log.info("→ GET /uploads/{}", file);
        try {
            Path path = Paths.get(uploadDir).toAbsolutePath().normalize()
                    .resolve(file).normalize();
            Resource resource = new UrlResource(path.toUri());
            if (!resource.exists()) {
                log.warn("Fichero no encontrado: {}", path);
                return ResponseEntity.notFound().build();
            }
            MediaType media = MediaTypeFactory
                    .getMediaType(resource)
                    .orElse(MediaType.APPLICATION_OCTET_STREAM);
            return ResponseEntity.ok().contentType(media).body(resource);
        } catch (MalformedURLException e) {
            log.error("URL mal formada al servir fichero", e);
            return ResponseEntity.badRequest().build();
        }
    }
}
