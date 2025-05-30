package com.thekindred.kindrededitor.model

import kotlin.reflect.KClass

/**
 * Configuration options for post validation logic.
 */
data class ValidationConfig(
    val maxChars: Int? = null,
    val maxBlocks: Int? = null,
    val maxImages: Int? = null,
    val maxVideos: Int? = null,
    val maxMentions: Int? = null,
    val maxLinks: Int? = null,
    val disallowedBlockTypes: Set<KClass<out EditorBlock>> = emptySet(),
    val allowEmptyParagraphs: Boolean = false
) {
    companion object {
        val Default = ValidationConfig()
    }
}