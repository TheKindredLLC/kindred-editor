package com.thekindred.kindrededitor.model

/**
 * Describes a single rendered element (block or inline).
 */
sealed class RenderElement {
    data class Paragraph(val chunks: List<InlineText>) : RenderElement()
    data class Quote(val text: String, val author: String?) : RenderElement()
    data class Image(val url: String, val alt: String?) : RenderElement()
    data class Video(val url: String, val caption: String?, val track: CaptionTrack?) : RenderElement()
    data class Code(val code: String, val language: String?) : RenderElement()
    data class Divider(val lengthPercent: Int, val thickness: Int, val color: String?) : RenderElement()
    data class Poll(val question: String, val options: List<String>, val allowsMultiple: Boolean) : RenderElement()
}