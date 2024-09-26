package com.minch.service;

import com.minch.dto.ReviewRequestDTO;
import com.minch.dto.SelectTopicByDTO;
import com.minch.dto.TopicDTO;
import com.minch.dto.UpdateTopicDTO;
import com.minch.entity.Topic;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Optional;


public interface TopicService {

    Topic findTopicById(Integer id);

    // Topic addTopic(@RequestBody TopicDTO topicDTO);

    List<Topic> selectTopicByLetters(SelectTopicByDTO letters);
    List<Topic> selectTopicByLettersAndLanguages(String languages, String letters);
    Topic updateTopic(UpdateTopicDTO updateTopicDTO);

    void reviewTopicWithEmail(ReviewRequestDTO reviewRequestDTO);

    @Transactional
    void addTopicWithEmail(Topic topic);

    Boolean existsByTitle(String title);

    Topic save(Topic topic);
}
