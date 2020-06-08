package com.bachelor.thesisbe.repo;

import com.bachelor.thesisbe.model.UserEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserEntityRepo extends CrudRepository<UserEntity, Long> {

    UserEntity findByEmail(String email);
}
