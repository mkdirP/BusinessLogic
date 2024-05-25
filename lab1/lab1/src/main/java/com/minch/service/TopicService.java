package com.minch.service;

import com.minch.model.Topic;

import java.util.List;
import java.util.Optional;


public interface TopicService {

    Topic addTopic(Topic topic);
    Topic selectTopicByTitle(String title);
    List<Topic> selectTopicByLetters(String letters);
    List<Topic> selectTopicByLettersAndLanguages(String languages, String letters);
    Topic updateTopic(Topic topic);
    // Optional<Topic> selectTopicById(Integer id);

}
