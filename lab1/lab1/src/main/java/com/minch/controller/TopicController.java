package com.minch.controller;

import com.minch.model.Topic;
import com.minch.model.Users;
import com.minch.service.TopicService;
import com.minch.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class TopicController {

    @Autowired
    private TopicService topicService;
    @Autowired
    private UsersService usersService;

    // topic - 增
    @PostMapping("/topics")
    public ResponseEntity<?> addTopic(@RequestBody Topic topic){
        topicService.addTopic(topic);
        return ResponseEntity.status(HttpStatus.CREATED).body(topic);
    }

    // topic - 查
    @GetMapping("/topics/{letters}")
    public List<Topic> selectTopicByLetters(@PathVariable String letters){

        List<Topic> topics = topicService.selectTopicByLetters(letters);
        return topics;
    }

    @GetMapping("/topics")
    public List<Topic> selectTopicByLettersAndLanguages(@RequestParam String languages, String letters){
        List<Topic> topics = topicService.selectTopicByLettersAndLanguages(languages, letters);
        return topics;
    }

    // TOPIC - 改
    @PutMapping("/topics/{title}")
    public ResponseEntity<?> updateTopic(@PathVariable String title, @RequestBody Topic topic){
        topic.setTitle(title);
        Topic t = topicService.updateTopic(topic);
        if (t != null){
            return ResponseEntity.ok(t);
        }else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("This topic does not exist yet");
        }
    }

    // 只是请求编辑，不是编辑，请求后会进入主界面
    // @GetMapping("/topics/id/{id}")
    // public ResponseEntity<?> editTopic(
    //         @RequestBody Users u,
    //         @PathVariable Integer id){
    //     if (!usersService.existsUsers(u)){
    //         return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not exists yet");
    //     }
    //     // updateTopic(topicService.selectTopicByTitle(title));
    //     Users user = usersService.loginUsers(u);
    //
    //     if (user != null){
    //         // 用户存在并且用户名密码匹配
    //         ResponseEntity.ok("Successful login");
    //         Optional<Topic> topic = topicService.selectTopicById(id);
    //         return ResponseEntity.ok(topic);
    //
    //     }else {
    //         return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Username or password wrong");
    //     }
    // }
}
