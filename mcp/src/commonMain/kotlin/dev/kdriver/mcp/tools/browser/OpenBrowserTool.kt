package dev.kdriver.mcp.tools.browser

import dev.kdriver.core.browser.Config
import dev.kdriver.mcp.services.IBrowserService
import dev.kdriver.mcp.tools.ITool
import io.modelcontextprotocol.kotlin.sdk.CallToolRequest
import io.modelcontextprotocol.kotlin.sdk.CallToolResult
import io.modelcontextprotocol.kotlin.sdk.TextContent

class OpenBrowserTool(
    private val browserService: IBrowserService,
) : ITool {

    override val name = "OpenBrowserTool"
    override val description = "Start a browser with an empty page"
    override val params = emptyMap<String, String>()

    override suspend fun invoke(input: CallToolRequest): CallToolResult {
        browserService.setupBrowser(Config())
        return CallToolResult(content = listOf(TextContent("Browser opened successfully!")))
    }

}
