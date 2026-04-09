package net.rottmar.ai.chat.mcp_chat.config;

import io.modelcontextprotocol.client.McpClient;
import io.modelcontextprotocol.client.McpSyncClient;
import io.modelcontextprotocol.client.transport.HttpClientSseClientTransport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.mcp.SyncMcpToolCallbackProvider;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.http.HttpClient;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "mcp")
public class McpClientConfig {

    private static final Logger log = LoggerFactory.getLogger(McpClientConfig.class);

    // list will be populated via yaml config
    private List<ServerConfig> servers = new ArrayList<>();

    public void setServers(List<ServerConfig> servers) {
        this.servers = servers;
    }

    @Bean
    public SyncMcpToolCallbackProvider toolCallbackProvider(List<McpSyncClient> mcpSyncClients) {
        return new SyncMcpToolCallbackProvider(mcpSyncClients);
    }

    @Bean
    public List<McpSyncClient> mcpSyncClients() {
        List<McpSyncClient> clients = new ArrayList<>();

        if (servers.isEmpty()) {
            log.warn("⚠️ Could not find MCP-Server-Configuration in application*.yaml.");
            return clients;
        }

        for (ServerConfig s : servers) {
            try {
                var httpClient = HttpClient.newBuilder()
                        .connectTimeout(Duration.ofSeconds(3))
                        .build();

                var transport = HttpClientSseClientTransport.builder(s.url())
                        .sseEndpoint(s.endpoint())
                        .build();

                var client = McpClient.sync(transport)
                        .requestTimeout(Duration.ofSeconds(3))
                        .initializationTimeout(Duration.ofSeconds(3))
                        .build();

                client.initialize();
                clients.add(client);
                log.info("✅ Connected to MCP Server: {} ({}{})", s.name(), s.url(), s.endpoint());

            } catch (Exception e) {
                log.warn("⚠️ MCP Server '{}' not reachable: {} ({}{})", s.name(), e.getMessage(), s.url(), s.endpoint());
            }
        }
        return clients;
    }

    // Extended Record for mapping
    public record ServerConfig(String name, String url, String endpoint) {}
}