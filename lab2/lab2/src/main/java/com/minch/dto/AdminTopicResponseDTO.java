package com.minch.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminTopicResponseDTO extends TopicDTO{

    Integer id;
    Boolean acceptTable;
}
