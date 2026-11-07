package com.finantialWeb.FinantialWeb.module_users.controller;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.finantialWeb.FinantialWeb.module_users.controller.model.*;
import com.finantialWeb.FinantialWeb.module_users.service.UsersService;
import com.finantialWeb.FinantialWeb.module_users.utils.exceptions.*;
import com.finantialWeb.FinantialWeb.security.JwtService;

@RestController
@RequiredArgsConstructor
@Tag(name = "Users Controller", description = "Microservicio encargado de gestionar los usuarios.")
@RequestMapping("/users")
public class UsersController {

    private final UsersService userService;
    private final JwtService jwtService;

    @Operation(
        operationId = "registerUser",
        description = "Registrar un nuevo usuario",
        summary = "Se registra un nuevo usuario en el sistema.")
    @ApiResponses(value = {
        @ApiResponse(
                responseCode = "201",
                description = "Usuario registrado correctamente",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = UserDto.class)
                )
        ),
        @ApiResponse(responseCode = "400", description = Exceptions.USER_INVALID_CREDENTIALS),
        @ApiResponse(responseCode = "409", description = Exceptions.USER_ALREADY_EXISTS)
    })
    @PostMapping
    public ResponseEntity<UserDto> register(@Valid @RequestBody RegisterUserDto registerUserDto) {
        UserDto createdUser = userService.register(registerUserDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @Operation(
        operationId = "loginUser",
        summary = "Login de usuario",
        description = "Autentica a un usuario mediante su nombre de usuario o correo electrónico y contraseña")
    @ApiResponses(value = {
        @ApiResponse(
                responseCode = "200",
                description = "Login correcto",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = LoginUserResponseDto.class)
                )
        ),
        @ApiResponse(responseCode = "400", description = Exceptions.USER_INVALID_CREDENTIALS),
        @ApiResponse(responseCode = "401", description = Exceptions.USER_INVALID_CREDENTIALS)
    })
    @PostMapping("/login")
    public ResponseEntity<LoginUserResponseDto> login(@Valid @RequestBody LoginUserDto loginUserDto) {
        UserDto userDto = userService.login(loginUserDto.getLogin(), loginUserDto.getPassword());
        Long tokenVersion = userService.getTokenVersion(userDto.getId());
        String token = jwtService.generateToken(userDto.getId(), tokenVersion);

        LoginUserResponseDto response = new LoginUserResponseDto(userDto, token);

        return ResponseEntity.ok(response);
    }

    @Operation(
        operationId = "getCurrentUser",
        summary = "Consultar usuario autenticado",
        description = "Obtiene los datos del usuario identificado mediante su token JWT")
    @ApiResponses(value = {
        @ApiResponse(
                responseCode = "200",
                description = "Usuario obtenido correctamente",
                content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = UserDto.class)
                )
        ),
        @ApiResponse(responseCode = "401", description = Exceptions.USER_NOT_AUTHENTICATED),
        @ApiResponse(responseCode = "404", description = Exceptions.USER_NOT_FOUND)
    })
    @GetMapping("/me")
    public ResponseEntity<UserDto> getCurrentUser(Authentication authentication) {
        String username = authentication.getName();
        UserDto userDto = UserDto.from(userService.findByUsernameOrMail(username).orElseThrow(() -> new UserNotFoundException("User not found")));

        return ResponseEntity.ok(userDto);
    }

     @Operation(
        operationId = "updateUser",
        summary = "Update de usuario",
        description = "Modifica los datos de un usuario")
     @ApiResponses(value = {
        @ApiResponse(
                responseCode = "200",
                description = "Usuario actualizado correctamente",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = UserDto.class)
                )
        ),
        @ApiResponse(responseCode = "400", description = Exceptions.USER_INVALID_CREDENTIALS),
        @ApiResponse(responseCode = "401", description = Exceptions.USER_NOT_AUTHENTICATED),
        @ApiResponse(responseCode = "404", description = Exceptions.USER_NOT_FOUND),
        @ApiResponse(responseCode = "409", description = Exceptions.USER_ALREADY_EXISTS)
     })
     @PutMapping("/me")
     public ResponseEntity<UserDto> update(Authentication authentication, @Valid @RequestBody UpdateUserDto updateUserDto) {
        String username = authentication.getName();
        UserDto updatedUser = userService.update(username, updateUserDto);

        return ResponseEntity.ok(updatedUser);
     }

     @Operation(
        operationId = "deleteUser",
        summary = "Eliminar usuario",
        description = "Elimina un usuario de la base de datos")
     @ApiResponses(value = {
        @ApiResponse(
                responseCode = "204",
                description = "Usuario eliminado correctamente",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = UserDto.class)
                )
        ),
        @ApiResponse(responseCode = "401", description = Exceptions.USER_NOT_AUTHENTICATED),
        @ApiResponse(responseCode = "404", description = Exceptions.USER_NOT_FOUND)
     })
     @DeleteMapping("/me")
     public ResponseEntity<UserDto> delete(Authentication authentication) {
        String username = authentication.getName();
        userService.delete(username);

        return ResponseEntity.noContent().build();
     }

     @Operation(
        operationId = "deleteUser",
        summary = "Eliminar usuario",
        description = "Elimina un usuario de la base de datos")
     @ApiResponses(value = {
        @ApiResponse(
                responseCode = "204",
                description = "Usuario eliminado correctamente",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = UserDto.class)
                )
        ),
        @ApiResponse(responseCode = "401", description = Exceptions.USER_NOT_AUTHENTICATED),
        @ApiResponse(responseCode = "404", description = Exceptions.USER_NOT_FOUND)
     })
     @PatchMapping("/me/pwd")
     public ResponseEntity<UserDto> patch(Authentication authentication, @Valid @RequestBody ChangePasswordDto changePwdDto) {
        String username = authentication.getName();
        userService.changePassword(username, changePwdDto);

        return ResponseEntity.noContent().build();
     }
}
