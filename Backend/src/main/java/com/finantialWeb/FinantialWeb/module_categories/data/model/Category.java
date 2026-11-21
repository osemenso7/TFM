package com.finantialWeb.FinantialWeb.module_categories.data.model;

import com.finantialWeb.FinantialWeb.module_categories.utils.Consts;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "categories")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = Consts.NAME, unique = true)
    private String name;
}

