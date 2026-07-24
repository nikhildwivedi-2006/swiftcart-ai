package in.swiftcart.ai.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import in.swiftcart.ai.loader.PromptLoader;
import in.swiftcart.exception.InvalidOperationException;

@Service
public class AIServiceImpl implements AIService {

	
	
    private final ChatClient chatClient;
    private final PromptLoader promptLoader;

    public AIServiceImpl(ChatClient chatClient,
                         PromptLoader promptLoader) {

        this.chatClient = chatClient;
        this.promptLoader = promptLoader;
    }
    
    private String getPromptFile(String message) {

        String msg = message.trim().toLowerCase();

        if (msg.contains("payment") || msg.contains("razorpay")
                || msg.contains("upi") || msg.contains("cod")) {

            return "payment-prompt.txt";
        }

        if (msg.contains("refund") || msg.contains("return")
                || msg.contains("replace")) {

            return "return-refund-prompt.txt";
        }

        if (msg.contains("order") || msg.contains("delivery")
                || msg.contains("shipping")) {

            return "order-prompt.txt";
        }

        if (msg.contains("buy") || msg.contains("product")
                || msg.contains("recommend")) {

            return "shopping-prompt.txt";
        }

        if (msg.contains("hi") || msg.contains("hello")
                || msg.contains("hey")) {

            return "greeting-prompt.txt";
        }

        return "faq-prompt.txt";
    }

    @Override
    public String chat(String message) {

        try {

            String systemPrompt = promptLoader.loadPrompt("system-prompt.txt");

            String promptFile = getPromptFile(message);

            String categoryPrompt = promptLoader.loadPrompt(promptFile);

            return chatClient
                    .prompt()
                    .system(systemPrompt + "\n\n" + categoryPrompt)
                    .user(message)
                    .call()
                    .content();

        } catch (Exception e) {
            throw new InvalidOperationException("AI service is currently unavailable");
        }
    }
}