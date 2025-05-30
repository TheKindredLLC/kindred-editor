package com.thekindred.kindrededitor.model

import kotlinx.serialization.Serializable

/**
 * Represents an inline span of styled text within a paragraph.
 * A paragraph is composed of multiple [TextChunk]s, each potentially carrying unique formatting or metadata.
 *
 * This structure enables fine-grained control of text formatting,
 * including support for bold, italic, hyperlinks, emoji, and more.
 *
 * All formatting is optional. If all flags are false/null, the chunk is treated as plain text.
 *
 * @param text The actual text content of this chunk.
 * @param bold Whether the text is rendered in bold.
 * @param italic Whether the text is rendered in italic.
 * @param underline Whether the text is underlined.
 * @param strikethrough Whether the text has a strikethrough.
 * @param highlight Whether the text should be highlighted (e.g., background color).
 * @param color Optional hex color for the text (e.g., "#FF0000").
 * @param fontSize Optional font size override (in points, dp, or px depending on rendering layer).
 * @param link Optional hyperlink target (if present, the text should behave like a link).
 * @param mention If this chunk represents a user mention, metadata about the user.
 * @param emoji If this chunk represents an inline emoji, metadata about the emoji.
 */
@Serializable
data class TextChunk(
    val text: String = "",
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