package com.minch.service.impl;

import com.minch.model.Topic;
import com.minch.repository.TopicRepository;
import com.minch.service.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Optional;

@Service
public class TopicServiceImpl implements TopicService {

    @Autowired
    private TopicRepository topicRepository;

    @Override
    public Topic addTopic(@RequestBody Topic topic) {
        if (topic.getTitle() == null || topic.getTitle().isEmpty()){
            throw new RuntimeException("The title can not be blank");
        }
        if (topic.getContent() == null || topic.getContent().isEmpty()){
            throw new RuntimeException("The content can not be blank");
        }
        if (topic.getLanguages() == null || topic.getLanguages().isEmpty()){
            throw new RuntimeException("The language of topic can not be blank");
        }

        if (topicRepository.existsByTitle(topic.getTitle())){
            throw new RuntimeException("Title is already used");
        }
        return topicRepository.save(topic);
    }

    @Override
    public Topic selectTopicByTitle(String title) {
        return topicRepository.findByTitle(title);
    }


    @Override
    public List<Topic> selectTopicByLetters(String letters) {

        List<Topic> topics = topicRepository.findByTitleStartingWith(letters);
        return topics;
    }

    @Override
    public List<Topic> selectTopicByLettersAndLanguages(String languages, String letters) {
        return topicRepository.findByLanguagesAndTitleStartingWith(languages, letters);
    }

    @Override
    public Topic updateTopic(Topic topic) {
        if (topic != null) {
            Topic t = topicRepository.findByTitle(topic.getTitle());
            if (t != null){
                // 如果标题存在，即可以对内容进行更改
                t.setContent(topic.getContent());
                return topicRepository.save(t);
            } else {
                return null;
            }
        }
        return null;
    }

    // @Override
    // public Optional<Topic> selectTopicById(Integer id) {
    //     return topicRepository.findById(id);
    // }


}
