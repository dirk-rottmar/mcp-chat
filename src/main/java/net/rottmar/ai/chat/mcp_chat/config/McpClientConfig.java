package net.rottmar.ai.chat.mcp_chat.config;

import io.modelcontextprotocol.client.McpClient;
import io.modelcontextprotocol.client.McpSyncClient;
import io.modelcontextprotocol.client.transport.HttpClientSseClientTransport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.mcp.SyncMcpToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


import java.time.Duration;
import java.util.ArrayList;
import java.util.List;




@Configuration
public class McpClientConfig {

    private static final Logger log = LoggerFactory.getLogger(McpClientConfig.class);

    @Bean
    public List<McpSyncClient> mcpSyncClients() {
        List<ServerConfig> servers = List.of(
                new ServerConfig("profiles-generator", "http://localhost:8085"),
                new ServerConfig("date-time", "http://localhost:8086"),
                new ServerConfig("math-tool", "http://localhost:8087")
        );

        List<McpSyncClient> clients = new ArrayList<>();
        for (ServerConfig s : servers) {
            try {
                var transport = HttpClientSseClientTransport.builder(s.url())
                        .sseEndpoint("/sse")
                        .build();
                var client = McpClient.sync(transport)
                        .requestTimeout(Duration.ofSeconds(5))
                        .build();
                client.initialize();
                clients.add(client);
                log.info("✅ Connected to MCP Server: {}", s.name());
            } catch (Exception e) {
                log.warn("⚠️ MCP Server not reachable, will be skipped: {}", s.name());
            }
        }
        return clients;
    }

    @Bean
    public SyncMcpToolCallbackProvider toolCallbackProvider(List<McpSyncClient> mcpSyncClients) {
        return new SyncMcpToolCallbackProvider(mcpSyncClients);
    }

    record ServerConfig(String name, String url) {
    }
}
