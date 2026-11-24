package com.finantialWeb.FinantialWeb.module_categories.data.model;

import java.io.Serializable;

import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Builder
public class UserCategoryId implements Serializable {
    
    private Long userId;
    private Long categoryId;
}
