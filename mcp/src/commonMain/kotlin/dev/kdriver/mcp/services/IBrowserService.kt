package dev.kdriver.mcp.services

import dev.kdriver.core.browser.Browser
import dev.kdriver.core.browser.Config

interface IBrowserService {

    suspend fun setupBrowser(config: Config)

    suspend fun <T> withBrowser(
        action: suspend (Browser) -> T,
    ): T

}
