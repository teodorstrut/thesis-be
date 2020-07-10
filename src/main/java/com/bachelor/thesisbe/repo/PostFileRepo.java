package com.bachelor.thesisbe.repo;

import com.bachelor.thesisbe.model.PostFile;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostFileRepo extends CrudRepository<PostFile, Long> {
}
