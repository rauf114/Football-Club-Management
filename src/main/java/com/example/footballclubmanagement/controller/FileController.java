package com.example.footballclubmanagement.controller;

import com.example.footballclubmanagement.dto.response.ApiResponse;
import com.example.footballclubmanagement.service.FileStorageService;
import com.example.footballclubmanagement.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/files")
@RequiredArgsConstructor
@Tag(name = "File Controller", description = "Endpoints for uploading and downloading files")
public class FileController {

    private final FileStorageService fileStorageService;

    private final NotificationService notificationService;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<String>> uploadFile(@RequestParam("file") MultipartFile file) {
        String fileName = fileStorageService.storeFile(file);

        notificationService.sendFileUploadNotification(fileName, "admin@footballclub.com");

        return new ResponseEntity<>(
                ApiResponse.success(fileName, "File uploaded successfully", HttpStatus.CREATED),
                HttpStatus.CREATED
        );
    }

    @GetMapping("/download/{fileName:.+}")
    @Operation(summary = "Download a file", description = "Downloads a previously uploaded file by name.")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request) {
        Resource resource = fileStorageService.loadFileAsResource(fileName);

        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
        }

        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
}