package com.gabsdev.findaseat.repository;

import com.gabsdev.findaseat.model.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepository extends JpaRepository<Token, Long> {
    boolean existsByTokenString(String token);
}
