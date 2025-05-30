package com.thekindred.kindrededitor.model

import kotlinx.serialization.Serializable

/**
 * Represents a user mention within a [TextChunk].
 *
 * This allows rich mentions of users (e.g., `@username`) that can be
 * hyperlinked or styled differently in the rendering layer.
 *
 * @param userId A unique identifier for the mentioned user (e.g., UUID or database ID).
 * @param displayName The name shown in the UI for the mention (e.g., "Alice Doe").
 */
@Serializable
data class MentionToken(
    val userId: String,
    val displayName: String
)