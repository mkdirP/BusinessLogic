package com.minch.repository;


import com.minch.entity.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TopicRepository  extends JpaRepository<Topic, Integer> {

    @Query("SELECT t FROM Topic t WHERE t.languages IN ?1 AND t.title LIKE ?2%")
    List<Topic> findByLanguagesAndTitleStartingWith(String languages, String searchText);

    // %?1% contains
    @Query("SELECT t FROM Topic t WHERE t.title LIKE ?1% ORDER BY t.id ASC")
    List<Topic> findByTitleStartingWith(String letters);

    Topic findByTitle(String title);
    Topic findTopicById(Integer id);
    boolean existsByTitle(String title);

    Topic save(Topic topic);

}
