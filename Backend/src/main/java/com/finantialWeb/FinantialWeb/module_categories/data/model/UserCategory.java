package com.finantialWeb.FinantialWeb.module_categories.data.model;

import com.finantialWeb.FinantialWeb.module_categories.utils.Consts;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user_categories")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserCategory {

    @EmbeddedId
    @AttributeOverride(
        name = "userId",
        column = @Column(name = Consts.USER_ID)
    )
    @AttributeOverride(
        name = "categoryId",
        column = @Column(name = Consts.CATEGORY_ID)
    )
    private UserCategoryId id;

    public static UserCategory createUserCategory(Long userId, Long categoryId) {
        return new UserCategory(new UserCategoryId(userId, categoryId));
    }
}
