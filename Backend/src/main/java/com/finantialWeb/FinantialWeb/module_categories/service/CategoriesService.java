package com.finantialWeb.FinantialWeb.module_categories.service;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.finantialWeb.FinantialWeb.module_categories.data.CategoriesJpaRepository;
import com.finantialWeb.FinantialWeb.module_categories.data.model.Category;

@Service
@Slf4j
@RequiredArgsConstructor
public class CategoriesService {

    private final CategoriesJpaRepository categoriesRepository;

    public List<Category> getAllCategories() {
        return categoriesRepository.findAll();
    }
}