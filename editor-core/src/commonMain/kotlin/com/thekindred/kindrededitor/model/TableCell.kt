package com.thekindred.kindrededitor.model

import kotlinx.serialization.Serializable

@Serializable
data class TableCell(
    val chunks: List<TextChunk>,
    val colSpan: Int = 1,
    val rowSpan: Int = 1
)
