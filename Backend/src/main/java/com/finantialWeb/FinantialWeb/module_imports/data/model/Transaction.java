package com.finantialWeb.FinantialWeb.module_imports.data.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.finantialWeb.FinantialWeb.module_imports.utils.Consts;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "transactions")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = Consts.CONCEPT, nullable = false)
    private String concept;

    @Column(name = Consts.DATE, nullable = false)
    private LocalDate date;

    @Column(name = Consts.AMOUNT, nullable = false, precision = 12, scale = 2)
    private BigDecimal amount;

    @JoinColumn(name = Consts.IMPORTID, nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Import importFile;

    public static Transaction createTransaction(String concept, LocalDate date, BigDecimal amount) {

        return Transaction.builder()
                .concept(concept)
                .date(date)
                .amount(amount)
                .build();
    }
}
