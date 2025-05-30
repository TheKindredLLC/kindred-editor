package com.thekindred.kindrededitor.model

import kotlinx.serialization.Serializable

@Serializable
data class ListItem(
    val chunks: List<TextChunk>,
    val checked: Boolean? = null, // Only for CHECKLIST type
    val subItems: List<ListItem> = emptyList() // For nested lists
)
