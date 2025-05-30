package com.thekindred.kindrededitor.model

import kotlinx.serialization.Serializable

@Serializable
data class CaptionCue(
    val start: String,  // "00:00:05.000"
    val end: String,    // "00:00:08.000"
    val text: String
)
