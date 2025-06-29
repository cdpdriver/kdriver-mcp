package dev.kdriver.mcp

import dev.kdriver.core.browser.Browser
import io.modelcontextprotocol.kotlin.sdk.*
import io.modelcontextprotocol.kotlin.sdk.server.Server
import io.modelcontextprotocol.kotlin.sdk.server.ServerOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.put

fun configureServer(mainScope: CoroutineScope): Server {
    val server = Server(
        Implementation(
            name = "kdriver-mcp",
            version = "0.1.0"
        ),
        ServerOptions(
            capabilities = ServerCapabilities(
                tools = ServerCapabilities.Tools(listChanged = true),
            )
        )
    )

    var browser: Browser? = null

    server.addTool(
        name = "Open a Browser",
        description = "Start a browser",
        inputSchema = Tool.Input()
    ) { request ->
        mainScope.launch { browser = Browser.create(mainScope) }.join()
        CallToolResult(content = listOf(TextContent("Browser opened successfully!")))
    }
    server.addTool(
        name = "Navigate to a URL",
        description = "Navigate the browser to a specific URL",
        inputSchema = Tool.Input(
            properties = buildJsonObject {
                put("url", buildJsonObject {
                    put("type", "string")
                    put("description", "The URL to navigate to")
                })
            },
            required = listOf("url")
        )
    ) { request ->
        val url = request.arguments["url"]?.jsonPrimitive?.content ?: return@addTool CallToolResult(
            content = listOf(TextContent("URL is required"))
        )
        if (browser == null) return@addTool CallToolResult(content = listOf(TextContent("Browser is not opened")))
        println("Navigating to $url -")
        mainScope.launch { browser.get(url) }.join()
        CallToolResult(content = listOf(TextContent("Navigated to $url")))
    }

    return server
}
