package com.example.sortingcontroller.rest


import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.testcontainers.containers.JdbcDatabaseContainer
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.utility.DockerImageName

fun postgres(imageName: String, opts:JdbcDatabaseContainer<Nothing>.() -> Unit) =
    PostgreSQLContainer<Nothing>(DockerImageName.parse(imageName)).apply(opts)

@Testcontainers
class IntegrationTests {

    @Container
    val container = PostgreSQLContainer<Nothing>(
        DockerImageName.parse("postgres:13-alpine")
    ).apply {
        withDatabaseName("db")
        withUsername("user")
        withPassword("password")
        withInitScript("sql/schema.sql")
    }

    @Test
    fun container_is_up_and_running () {
        Assertions.assertTrue(container.isRunning)
    }
}