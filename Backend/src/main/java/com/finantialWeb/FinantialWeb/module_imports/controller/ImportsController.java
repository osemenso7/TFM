package com.finantialWeb.FinantialWeb.module_imports.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.finantialWeb.FinantialWeb.module_imports.service.ImportsService;

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
        @ApiResponse(responseCode = "204", description = "Archivo procesado correctamente"),
        @ApiResponse(responseCode = "400", description = "Archivo inválido"),
        @ApiResponse(responseCode = "401", description = "Usuario no autenticado")
    })
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> importCsv(@RequestParam("file") MultipartFile file) throws Exception {
        importsService.importCsv(file);

        return ResponseEntity.noContent().build();
    }
}
