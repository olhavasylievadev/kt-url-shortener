package com.url_shortener

import com.url_shortener.routes.UrlRequest
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class UrlShortenerTest {
    @Test
    fun testShortenEndpoint() = runTest {
        testApplication {
            application { main() }

            val response = client.post("/shorten") {
                contentType(ContentType.Application.Json)
                setBody(Json.encodeToString(UrlRequest("https://example.com")))
            }

            assertEquals(HttpStatusCode.OK, response.status)
            val body = response.bodyAsText()
            println("Response: $body")
        }
    }
    // potential improvements: check for the fail, check for redirect status code,
    // check for invalid URL
}