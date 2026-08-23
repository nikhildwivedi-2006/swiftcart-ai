package in.swiftcart.ai.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AIRequestDTO {

    @NotBlank(message = "Message cannot be empty")
    private String message;

}