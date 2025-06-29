package dev.kdriver.mcp

import io.ktor.server.cio.*
import io.ktor.server.engine.*
import io.modelcontextprotocol.kotlin.sdk.server.mcp
import kotlinx.coroutines.runBlocking

fun main(): Unit = runBlocking {
    val server = configureServer(this)
    embeddedServer(CIO, host = "0.0.0.0", port = 3001) {
        mcp {
            server
        }
    }.start(true)
}
