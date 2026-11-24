package com.finantialWeb.FinantialWeb.module_categories.data;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.finantialWeb.FinantialWeb.module_categories.data.model.UserCategory;
import com.finantialWeb.FinantialWeb.module_categories.data.model.UserCategoryId;

public interface UserCategoriesJpaRepository extends JpaRepository<UserCategory, UserCategoryId>, JpaSpecificationExecutor<UserCategory> {

    List<UserCategory> findById_UserId(Long userId);
}
