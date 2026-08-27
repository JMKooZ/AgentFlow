package com.agentflow.auth.repository;

import com.agentflow.auth.entity.RefreshToken;
import com.agentflow.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByToken(String token);

    Optional<RefreshToken> findByUser(User user);

    List<RefreshToken> findAllByUser(User user);

    void deleteByUser(User user);
}
