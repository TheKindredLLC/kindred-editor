package com.thekindred.kindrededitor.model

data class PostMetrics(
    val totalWords: Int,
    val totalChars: Int,
    val blockCount: Int,
    val imageCount: Int,
    val videoCount: Int,
    val mentionCount: Int,
    val emojiCount: Int,
    val linkCount: Int,
    val hasPoll: Boolean,
    val hasCode: Boolean,
    val hasQuote: Boolean
)