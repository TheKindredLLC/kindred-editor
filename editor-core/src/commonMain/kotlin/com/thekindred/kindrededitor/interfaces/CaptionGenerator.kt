package com.thekindred.kindrededitor.interfaces

import com.thekindred.kindrededitor.model.CaptionTrack

/**
 * Defines a contract for generating [CaptionTrack] data from a video source.
 *
 * This interface is used to support AI-assisted captioning in the KindredEditor
 * without binding the core library to any specific machine learning model or vendor.
 *
 * Consumers of this library are expected
 * to implement this interface using the caption generation approach that best fits
 * their platform and infrastructure:
 *
 * - OpenAI Whisper (local or hosted)
 * - Google Cloud Speech-to-Text
 * - AWS Transcribe
 * - Deepgram, AssemblyAI, or other commercial APIs
 *
 * Usage:
 * - A video file or stream is uploaded or referenced via [videoUrl]
 * - The implementation extracts audio and runs STT processing
 * - The result is converted into a [CaptionTrack] containing timed caption cues
 *
 * This interface may be invoked from the post editor UI to allow users to
 * auto-generate and optionally edit captions before saving.
 */
interface CaptionGenerator {

    /**
     * Generate a structured [CaptionTrack] for a given video URL.
     *
     * @param videoUrl The URL or path to a video file (must be accessible to the underlying model).
     * @return A [CaptionTrack] containing a list of caption cues with timestamps and text.
     * @throws Exception if caption generation fails (e.g., network, model, or media issues).
     */
    suspend fun generateCaptions(videoUrl: String): CaptionTrack
}