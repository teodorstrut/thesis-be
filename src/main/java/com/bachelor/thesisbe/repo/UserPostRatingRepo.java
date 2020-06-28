package com.bachelor.thesisbe.repo;


import com.bachelor.thesisbe.model.UserPostKey;
import com.bachelor.thesisbe.model.UserPostRating;
import org.springframework.data.repository.CrudRepository;

public interface UserPostRatingRepo extends CrudRepository<UserPostRating, UserPostKey> {
}
