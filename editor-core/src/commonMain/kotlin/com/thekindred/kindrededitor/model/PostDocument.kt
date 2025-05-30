package com.thekindred.kindrededitor.model

import kotlinx.serialization.Serializable

/**
 * Represents a complete editor post or document.
 *
 * This is the top-level container that wraps a sequence of [EditorBlock]s,
 * along with optional metadata. It is intended to be serialized for storage,
 * transmission, or rendering across platforms.
 *
 * @param blocks The ordered list of content blocks composing the post.
 * @param metadata Optional key-value pairs for post-level data, such as tags, visibility,
 *                 creation timestamps, or custom consumer metadata.
 */
@Serializable
data class PostDocument(
    val blocks: List<EditorBlock> = emptyList(),
    val metadata: Map<String, String> = emptyMap()
)