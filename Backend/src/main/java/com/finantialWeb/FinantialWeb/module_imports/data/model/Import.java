package com.finantialWeb.FinantialWeb.module_imports.data.model;

import java.time.OffsetDateTime;

import com.finantialWeb.FinantialWeb.module_imports.utils.Consts;
import com.finantialWeb.FinantialWeb.module_users.data.model.User;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "imports")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Import {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = Consts.FILENAME, nullable = false)
    private String filename;

    @Column(name = Consts.BANK, nullable = false)
    private String bank;

    @JoinColumn(name = Consts.USERID, nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @Column(name = Consts.IMPORTEDAT, nullable = false)
    private OffsetDateTime importedAt;
}
