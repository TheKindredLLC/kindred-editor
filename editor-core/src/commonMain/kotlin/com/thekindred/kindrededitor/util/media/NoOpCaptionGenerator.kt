package com.thekindred.kindrededitor.util.media

import com.thekindred.kindrededitor.interfaces.CaptionGenerator
import com.thekindred.kindrededitor.model.CaptionTrack

/**
 * A no-op (no operation) implementation of [CaptionGenerator] that returns an empty [CaptionTrack].
 *
 * This is useful as a default or fallback when no AI captioning model is configured.
 * It prevents the need for null checks or placeholder logic elsewhere in the system.
 */
object NoOpCaptionGenerator : CaptionGenerator {

    /**
     * Always returns an empty [CaptionTrack] with no caption cues.
     *
     * @param videoUrl The URL of the video to "caption" (ignored).
     * @return A [CaptionTrack] with empty cues.
     */
    override suspend fun generateCaptions(videoUrl: String): CaptionTrack {
        return CaptionTrack(
            src = "about:blank",
            format = "vtt",
            language = null,
            label = "No Captions"
        )
    }
}
