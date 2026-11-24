package com.finantialWeb.FinantialWeb.module_imports.controller;

import java.io.IOException;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.finantialWeb.FinantialWeb.module_imports.service.ImportsService;
import com.finantialWeb.FinantialWeb.module_imports.utils.exceptions.ExceptionsImports;
import com.finantialWeb.FinantialWeb.module_users.utils.exceptions.ExceptionsUsers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@Tag(name = "Imports Controller", description = "Microservicio encargado de gestionar los archivos importados.")
@RequestMapping("/imports")
public class ImportsController {

    private final ImportsService importsService;

    @Operation(
        operationId = "importsCSv",
        description = "Importar movimientos bancarios",
        summary = "Recibe un archivo CSV de CaixaBank y procesa sus movimientos.")
    @ApiResponses(value = {
         @ApiResponse(
            responseCode = "204",
            description = "File processed successfully",
            content = @Content
        ),
        @ApiResponse(responseCode = "400", description = ExceptionsImports.INVALID_FILE),
        @ApiResponse(responseCode = "401", description = ExceptionsUsers.USER_NOT_AUTHENTICATED),
        @ApiResponse(responseCode = "500",description = "Internal server error")
    })
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> importCsv(@RequestParam("file") MultipartFile file) throws IOException {
        importsService.importCsv(file);

        return ResponseEntity.noContent().build();
    }
}
