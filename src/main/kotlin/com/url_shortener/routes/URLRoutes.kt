package com.url_shortener.routes

import com.url_shortener.model.Urls
import com.url_shortener.model.toUrlEntry
import com.url_shortener.util.encodeBase62
import com.url_shortener.util.decodeBase62
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import java.net.URI

@Serializable
data class UrlRequest(val url: String)

@Serializable
data class UrlResponse(val hash: String)

fun Route.urlRoutes() {
//    just a good practice to have a health-check here
    get("/health") {
        call.respond(HttpStatusCode.OK, "OK")
    }

    post("/shorten") {
        val req = call.receive<UrlRequest>()

        // trying to check if the given URL is valid. If not, we throw.
        try {
            URI(req.url)
        } catch (e: Exception) {
            return@post call.respond(HttpStatusCode.BadRequest, "Invalid URL")
        }

        val id = transaction {
            Urls.insertAndGetId {
                it[originalUrl] = req.url
            }.value
        }
        call.respond(UrlResponse(hash = encodeBase62(id)))
    }

    get("/{hash}") {
        val hash = call.parameters["hash"] ?: return@get call.respond(HttpStatusCode.BadRequest)
        val id = try {
            decodeBase62(hash)
        } catch (e: Exception) {
            return@get call.respond(HttpStatusCode.BadRequest, "Invalid hash")
        }

        if (id <= 0) return@get call.respond(HttpStatusCode.BadRequest)

        val originalUrl = transaction {
            Urls.select { Urls.id eq id }.singleOrNull()?.let { toUrlEntry(it).originalUrl }
        }
        if (originalUrl != null) {
            // if we have original URL stored already, we should go with 3xx code and do
            // a permanent redirect
            call.respondRedirect(originalUrl)
        } else {
            call.respond(HttpStatusCode.NotFound)
        }
    }
}