package com.thekindred.kindrededitor.model

/**
 * Represents a problem found during post validation.
 *
 * @property message Technical/internal detail
 * @property blockIndex Index of the block where the issue occurred (-1 for global issues)
 * @property userMessage Optional user-facing message for display in UI
 */
data class ValidationError(
    val message: String,
    val blockIndex: Int,
    val userMessage: String? = null
)