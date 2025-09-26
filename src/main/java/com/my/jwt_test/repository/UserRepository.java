package com.my.jwt_test.repository;

import com.my.jwt_test.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Boolean existsByEmail(String email);

    //email을 받아 DB 테이블에서 회원을 조회하는 메소드 작성
    UserEntity findByEmail(String email);
}
