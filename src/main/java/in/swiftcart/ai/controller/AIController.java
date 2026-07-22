package in.swiftcart.ai.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import in.swiftcart.ai.dto.AIRequestDTO;
import in.swiftcart.ai.dto.AIResponseDTO;
import in.swiftcart.ai.service.AIService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/ai")
public class AIController {

    private final AIService aiService;

    public AIController(AIService aiService) {
        this.aiService = aiService;
    }

    @PostMapping("/chat")
    public ResponseEntity<AIResponseDTO> chat(@Valid @RequestBody AIRequestDTO request) {

        String response = aiService.chat(request.getMessage());

        AIResponseDTO aiResponse = new AIResponseDTO(response);

        return ResponseEntity.ok(aiResponse);
    }
}