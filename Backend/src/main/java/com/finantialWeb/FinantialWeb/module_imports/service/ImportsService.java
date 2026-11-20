package com.finantialWeb.FinantialWeb.module_imports.service;

import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


import com.finantialWeb.FinantialWeb.module_imports.data.ImportJpaRepository;
import com.finantialWeb.FinantialWeb.module_imports.data.model.Import;
import com.finantialWeb.FinantialWeb.module_imports.data.model.Transaction;
import com.finantialWeb.FinantialWeb.module_imports.data.TransactionJpaRepository;
import com.finantialWeb.FinantialWeb.module_users.data.model.User;
import com.finantialWeb.FinantialWeb.module_users.service.UsersService;
import com.finantialWeb.FinantialWeb.module_users.utils.exceptions.UserNotFoundException;


@Service
@Slf4j
@RequiredArgsConstructor
public class ImportsService {

    private final ImportJpaRepository importRepository;
    private final TransactionJpaRepository transactionRepository;
    private final UsersService usersService;

    @Transactional(rollbackFor = Exception.class)
    public void importCsv(MultipartFile file) throws Exception {
        // Validate authenticated user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = usersService.findByUsernameOrMail(username).orElseThrow(() -> new UserNotFoundException("User not found"));

        // Parse and validate all transactions before persisting them to the database.
        List<Transaction> transactions = new ArrayList<>();

        try (
            InputStreamReader reader = new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8);
            CSVParser parser = CSVFormat.DEFAULT.builder()
                                                .setDelimiter(';')
                                                .get()
                                                .parse(reader)
            ) 
        {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            for (CSVRecord csvRecord : parser) {

                // Ignore metadata rows and empty rows
                if ((csvRecord.getRecordNumber() <= 3) || csvRecord.stream().allMatch(StringUtils::isBlank)) {
                    continue;
                }

                // Validate transaction columns
                if (csvRecord.size() < 3 || csvRecord.get(0).isBlank()) {
                    throw new IllegalArgumentException("Invalid transaction at line " + csvRecord.getRecordNumber());
                }

                String concept = csvRecord.get(0).trim();
                LocalDate date = LocalDate.parse(csvRecord.get(1).trim(), formatter);
                BigDecimal amount = new BigDecimal(csvRecord.get(2).trim());

                Transaction transaction = Transaction.builder()
                                                        .concept(concept)
                                                        .date(date)
                                                        .amount(amount)
                                                        .build();

                transactions.add(transaction);
            }
        }

        if (transactions.isEmpty()) {
            throw new IllegalArgumentException("The CSV file contains no valid transactions");
        }

        Import importFile = Import.builder()
                                    .filename(file.getOriginalFilename())
                                    .bank("CAIXABANK")
                                    .user(user)
                                    .importedAt(OffsetDateTime.now(ZoneOffset.UTC).truncatedTo(ChronoUnit.SECONDS))
                                    .build();

        Import savedImport = importRepository.save(importFile);

        // Associate generated import to each transaction and save them
        transactions.forEach(transaction ->transaction.setImportFile(savedImport));
        transactionRepository.saveAll(transactions);    
    }

}
