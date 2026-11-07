package com.finantialWeb.FinantialWeb.module_users.controller.model;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.finantialWeb.FinantialWeb.module_users.data.model.User;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
	private Long id;
	private String username;
	private String mail;
	private String name;
	private String surname;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
	private LocalDate birthday;

	public static UserDto from(User user) {
		return UserDto.builder()
				.id(user.getId())
				.username(user.getUsername())
				.mail(user.getMail())
				.name(user.getName())
				.surname(user.getSurname())
				.birthday(user.getBirthday())
				.build();
	}
}
