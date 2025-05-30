package com.thekindred.kindrededitor.model

data class PostChangeSummary(
    val addedBlocks: List<Int>,
    val removedBlocks: List<Int>,
    val modifiedBlocks: List<Int>,
    val hasSignificantChanges: Boolean
)
