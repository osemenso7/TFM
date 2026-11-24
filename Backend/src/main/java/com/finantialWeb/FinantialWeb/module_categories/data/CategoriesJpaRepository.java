package com.finantialWeb.FinantialWeb.module_categories.data;

import com.finantialWeb.FinantialWeb.module_categories.data.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CategoriesJpaRepository extends JpaRepository<Category, Long>, JpaSpecificationExecutor<Category> {
}
