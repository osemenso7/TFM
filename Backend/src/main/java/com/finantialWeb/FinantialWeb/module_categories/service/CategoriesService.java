package com.finantialWeb.FinantialWeb.module_categories.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.finantialWeb.FinantialWeb.module_categories.data.CategoriesJpaRepository;
import com.finantialWeb.FinantialWeb.module_categories.data.UserCategoriesJpaRepository;
import com.finantialWeb.FinantialWeb.module_categories.data.model.Category;
import com.finantialWeb.FinantialWeb.module_categories.data.model.UserCategory;
import com.finantialWeb.FinantialWeb.module_categories.utils.exceptions.ExceptionsCategories;
import com.finantialWeb.FinantialWeb.module_categories.utils.exceptions.InvalidCategoryException;
import com.finantialWeb.FinantialWeb.module_users.data.model.User;
import com.finantialWeb.FinantialWeb.module_users.service.UsersService;
import com.finantialWeb.FinantialWeb.module_users.utils.exceptions.ExceptionsUsers;
import com.finantialWeb.FinantialWeb.module_users.utils.exceptions.UserNotFoundException;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class CategoriesService {

    private final CategoriesJpaRepository categoriesRepository;
    private final UserCategoriesJpaRepository userCategoriesRepository;
    private final UsersService usersService;

    public List<Category> getAllCategories() {
        return categoriesRepository.findAll();
    }

    public List<Long> getSelectedCategories() {
        Long userId = getAuthenticatedUser().getId();

        return userCategoriesRepository.findById_UserId(userId)
                                            .stream()
                                            .map(userCategory -> userCategory.getId().getCategoryId())
                                            .toList();
    }

    @Transactional 
    public void saveSelectedCategories(List<Long> categoryIds) {
        Long userId = getAuthenticatedUser().getId();
        List<UserCategory> previousCategories = userCategoriesRepository.findById_UserId(userId);

        if (categoryIds == null) {
            userCategoriesRepository.deleteAll(previousCategories);
            return;
        }

        if (categoryIds.contains(null)) {
            throw new InvalidCategoryException(ExceptionsCategories.INVALID_CATEGORY);
        }

        Set<Long> selectedCategoriesIds = new HashSet<>(categoryIds);
        List<Category> existingCategories = categoriesRepository.findAllById(selectedCategoriesIds);

        if (existingCategories.size() != selectedCategoriesIds.size()) {
            throw new InvalidCategoryException(ExceptionsCategories.CATEGORY_NOT_FOUND);
        }

        Set<Long> currentCategoryIds = previousCategories.stream()
                                                            .map(category -> category.getId().getCategoryId())
                                                            .collect(Collectors.toSet());

        List<UserCategory> categoriesToDelete = previousCategories.stream()
                                                                    .filter(category -> !selectedCategoriesIds.contains(category.getId().getCategoryId()))
                                                                    .toList();

        List<UserCategory> categoriesToAdd = selectedCategoriesIds.stream()
                                                            .filter(categoryId -> !currentCategoryIds.contains(categoryId))
                                                            .map(categoryId -> UserCategory.createUserCategory(userId, categoryId))
                                                            .toList();

        userCategoriesRepository.deleteAll(categoriesToDelete);
        userCategoriesRepository.saveAll(categoriesToAdd);
    }

    private User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        return usersService.findByUsernameOrMail(username).orElseThrow(() -> new UserNotFoundException(ExceptionsUsers.USER_NOT_FOUND));
    }
}