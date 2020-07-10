package com.bachelor.thesisbe.repo;

import com.bachelor.thesisbe.model.Forum;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ForumRepo extends CrudRepository<Forum, Long> {
    void deleteById(Long forumId);

    List<Forum> findAll();
}
