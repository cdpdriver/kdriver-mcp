package dev.kdriver.mcp.tools.tab

import dev.kdriver.mcp.services.IBrowserService
import dev.kdriver.mcp.tools.ITool
import io.modelcontextprotocol.kotlin.sdk.CallToolRequest
import io.modelcontextprotocol.kotlin.sdk.CallToolResult
import io.modelcontextprotocol.kotlin.sdk.TextContent
import kotlinx.serialization.json.jsonPrimitive

class SelectTool(
    private val browserService: IBrowserService,
) : ITool {

    override val name = "SelectTool"
    override val description = "Select an element in the current browser tab"
    override val params = mapOf(
        "selector" to "The selector of the element to select"
    )

    override suspend fun invoke(input: CallToolRequest): CallToolResult = browserService.withBrowser { browser ->
        val selector = input.arguments["selector"]?.jsonPrimitive?.content
            ?: return@withBrowser CallToolResult(content = listOf(TextContent("Selector is required")))
        val tab = browser.mainTab
            ?: return@withBrowser CallToolResult(content = listOf(TextContent("No active tab found, please call NavigateTool first")))
        val element = tab.select(selector)
        CallToolResult(
            content = listOf(
                TextContent(element.toString()),
                TextContent("backendNodeId = ${element.backendNodeId}"),
            )
        )
    }

}
