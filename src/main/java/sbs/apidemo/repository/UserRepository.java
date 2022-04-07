package sbs.apidemo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sbs.apidemo.entity.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    boolean existsByEmail(String email);
}