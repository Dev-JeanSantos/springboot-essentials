package com.academy.fourtk.springbootessentials.requesties;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class AnimePostRequesteBody {
    @NotBlank(message = "The anime name cannot be empty")
    private String name;
}
