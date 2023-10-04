package com.ecommerce.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ecommerce.user.entity.UserSession;

import jakarta.transaction.Transactional;
@Repository
public interface UserSessionRepository extends JpaRepository<UserSession, String>{
	Optional<UserSession> findByUserId(long userId);

	  // Define a JPQL delete query
    @Modifying
    @Transactional
    @Query("DELETE FROM UserSession us WHERE us.userId = :userId")
    void deleteByUserId(long userId);

}
