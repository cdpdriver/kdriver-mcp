package dev.kdriver.mcp.tools.tab

import dev.kdriver.mcp.services.IBrowserService
import dev.kdriver.mcp.tools.ITool
import io.modelcontextprotocol.kotlin.sdk.CallToolRequest
import io.modelcontextprotocol.kotlin.sdk.CallToolResult
import io.modelcontextprotocol.kotlin.sdk.TextContent
import kotlinx.serialization.json.jsonPrimitive

class NavigateTool(
    private val browserService: IBrowserService,
) : ITool {

    override val name = "NavigateTool"
    override val description = "Navigate the browser to a specific URL"
    override val params = mapOf(
        "url" to "The URL to navigate to"
    )

    override suspend fun invoke(input: CallToolRequest): CallToolResult = browserService.withBrowser { browser ->
        val url = input.arguments["url"]?.jsonPrimitive?.content
            ?: return@withBrowser CallToolResult(content = listOf(TextContent("URL is required")))
        browser.get(url)
        CallToolResult(content = listOf(TextContent("Navigated to $url")))
    }

}
