package dev.kdriver.mcp.services

import dev.kdriver.core.browser.Browser
import dev.kdriver.core.browser.Config
import kotlinx.coroutines.CoroutineScope

class BrowserService(
    private val coroutineScope: CoroutineScope,
) : IBrowserService {

    private var browser: Browser? = null

    override suspend fun setupBrowser(config: Config) {
        browser = Browser.create(coroutineScope, config)
    }

    override suspend fun <T> withBrowser(
        action: suspend (Browser) -> T,
    ): T {
        val browser = browser ?: throw IllegalStateException("Browser is not initialized. Call OpenBrowserTool first.")
        return action(browser)
    }

}
