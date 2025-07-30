package com.url_shortener.db

import org.jetbrains.exposed.sql.Database
import org.flywaydb.core.Flyway

object DBFactory {
    fun init() {
        val url = System.getenv("DB_URL") ?: "jdbc:postgresql://localhost:5432/urlshortener"
        val user = System.getenv("DB_USER") ?: "postgres"
        val password = System.getenv("DB_PASS") ?: "postgres"

        Flyway.configure()
            .dataSource(url, user, password)
            .locations("classpath:db/migration")
            .baselineOnMigrate(true)
            .load()
            .migrate()

        Database.connect(url, driver = "org.postgresql.Driver", user = user, password = password)
    }
}