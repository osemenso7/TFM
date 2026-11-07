package com.finantialWeb.FinantialWeb.module_users.data;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.finantialWeb.FinantialWeb.module_users.data.model.User;


public interface UserJpaRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    
    Optional<User> findByUsernameOrMail(String username, String mail);

    boolean existsByUsername(String username);

    boolean existsByMail(String mail);
}
