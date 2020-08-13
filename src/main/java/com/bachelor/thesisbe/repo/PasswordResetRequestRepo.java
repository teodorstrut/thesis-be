package com.bachelor.thesisbe.repo;

import com.bachelor.thesisbe.model.PasswordResetRequest;
import com.bachelor.thesisbe.model.UserEntity;
import org.springframework.data.repository.CrudRepository;

public interface PasswordResetRequestRepo extends CrudRepository<PasswordResetRequest, Long> {

    PasswordResetRequest findByRequester(UserEntity requester);
}
