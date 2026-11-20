package com.finantialWeb.FinantialWeb.module_imports.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.finantialWeb.FinantialWeb.module_imports.data.model.Import;

public interface ImportJpaRepository extends JpaRepository<Import, Long>, JpaSpecificationExecutor<Import> {
}
