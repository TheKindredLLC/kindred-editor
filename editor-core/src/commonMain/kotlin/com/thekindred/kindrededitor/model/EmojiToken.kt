package com.thekindred.kindrededitor.model

import kotlinx.serialization.Serializable

/**
 * Represents an inline emoji within a [TextChunk].
 *
 * This allows for consistent, platform-independent emoji rendering by associating
 * each emoji with a specific shortcode and image resource.
 *
 * @param shortcode The text-based emoji identifier (e.g., ":smile:").
 * @param name A human-readable name or label for the emoji (e.g., "Smiling Face").
 * @param url A URL or asset path to the image representing the emoji.
 *            Can be a remote CDN URL, embedded base64, or local file path.
 */
@Serializable
data class EmojiToken(
    val shortcode: String,   // e.g., ":smile:"
    val name: String,        // descriptive name
    val url: String          // image or SVG path
)