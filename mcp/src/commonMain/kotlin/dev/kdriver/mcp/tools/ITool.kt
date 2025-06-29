package dev.kdriver.mcp.tools

import dev.kaccelero.usecases.ISuspendUseCase
import io.modelcontextprotocol.kotlin.sdk.CallToolRequest
import io.modelcontextprotocol.kotlin.sdk.CallToolResult

interface ITool : ISuspendUseCase<CallToolRequest, CallToolResult> {

    val name: String
    val description: String
    val params: Map<String, String>

}
