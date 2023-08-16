package com.academy.fourtk.springbootessentials.requesties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AnimePostRequesteBody {
    @NotBlank(message = "The anime name cannot be empty")
    private String name;
}
