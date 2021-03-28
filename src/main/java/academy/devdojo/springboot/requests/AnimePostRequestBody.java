package academy.devdojo.springboot.requests;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AnimePostRequestBody {

    @Schema(description = "This is the anime name.", example = "Overlord")
    @NotEmpty(message = "Anime name cannot be empty")
    private String name;
}
