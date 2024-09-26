package com.minch.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;

/**
 * заголовки -  条目
 *      id
 *      title - 标题
 *      content - 文章，内容
 *      language - 语言
  */
@Entity
@Table(name = "topics")
@Data
public class Topic {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "title")
    private String title;

    @Column(name = "content")
    private String content;

    @Column(name = "languages")
    private String languages;

    @Column(name = "acceptTable")
    private Boolean acceptTable;

    // 防止懒加载
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @JsonIgnore
    private Users users;


}
