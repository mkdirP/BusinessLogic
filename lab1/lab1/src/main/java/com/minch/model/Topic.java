package com.minch.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * заголовки -  条目
 *      id
 *      title - 标题
 *      content - 文章，内容
 *      language - 语言
  */
@Entity
@Table(name = "topic")
@Data
@Setter
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




}
