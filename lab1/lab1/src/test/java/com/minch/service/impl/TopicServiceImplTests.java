package com.minch.service.impl;

import com.minch.dto.ReviewRequestDTO;
import com.minch.entity.Topic;
import com.minch.service.EmailService;
import com.minch.service.TopicService;
import com.minch.service.UsersService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
public class TopicServiceImplTests {

    @Mock
    private TopicService topicService;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private TopicServiceImpl topicServiceImpl; // 用你的实际实现类替换

    private Topic testTopic;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        testTopic = new Topic();
        testTopic.setId(1);
        testTopic.setTitle("Test Title");
        testTopic.setContent("Test Content");
        testTopic.setAcceptTable(false);
    }

    @Test
    @Transactional
    public void testReviewTopicWithEmail_Success() {
        // 模拟 findTopicById 方法
        when(topicService.findTopicById(1)).thenReturn(testTopic);

        // 模拟 save 方法
        when(topicService.save(testTopic)).thenReturn(testTopic);

        // 模拟 emailService 发送邮件
        doNothing().when(emailService).sendMessage(anyString(), anyString(), anyString());

        ReviewRequestDTO reviewRequestDTO = new ReviewRequestDTO();
        reviewRequestDTO.setId(1);
        reviewRequestDTO.setAcceptTable(true);
        reviewRequestDTO.setMessage("Looks good!");

        topicServiceImpl.reviewTopicWithEmail(reviewRequestDTO);

        // 验证方法调用
        verify(topicService).findTopicById(1);
        verify(topicService).save(testTopic);
        verify(emailService).sendMessage(eq("mkdirp930@gmail.com"), anyString(), contains("Your topic is already review and PASSED"));

        // 如果测试事务，可以检查数据库状态（使用测试数据库）
    }

    @Test
    @Transactional
    public void testReviewTopicWithEmail_Failure() {
        // 模拟 findTopicById 方法返回 null
        when(topicService.findTopicById(1)).thenReturn(null);

        ReviewRequestDTO reviewRequestDTO = new ReviewRequestDTO();
        reviewRequestDTO.setId(1);

        // 验证异常抛出
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            topicServiceImpl.reviewTopicWithEmail(reviewRequestDTO);
        });

        assertEquals("Topic not found", thrown.getMessage());
    }
}
