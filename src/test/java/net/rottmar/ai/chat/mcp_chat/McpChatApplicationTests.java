package net.rottmar.ai.chat.mcp_chat;

import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = {
		"spring.ai.mcp.client.enabled=false"
})
class McpChatApplicationTests {

	@MockBean
    ChatClient chatClient;

	@Test
	void contextLoads() {
	}

}
