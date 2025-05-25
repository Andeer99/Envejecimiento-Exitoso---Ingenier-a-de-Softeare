package com.IngdeSoftware.EnvejecimientoExitoso.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.*;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class UploadController {

    // 1) Inyecta la ruta configurable
    @Value("${app.upload.dir}")
    private String uploadDir;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Map<String,String> upload(@RequestParam("file") MultipartFile file) throws IOException {
        // 2) Carpeta garantizada escribible
        Path dir = Paths.get(uploadDir).toAbsolutePath().normalize();
        Files.createDirectories(dir);

        // 3) Genera nombre aleatorio + extensión
        String ext = "";
        String original = Path.of(file.getOriginalFilename()).getFileName().toString();
        int dot = original.lastIndexOf('.');
        if (dot > -1) ext = original.substring(dot).toLowerCase();
        String filename = UUID.randomUUID() + ext;
        Path target = dir.resolve(filename);

        // 4) Copia el archivo
        Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);

        // 5) Devuelve la URL **relativa** que mapea StaticResourceConfig
        return Map.of("url", "/uploads/" + filename);
    }

    @GetMapping("/uploads/{file:.+}")
    public ResponseEntity<Resource> serve(@PathVariable String file) throws MalformedURLException {
        Path path = Paths.get(uploadDir).toAbsolutePath().normalize().resolve(file).normalize();
        Resource resource = new UrlResource(path.toUri());
        if (!resource.exists()) return ResponseEntity.notFound().build();
        MediaType media = MediaTypeFactory.getMediaType(resource)
                                          .orElse(MediaType.APPLICATION_OCTET_STREAM);
        return ResponseEntity.ok().contentType(media).body(resource);
    }
}
