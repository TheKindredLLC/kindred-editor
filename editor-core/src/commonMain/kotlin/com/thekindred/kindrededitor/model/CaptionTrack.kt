package com.thekindred.kindrededitor.model

import kotlinx.serialization.Serializable

/**
 * Represents a closed caption or subtitle track associated with a video.
 *
 * @property src The URL to the caption file (e.g., `.vtt` or `.srt`)
 * @property format The format of the caption file (e.g., `"vtt"`)
 * @property language Optional language code (e.g., `"en"`, `"es"`)
 * @property label Optional label to show in player UI (e.g., `"English"`)
 */
@Serializable
data class CaptionTrack(
    val src: String,
    val format: String = "vtt",
    val language: String? = null,
    val label: String? = null
)
