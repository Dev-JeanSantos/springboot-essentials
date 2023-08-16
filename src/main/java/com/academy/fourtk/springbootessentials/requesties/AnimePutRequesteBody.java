package com.academy.fourtk.springbootessentials.requesties;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class AnimePutRequesteBody {
    @NotNull(message = "The anime id cannot be null")
    private Long id;
    @NotBlank(message = "The anime name cannot be empty")
    private String name;
}
