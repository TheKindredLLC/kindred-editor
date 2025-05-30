package com.thekindred.kindrededitor.model

/**
 * Represents an inline text span with formatting.
 */
data class InlineText(
    val text: String,
    val bold: Boolean = false,
    val italic: Boolean = false,
    val underline: Boolean = false,
    val strikethrough: Boolean = false,
    val highlight: Boolean = false,
    val color: String? = null,
    val fontSize: Int? = null,
    val link: String? = null,
    val mention: MentionToken? = null,
    val emoji: EmojiToken? = null
)