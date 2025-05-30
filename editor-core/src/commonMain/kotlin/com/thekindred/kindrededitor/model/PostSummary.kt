package com.thekindred.kindrededitor.model

/**
 * Summary information extracted from a [PostDocument].
 */
data class PostSummary(
    val title: String?,              // Optional if title field is stored separately
    val plainTextSnippet: String,   // First ~160 characters of body text
    val firstImageUrl: String?,     // For preview thumbnails
    val firstVideoUrl: String?,     // For video thumbnails
    val containsPoll: Boolean = false
)