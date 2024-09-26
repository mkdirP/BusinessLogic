package com.minch.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateTopicDTO {


    private String title;

    @NotBlank(message = "Content is required")
    private String content;
}
