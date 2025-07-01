package com.thekindred.kindrededitor.model

import kotlinx.serialization.Serializable
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

/**
 * Represents a specific position in a block-based document.
 *
 * This can point to a block ID and optionally a child index (e.g., for text chunks).
 *
 * For block-level selections (e.g., image, video), [chunkIndex] can be null.
 *
 * @property blockId The unique ID of the [EditorBlock] being referenced.
 * @property chunkIndex Optional index into a list of text chunks.
 *                      Null for non-textual blocks or whole-block selection.
 * @property offset Optional character offset within the chunk (for fine-grained selection).
 */
@Serializable
data class SelectionPosition @OptIn(ExperimentalUuidApi::class) constructor(
    val blockId: Uuid,
    val chunkIndex: Int? = null,
    val offset: Int? = null
)