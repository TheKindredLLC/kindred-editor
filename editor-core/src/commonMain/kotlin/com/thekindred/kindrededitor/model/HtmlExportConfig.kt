package com.thekindred.kindrededitor.model

/**
 * Configuration for HTML export behavior.
 */
data class HtmlExportConfig(
    val openLinksInNewTab: Boolean = true,
    val includeInlineStyles: Boolean = true,
    val emojiAsUnicode: Boolean = true,
    val headingLevelOffset: Int = 0, // e.g. +1 means start at <h2> instead of <h1>
    val renderClosedCaptions: Boolean = false,
    val pollAsList: Boolean = true
) {
    companion object {
        val Default = HtmlExportConfig()
    }
}