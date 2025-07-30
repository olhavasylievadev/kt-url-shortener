package com.url_shortener.model

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.javatime.timestamp
import java.time.LocalDateTime
import java.time.Instant

object Urls : IntIdTable() {
    val originalUrl = varchar("original_url", 2048)
    val createdAt = timestamp("created_at")
}

data class UrlEntry(val id: Int, val originalUrl: String, val createdAt: Instant)

fun toUrlEntry(row: ResultRow): UrlEntry =
    UrlEntry(
        id = row[Urls.id].value,
        originalUrl = row[Urls.originalUrl],
        createdAt = row[Urls.createdAt]
    )