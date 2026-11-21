package com.finantialWeb.FinantialWeb.module_categories.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.finantialWeb.FinantialWeb.module_categories.data.model.Category;

public interface CategoriesJpaRepository extends JpaRepository<Category, Long>, JpaSpecificationExecutor<Category> {
}
