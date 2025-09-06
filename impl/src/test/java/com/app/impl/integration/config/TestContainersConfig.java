package com.app.impl.integration.config;

import java.time.Duration;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

import com.redis.testcontainers.RedisContainer;

@TestConfiguration
public class TestContainersConfig {

    @Bean
    @ServiceConnection
    public RedisContainer redisContainer() {
        return new RedisContainer(DockerImageName.parse("redis:7.0-alpine"))
                .withExposedPorts(6379)
                .withStartupTimeout(Duration.ofMinutes(2));
    }

    @Bean
    @ServiceConnection
    public PostgreSQLContainer<?> postgreSQLContainer() {
        return new PostgreSQLContainer<>("postgres:17-alpine")
                .withStartupTimeout(Duration.ofMinutes(2));
    }
}