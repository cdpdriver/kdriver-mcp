package dev.kdriver.mcp

import dev.kdriver.mcp.services.BrowserService
import dev.kdriver.mcp.tools.ITool
import dev.kdriver.mcp.tools.browser.OpenBrowserTool
import dev.kdriver.mcp.tools.tab.GetContentTool
import dev.kdriver.mcp.tools.tab.NavigateTool
import dev.kdriver.mcp.tools.tab.SelectTool
import io.modelcontextprotocol.kotlin.sdk.*
import io.modelcontextprotocol.kotlin.sdk.server.Server
import io.modelcontextprotocol.kotlin.sdk.server.ServerOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.serialization.json.buildJsonObject
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

    val browserService = BrowserService(mainScope)
    listOf(
        OpenBrowserTool(browserService),
        NavigateTool(browserService),
        GetContentTool(browserService),
        SelectTool(browserService)
    ).forEach { tool ->
        server.addTool(mainScope, tool)
    }

    return server
}

private fun Server.addTool(mainScope: CoroutineScope, tool: ITool) =
    addTool(
        name = tool.name,
        description = tool.description,
        inputSchema = Tool.Input(
            properties = buildJsonObject {
                tool.params.forEach { (key, value) ->
                    put(key, buildJsonObject {
                        put("type", "string")
                        put("description", value)
                    })
                }
            },
            required = tool.params.keys.toList()
        )
    ) { request ->
        mainScope.async {
            try {
                tool(request)
            } catch (e: Exception) {
                CallToolResult(
                    content = listOf(TextContent(e.message ?: "An error occurred")),
                    isError = true
                )
            }
        }.await()
    }
