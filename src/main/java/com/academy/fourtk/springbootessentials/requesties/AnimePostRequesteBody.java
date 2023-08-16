package com.academy.fourtk.springbootessentials.requesties;

import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(description = "This is the Anime`s name", example = "Dragon Ball Z", required = true)
    private String name;
}
