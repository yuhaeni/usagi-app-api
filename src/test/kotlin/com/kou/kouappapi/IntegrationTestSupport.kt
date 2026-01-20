package com.kou.kouappapi

import io.kotest.core.extensions.Extension
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.extensions.spring.SpringExtension
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.utility.DockerImageName

@SpringBootTest
abstract class IntegrationTestSupport(
    body: DescribeSpec.() -> Unit = {},
) : DescribeSpec(body) {
    override fun extensions(): List<Extension> = listOf(SpringExtension)

    companion object {
        val redisContainer =
            GenericContainer(DockerImageName.parse("redis:alpine"))
                .withExposedPorts(6379)
                .apply { start() }

        @ServiceConnection
        val postgresContainer =
            PostgreSQLContainer<Nothing>(DockerImageName.parse("postgres:15-alpine"))
                .apply {
                    withDatabaseName("testdb")
                    withUsername("test")
                    withPassword("test")
                }.apply { start() }

        @JvmStatic
        @DynamicPropertySource
        fun overrideProps(registry: DynamicPropertyRegistry) {
            registry.add("spring.data.redis.host") { redisContainer.host }
            registry.add("spring.data.redis.port") { redisContainer.getMappedPort(6379).toString() }
        }
    }
}
