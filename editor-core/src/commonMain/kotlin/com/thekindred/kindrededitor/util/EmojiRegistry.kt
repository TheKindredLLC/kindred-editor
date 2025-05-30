package com.thekindred.kindrededitor.util

import com.thekindred.kindrededitor.model.EmojiToken

/**
 * A simple in-memory registry for known emoji tokens.
 *
 * This can be used for validating shortcodes or retrieving emoji metadata
 * without requiring a rendering layer to load everything externally.
 */
object EmojiRegistry {

    /** Static map of emoji shortcodes to [EmojiToken] entries. */
    private val emojiMap: Map<String, EmojiToken> = listOf(
        EmojiToken(":smile:", "Smiling Face", "https://example.com/emoji/smile.png"),
        EmojiToken(":heart:", "Heart", "https://example.com/emoji/heart.png"),
        EmojiToken(":star:", "Star", "https://example.com/emoji/star.png")
    ).associateBy { it.shortcode }

    /**
     * Retrieves an [EmojiToken] for a given shortcode.
     *
     * @param shortcode The shortcode (e.g., ":smile:")
     * @return The corresponding emoji token, or `null` if not found.
     */
    fun get(shortcode: String): EmojiToken? = emojiMap[shortcode]

    /**
     * Returns all registered emoji tokens.
     */
    fun all(): Collection<EmojiToken> = emojiMap.values
}