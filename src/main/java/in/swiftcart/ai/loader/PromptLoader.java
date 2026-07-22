package in.swiftcart.ai.loader;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

@Component
public class PromptLoader {

    public String loadPrompt(String fileName) {
        try {
            ClassPathResource resource = new ClassPathResource("prompts/" + fileName);

            byte[] bytes = resource.getInputStream().readAllBytes();

            return new String(bytes, StandardCharsets.UTF_8);

        } catch (IOException e) {
            throw new RuntimeException("Failed to load system prompt.", e);
        }
    }

}