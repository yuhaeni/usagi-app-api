package com.kou.kouappapi

import io.kotest.core.extensions.Extension
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.extensions.spring.SpringExtension
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.utility.DockerImageName

@SpringBootTest
@Testcontainers
abstract class IntegrationTestSupport(
    body: DescribeSpec.() -> Unit = {},
) : DescribeSpec(body) {
    override fun extensions(): List<Extension> = listOf(SpringExtension)

    companion object {
        @Container
        @ServiceConnection
        val redisContainer =
            GenericContainer(DockerImageName.parse("redis:alpine"))
                .withExposedPorts(6379)
                .apply { start() }

        @Container
        @ServiceConnection // Spring이 자동으로 datasource url, username, password를 교체해줌
        val postgresContainer =
            PostgreSQLContainer<Nothing>(DockerImageName.parse("postgres:15-alpine"))
                .apply {
                    withDatabaseName("testdb")
                    withUsername("test")
                    withPassword("test")
                }.apply { start() }
    }
}
