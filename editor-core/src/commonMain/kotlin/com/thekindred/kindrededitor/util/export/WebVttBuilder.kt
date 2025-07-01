package com.thekindred.kindrededitor.util.export

/**
 * Utility to programmatically build a WebVTT (Web Video Text Tracks) subtitle file.
 * See: https://developer.mozilla.org/en-US/docs/Web/API/WebVTT_API
 */
class WebVttBuilder {

    val cues = mutableListOf<Cue>()

    /**
     * Adds a single caption cue to the VTT file.
     * @param start Timestamp in `HH:MM:SS.mmm` format (e.g., "00:00:01.000")
     * @param end   Timestamp in `HH:MM:SS.mmm` format (e.g., "00:00:04.000")
     * @param text  The subtitle/caption text to display during this interval.
     */
    fun addCue(start: String, end: String, text: String): WebVttBuilder {
        cues.add(Cue(start, end, text))
        return this
    }

    /**
     * Builds the final WebVTT string.
     */
    fun build(): String = buildString {
        appendLine("WEBVTT\n")
        cues.forEach { cue ->
            appendLine("${cue.start} --> ${cue.end}")
            appendLine(cue.text.trim())
            appendLine() // Blank line between cues
        }
    }

    data class Cue(val start: String, val end: String, val text: String)
}
