package com.minch.controller;

import com.minch.dto.SelectTopicByDTO;
import com.minch.dto.TopicDTO;
import com.minch.dto.UpdateTopicDTO;
import com.minch.entity.Topic;
import com.minch.entity.Users;
import com.minch.service.TopicService;
import com.minch.service.UsersService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/topics")
public class TopicController {

    @Autowired
    private TopicService topicService;
    @Autowired
    private UsersService usersService;


    // topic - 增
    @PutMapping("/add")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> addTopic(@Valid @RequestBody TopicDTO topicDTO){
        try {
            Topic topic = new Topic();
            Users currentUser = usersService.getCurrentUser();

            if (topicService.existsByTitle(topicDTO.getTitle()) || currentUser == null) {
                throw new RuntimeException("About the title of this article has been used");
            }

            topic.setTitle(topicDTO.getTitle());
            topic.setContent(topicDTO.getContent());
            topic.setLanguages(topicDTO.getLanguages());
            topic.setUsers(usersService.getCurrentUser());

            topic.setAcceptTable(null);

            topicService.addTopicWithEmail(topic);

            topicService.save(topic);
            return ResponseEntity.status(HttpStatus.CREATED).body(topicDTO);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage()); // 返回错误提示信息
        }
    }

    // topic - 查
    @GetMapping("/get{letters}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public List<Topic> selectTopicByLetters(@RequestParam String letters){

        SelectTopicByDTO dto = new SelectTopicByDTO();
        dto.setLetters(letters);

        List<Topic> topics = topicService.selectTopicByLetters(dto);
        return topics;
    }

    @GetMapping("/gets")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public List<Topic> selectTopicByLettersAndLanguages(@RequestParam String languages,
                                                        @RequestParam String letters){
        SelectTopicByDTO dto = new SelectTopicByDTO();
        dto.setLetters(letters);
        dto.setLanguages(languages);

        List<Topic> topics = topicService.selectTopicByLettersAndLanguages(dto.getLanguages(), dto.getLetters());
        return topics;
    }

    // TOPIC - 改
    @PutMapping("/put{title}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> updateTopic(@RequestParam String title,
                                         @Valid @RequestBody UpdateTopicDTO updateTopicDTO){
        // 将 URL 中的 title 设置到 TopicDTO
        updateTopicDTO.setTitle(title);

        Topic t = topicService.updateTopic(updateTopicDTO);

        if (t != null){
            return ResponseEntity.ok(t);
        }else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("This topic does not exist yet");
        }
    }

}
