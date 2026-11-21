package com.finantialWeb.FinantialWeb.module_categories.controller;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.finantialWeb.FinantialWeb.module_categories.data.model.Category;
import com.finantialWeb.FinantialWeb.module_categories.service.CategoriesService;

@RestController
@RequiredArgsConstructor
@Tag(name = "Categories Controller", description = "Microservicio encargado de gestionar las categorías.")
@RequestMapping("/categories")
public class CategoriesController {

    private final CategoriesService categoriesService;

    @Operation(
        operationId = "getAllCategories",
        description = "Obtener todas las categorías disponibles",
        summary = "Devuelve las categorías almacenadas en la base de datos."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Categorías obtenidas correctamente"),
        @ApiResponse(responseCode = "401", description = "Usuario no autenticado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping
    public ResponseEntity<List<Category>> getAllCategories() {
        return ResponseEntity.ok(categoriesService.getAllCategories());
    }
}
