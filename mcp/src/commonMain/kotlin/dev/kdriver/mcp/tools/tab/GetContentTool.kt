package dev.kdriver.mcp.tools.tab

import dev.kdriver.mcp.services.IBrowserService
import dev.kdriver.mcp.tools.ITool
import io.modelcontextprotocol.kotlin.sdk.CallToolRequest
import io.modelcontextprotocol.kotlin.sdk.CallToolResult
import io.modelcontextprotocol.kotlin.sdk.TextContent

class GetContentTool(
    private val browserService: IBrowserService,
) : ITool {

    override val name = "GetContentTool"
    override val description = "Get the content of the current browser tab"
    override val params = emptyMap<String, String>()

    override suspend fun invoke(input: CallToolRequest): CallToolResult = browserService.withBrowser { browser ->
        val tab = browser.mainTab
            ?: return@withBrowser CallToolResult(content = listOf(TextContent("No active tab found, please call NavigateTool first")))
        val content = tab.getContent()
        CallToolResult(content = listOf(TextContent(content)))
    }

}
