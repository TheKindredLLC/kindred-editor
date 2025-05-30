package com.thekindred.kindrededitor.util

import com.thekindred.kindrededitor.model.CaptionTrack

fun CaptionTrack.toVtt(): String {
    return WebVttBuilder().apply {
        cues.forEach { addCue(it.start, it.end, it.text) }
    }.build()
}
