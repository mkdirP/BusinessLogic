package com.minch.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ReviewRequestDTO {

    @NotNull
    Integer id;

    @NotNull
    Boolean acceptTable;

    String message;
}
