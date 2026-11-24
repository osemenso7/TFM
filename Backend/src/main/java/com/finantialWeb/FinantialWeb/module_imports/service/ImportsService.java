package com.finantialWeb.FinantialWeb.module_imports.service;

import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.finantialWeb.FinantialWeb.module_imports.data.ImportJpaRepository;
import com.finantialWeb.FinantialWeb.module_imports.data.TransactionJpaRepository;
import com.finantialWeb.FinantialWeb.module_imports.data.model.Import;
import com.finantialWeb.FinantialWeb.module_imports.data.model.Transaction;
import com.finantialWeb.FinantialWeb.module_imports.utils.exceptions.ExceptionsImports;
import com.finantialWeb.FinantialWeb.module_imports.utils.exceptions.InvalidImportException;
import com.finantialWeb.FinantialWeb.module_users.data.model.User;
import com.finantialWeb.FinantialWeb.module_users.service.UsersService;
import com.finantialWeb.FinantialWeb.module_users.utils.exceptions.ExceptionsUsers;
import com.finantialWeb.FinantialWeb.module_users.utils.exceptions.UserNotFoundException;

import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class ImportsService {

    private final ImportJpaRepository importRepository;
    private final TransactionJpaRepository transactionRepository;
    private final UsersService usersService;

    @Transactional(rollbackFor = Exception.class)
    public void importCsv(MultipartFile file) throws IOException {
        validateFile(file);
        User user = getAuthenticatedUser();

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

                validateTransaction(csvRecord);

                // Ignore metadata rows
                if ((csvRecord.getRecordNumber() < 4) || (csvRecord.stream().allMatch(StringUtils::isBlank))) {
                    continue;
                }

                String concept = csvRecord.get(0).trim();
                LocalDate date;
                BigDecimal amount;

                try {
                    date = LocalDate.parse(csvRecord.get(1).trim(), formatter);
                    amount = new BigDecimal(csvRecord.get(2).trim());
                } catch (DateTimeParseException | NumberFormatException ex) {
                    throw new InvalidImportException(ExceptionsImports.INVALID_VALUE);
                }

                Transaction transaction = Transaction.createTransaction(concept, date, amount);
                transactions.add(transaction);
            }
        }

        if (transactions.isEmpty()) {
            throw new InvalidImportException(ExceptionsImports.EMPTY_FILE);
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

    private void validateFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new InvalidImportException(ExceptionsImports.EMPTY_FILE);
        }

        if ((file.getOriginalFilename() == null) || !file.getOriginalFilename().toLowerCase(Locale.ROOT).endsWith(".csv")) {
            throw new InvalidImportException(ExceptionsImports.INVALID_FILE);
        }
    }

    private void validateTransaction(CSVRecord csvRecord) {
        long csvRow = csvRecord.getRecordNumber();

        if (csvRecord.size() < 3) {
            throw new InvalidImportException(ExceptionsImports.INVALID_FILE);
        }

        if ((csvRow == 3) 
                && (!csvRecord.get(0).trim().equalsIgnoreCase("Concepto")
                    || !csvRecord.get(1).trim().equalsIgnoreCase("Fecha")
                    || !csvRecord.get(2).trim().equalsIgnoreCase("Importe"))) {

            throw new InvalidImportException(ExceptionsImports.INVALID_HEADERS);
        }
    }

    private User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        
        return usersService.findByUsernameOrMail(username).orElseThrow(() -> new UserNotFoundException(ExceptionsUsers.USER_NOT_FOUND));
    }
}
