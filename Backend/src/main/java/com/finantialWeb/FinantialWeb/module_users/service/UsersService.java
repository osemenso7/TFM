package com.finantialWeb.FinantialWeb.module_users.service;

import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.finantialWeb.FinantialWeb.module_users.controller.model.*;
import com.finantialWeb.FinantialWeb.module_users.data.UserJpaRepository;
import com.finantialWeb.FinantialWeb.module_users.data.model.User;
import com.finantialWeb.FinantialWeb.module_users.utils.exceptions.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class UsersService {

    private final UserJpaRepository useRepository;
    private final PasswordEncoder passwordEncoder;

    public Optional<User> findByUsernameOrMail(String login) {
        return useRepository.findByUsernameOrMail(login, login);
    }

    public User findById(Long userId) {
        return useRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(Exceptions.USER_NOT_FOUND + userId));
    }

    public Long getTokenVersion(Long userId) {
        User user = findById(userId);

        return user.getTokenVersion();
    }

    public UserSecurityData findSecurityDataById(Long userId) {
        User user = findById(userId);

        return new UserSecurityData(user.getUsername(), user.getTokenVersion());
    }

    public UserDto register(RegisterUserDto registerDto) {
        if (useRepository.existsByUsername(registerDto.getUsername()) || (useRepository.existsByMail(registerDto.getMail()))) {
            throw new UserAlreadyExistsException(Exceptions.USER_ALREADY_EXISTS);
        }

        User user = User.builder()
            .username(registerDto.getUsername())
            .password(passwordEncoder.encode(registerDto.getPassword()))
            .mail(registerDto.getMail())
            .name(registerDto.getName())
            .surname(registerDto.getSurname())
            .birthday(registerDto.getBirthday())
            .build();

        User savedUser = useRepository.save(user);

        return UserDto.from(savedUser);
    }

    public UserDto login(String login, String password) {
        User user = findByUsernameOrMail(login).orElseThrow(() -> new InvalidCredentialsException(Exceptions.USER_INVALID_CREDENTIALS, HttpStatus.UNAUTHORIZED));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new InvalidCredentialsException(Exceptions.USER_INVALID_CREDENTIALS, HttpStatus.BAD_REQUEST);
        }

        return UserDto.from(user);
    }

    public UserDto update(String username, UpdateUserDto updateUserDto) {
        User user = findByUsernameOrMail(username).orElseThrow(() -> new UserNotFoundException(Exceptions.USER_NOT_FOUND));

        if ((!user.getUsername().equals(updateUserDto.getUsername()) && useRepository.existsByUsername(updateUserDto.getUsername()))
                || (!user.getMail().equals(updateUserDto.getMail()) && useRepository.existsByMail(updateUserDto.getMail()))) {
            throw new UserAlreadyExistsException(Exceptions.USER_ALREADY_EXISTS);
        }

        user.update(updateUserDto);
        User updatedUser = useRepository.save(user);

        return UserDto.from(updatedUser);
    }

    public void delete(String username) {
        User user = findByUsernameOrMail(username).orElseThrow(() -> new UserNotFoundException(Exceptions.USER_NOT_FOUND));

        useRepository.delete(user);
    }

    public void changePassword(String username, ChangePasswordDto changePwdDto) {
        User user = findByUsernameOrMail(username).orElseThrow(() -> new UserNotFoundException(Exceptions.USER_NOT_FOUND));

        if (!passwordEncoder.matches(changePwdDto.getCurrentPassword(), user.getPassword())) {
            throw new InvalidPasswordException("Invalid password");
        }

        if (!changePwdDto.getNewPassword().equals(changePwdDto.getConfirmationPassword())) {
            throw new InvalidPasswordException("Passwords do not match");
        }

        if (passwordEncoder.matches(changePwdDto.getNewPassword(), user.getPassword())) {
            throw new InvalidPasswordException("Invalid password");
        }

        user.setPassword(passwordEncoder.encode(changePwdDto.getNewPassword()));
        user.setTokenVersion(user.getTokenVersion() + 1);

        useRepository.save(user);
    }

}
