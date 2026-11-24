package com.finantialWeb.FinantialWeb.module_categories.controller.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SaveSelectedCategoriesDto {

    private List<Long> categoryIds;
}
