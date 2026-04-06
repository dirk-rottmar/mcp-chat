package net.rottmar.ai.chat.mcp_chat.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

@Service
public class ChatService {

    private final ChatClient chatClient;
    private final Resource systemPrompt;

    public ChatService(ChatClient chatClient, @Value("classpath:prompts/system-prompt.md") Resource systemPrompt) {
        this.chatClient = chatClient;
        this.systemPrompt = systemPrompt;
    }

    public String ask(String question, String conversationId) {

        return chatClient.prompt()
                .system(s -> s.text(systemPrompt))
                .user(question)
                .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, conversationId))
                .call()
                .content();
    }
}