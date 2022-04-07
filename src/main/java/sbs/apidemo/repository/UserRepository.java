package sbs.apidemo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sbs.apidemo.entity.UserEntity;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    boolean existsByEmail(String email);
    Optional<UserEntity> findByEmail(String email);
}