package com.minch.controller;

import com.minch.dto.AdminTopicResponseDTO;
import com.minch.dto.ReviewRequestDTO;
import com.minch.entity.Topic;
import com.minch.exception.ResourceNotFoundException;
import com.minch.service.TopicService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/admin/topic")
public class AdminController {

    @Autowired
    private TopicService topicService;


    // 审核词条
    @PutMapping("/get")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getTopicById(@RequestParam Integer id) {
        Topic topic = topicService.findTopicById(id);
        if (topic == null){
            throw new RuntimeException("Topic is not exists");
        }

        AdminTopicResponseDTO dto = new AdminTopicResponseDTO();

        dto.setId(topic.getId());
        dto.setAcceptTable(topic.getAcceptTable());
        dto.setTitle(topic.getTitle());
        dto.setContent(topic.getContent());
        dto.setLanguages(topic.getLanguages());

        return ResponseEntity.ok(dto);
    }

    @PostMapping("/review")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getTopicById(@Valid @RequestBody ReviewRequestDTO reviewRequestDTO){
        topicService.reviewTopicWithEmail(reviewRequestDTO);
        return ResponseEntity.ok("Topic status changed successfully");
    }
}
