package com.academy.fourtk.springbootessentials.requesties;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
public class AnimePutRequesteBody {
    @NotNull(message = "The anime id cannot be null")
    private Long id;
    @NotBlank(message = "The anime name cannot be empty")
    @Schema(description = "This is the Anime`s name", example = "Dragon Ball Z", required = true)
    private String name;
}
