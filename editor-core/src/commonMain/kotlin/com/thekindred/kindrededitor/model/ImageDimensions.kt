package com.thekindred.kindrededitor.model

import kotlinx.serialization.Serializable

@Serializable
data class ImageDimensions(
    val width: Int? = null,
    val height: Int? = null,
    val maxWidth: Int? = null
)
