package com.minch.service.impl;

import bitronix.tm.BitronixTransactionManager;
import bitronix.tm.TransactionManagerServices;
import com.minch.dto.ReviewRequestDTO;
import com.minch.dto.SelectTopicByDTO;
import com.minch.dto.UpdateTopicDTO;
import com.minch.entity.Topic;
import com.minch.entity.Users;
import com.minch.repository.TopicRepository;
import com.minch.repository.UsersRepository;
import com.minch.service.EmailService;
import com.minch.service.TopicService;
import com.minch.service.UsersService;
import jakarta.transaction.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import java.util.List;

@Slf4j
@Service
public class TopicServiceImpl implements TopicService {

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private UsersService usersService;

    @Autowired
    private BitronixTransactionManager btm;


    @Override
    public Topic findTopicById(Integer id) {
        return topicRepository.findTopicById(id);
    }

    private Users getCurrentUser() {
        // 获取当前用户的认证信息
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = ((UserDetails) authentication.getPrincipal()).getUsername();

        return usersRepository.findByUsername(username);
    }

    @Override
    public List<Topic> selectTopicByLetters(SelectTopicByDTO letters) {

        List<Topic> topics = topicRepository.findByTitleStartingWith(letters.getLetters());
        // 将 `Topic` 实体转换为 `TopicDTO`
        return topics;
    }

    @Override
    public List<Topic> selectTopicByLettersAndLanguages(String languages, String letters) {
        return topicRepository.findByLanguagesAndTitleStartingWith(languages, letters);
    }

    @Override
    public Topic updateTopic(UpdateTopicDTO updateTopicDTO) {

        Topic t = topicRepository.findByTitle(updateTopicDTO.getTitle());
        log.info(t.toString());
        if (t != null){
            // 如果标题存在，即可以对内容进行更改
            t.setContent(updateTopicDTO.getContent());
            return topicRepository.save(t);
        } else {
            return null;
        }

    }

    @Override
    // @Transactional
    public void reviewTopicWithEmail(ReviewRequestDTO reviewRequestDTO) {

        BitronixTransactionManager btm = TransactionManagerServices.getTransactionManager();
        try {
            btm.begin();

            Topic topic = findTopicById(reviewRequestDTO.getId());

            if (topic == null) {
                btm.rollback(); //  如果topic不存在则回滚上面的事务
                throw new RuntimeException("Topic not found");
            } else {
                topic.setAcceptTable(reviewRequestDTO.getAcceptTable());
                topic = save(topic);

                String emailMessage = topic.getAcceptTable() ? "PASSED" : "FAILED";

                String adminMessage = reviewRequestDTO.getMessage() == null ||
                        reviewRequestDTO.getMessage().trim().equals("") ? "" : "Comment admin: " + reviewRequestDTO.getMessage();

                emailService.sendMessage(topic.getUsers().getEmail(),
                        "Status of topic is already review",
                        "Hello, " + topic.getUsers().getUsername() + "!\n" +
                                "Your topic is already review and " + emailMessage + "\n" +
                                adminMessage + "\n");

                btm.commit();
            }
        } catch (HeuristicRollbackException |
                SystemException |
                HeuristicMixedException |
                NotSupportedException|
                RollbackException e) {
            throw new RuntimeException(e);
        }


    }

    @Override
    @Transactional
    public void addTopicWithEmail(Topic topic) {

        List<Users> admins = usersService.findAllAdminsByRoles("ROLE_ADMIN");
        // log.info("=================="+admins);
        for (Users admin : admins) {
            emailService.sendMessage(admin.getEmail(), "New Topic",
                    "Hello, " + admin.getUsername() + "!\n" +
                            "Here is a new topic\n" +
                            "title is \"" + topic.getTitle() +"\"\n");
        }
    }

    @Override
    public Boolean existsByTitle(String title) {
        return topicRepository.existsByTitle(title);
    }

    @Override
    public Topic save(Topic topic) {
        return topicRepository.save(topic);
    }


}
