package com.finantialWeb.FinantialWeb.module_categories.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.finantialWeb.FinantialWeb.module_categories.controller.model.SaveSelectedCategoriesDto;
import com.finantialWeb.FinantialWeb.module_categories.data.model.Category;
import com.finantialWeb.FinantialWeb.module_categories.service.CategoriesService;
import com.finantialWeb.FinantialWeb.module_categories.utils.exceptions.ExceptionsCategories;
import com.finantialWeb.FinantialWeb.module_users.utils.exceptions.ExceptionsUsers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

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
        @ApiResponse(
            responseCode = "200",
            description = "Categories retrieved successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Category[].class)
            )
        ),
        @ApiResponse(responseCode = "401", description = ExceptionsUsers.USER_NOT_AUTHENTICATED),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public ResponseEntity<List<Category>> getAllCategories() {
        return ResponseEntity.ok(categoriesService.getAllCategories());
    }

    @Operation(
        operationId = "getSelectedCategories",
        description = "Obtener categorías seleccionadas",
        summary = "Devuelve los IDs de las categorías seleccionadas por el usuario."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Selected categories retrieved successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(type = "array", implementation = Long.class)
            )
        ),
        @ApiResponse(responseCode = "401", description = ExceptionsUsers.USER_NOT_AUTHENTICATED),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/selected")
    public ResponseEntity<List<Long>> getSelectedCategories() {
        return ResponseEntity.ok(categoriesService.getSelectedCategories());
    }

    @Operation(
        operationId = "saveSelectedCategories",
        description = "Guardar categorías seleccionadas",
        summary = "Actualiza las categorías seleccionadas por el usuario."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "204",
            description = "Categories saved successfully",
            content = @Content
        ),
        @ApiResponse(responseCode = "400", description = ExceptionsCategories.INVALID_CATEGORY + " / " + ExceptionsCategories.CATEGORY_NOT_FOUND),
        @ApiResponse(responseCode = "401", description = ExceptionsUsers.USER_NOT_AUTHENTICATED),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping
    public ResponseEntity<Void> saveSelectedCategories(@RequestBody SaveSelectedCategoriesDto saveSelectedCategoriesDto) {
        categoriesService.saveSelectedCategories(saveSelectedCategoriesDto.getCategoryIds());

        return ResponseEntity.noContent().build();
    }
}
