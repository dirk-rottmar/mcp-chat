package net.rottmar.ai.chat.mcp_chat.controller;

import net.rottmar.ai.chat.mcp_chat.service.ChatService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chat")
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @GetMapping
    public String chat(@RequestParam String q,
                       @RequestParam(defaultValue = "default") String conversationId) {
        return chatService.ask(q, conversationId);
    }
}
