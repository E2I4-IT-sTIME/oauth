package com.kb_hackathon.plovo_backend.repository;

import com.kb_hackathon.plovo_backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    // JPA findBy 규칙
    // select * from user_master where kakao_email = ?
    User findByKakaoEmail(String kakaoEmail);

    User findByUserCode(Long userCode);
}
