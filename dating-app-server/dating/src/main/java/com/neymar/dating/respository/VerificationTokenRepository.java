package com.neymar.dating.respository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.neymar.dating.model.VerificationToken;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, String>{
	
	List<VerificationToken> findByUserEmail(String email);
	
    List<VerificationToken> findByToken(String token);
}
