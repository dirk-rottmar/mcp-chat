package net.rottmar.ai.chat.mcp_chat.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.stereotype.Service;

@Service
public class ChatService {

    private final ChatClient chatClient;

    public ChatService(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    public String ask(String question, String conversationId) {

        var mathPromptExtension = """
                You are a precise assistant.
                When using the math tool, just provide the final result in plain text.
                Do not generate spaces in numbers.
                Do not use LaTeX or formulas.
                Use the math tool even when using other tools and asked for calculations.
                Whenever a user asks a question involving numbers, subtractions, additions,
                or any quantities (e.g., '10 persons less 8'), you MUST call the 'mcp-math-tool'.
                Do not provide an answer until you have received the result from the tool.
                """;



        return chatClient.prompt()
                .system(mathPromptExtension)
                .user(question)
                .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, conversationId))
                .call()
                .content();
    }
}