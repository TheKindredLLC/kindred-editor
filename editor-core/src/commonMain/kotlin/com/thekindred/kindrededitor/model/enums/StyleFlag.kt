package com.thekindred.kindrededitor.model.enums

/**
 * Represents a toggleable inline style applied to a [com.thekindred.kindrededitor.model.TextChunk].
 */
enum class StyleFlag {
    /** Bold text style (typically rendered as `<strong>` or `font-weight: bold`) */
    Bold,

    /** Italic text style (typically rendered as `<em>` or `font-style: italic`) */
    Italic,

    /** Underline text style (e.g., for emphasis or links) */
    Underline,

    /** Strikethrough style, commonly used for edits or retracted statements */
    Strikethrough,

    /** Highlighted text (e.g., with a background color for emphasis) */
    Highlight
}