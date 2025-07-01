package com.thekindred.kindrededitor.model

/**
 * Configuration options for how input normalization should behave.
 *
 * @property removeEmptyBlocks If true, blocks with no meaningful content will be discarded.
 * @property allowEmptyParagraphs If false, empty paragraphs will be removed even if removeEmptyBlocks is false.
 */
data class NormalizationConfig(
    val removeEmptyBlocks: Boolean = true,
    val allowEmptyParagraphs: Boolean = false
)