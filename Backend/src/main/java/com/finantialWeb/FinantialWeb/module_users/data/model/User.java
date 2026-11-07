package com.finantialWeb.FinantialWeb.module_users.data.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.finantialWeb.FinantialWeb.module_users.controller.model.UpdateUserDto;
import com.finantialWeb.FinantialWeb.module_users.utils.Consts;

import jakarta.persistence.*;
import java.time.LocalDate;
import lombok.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = Consts.USERNAME, unique = true)
    private String username;

    @Column(name = Consts.PASSWORD)
    private String password;

    @Column(name = Consts.MAIL, unique = true)
    private String mail;

    @Column(name = Consts.NAME)
    private String name;

    @Column(name = Consts.SURNAME)
    private String surname;

    @Column(name = Consts.BIRTHDAY)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate birthday;

    @Column(name = Consts.TOKENVERSION, nullable = false)
    private long tokenVersion;

    public void update(UpdateUserDto updateUserDto) {
        this.username = updateUserDto.getUsername();
        this.mail = updateUserDto.getMail();
        this.name = updateUserDto.getName();
        this.surname = updateUserDto.getSurname();
        this.birthday = updateUserDto.getBirthday();
    }
}

