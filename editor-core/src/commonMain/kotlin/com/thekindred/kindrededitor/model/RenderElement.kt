package com.thekindred.kindrededitor.model

sealed class RenderElement {

    /**
     * Rich text paragraph composed of inline styled text.
     */
    data class Paragraph(val chunks: List<InlineText>) : RenderElement()

    /**
     * A standalone heading, semantically distinct from a paragraph.
     *
     * @param level Heading level (1–6)
     * @param text The visible heading content
     */
    data class Heading(val level: Int, val text: String) : RenderElement()

    /**
     * Quoted text with optional author attribution.
     */
    data class Quote(val text: String, val author: String?) : RenderElement()

    /**
     * An image with optional alternative text.
     */
    data class Image(val url: String, val alt: String?) : RenderElement()

    /**
     * A video element with optional caption and closed caption track.
     */
    data class Video(val url: String, val caption: String?, val track: CaptionTrack?) : RenderElement()

    /**
     * Block of code with optional language identifier for syntax highlighting.
     */
    data class Code(val code: String, val language: String?) : RenderElement()

    /**
     * A horizontal rule used for visual separation.
     */
    data class Divider(val lengthPercent: Int, val thickness: Int, val color: String?) : RenderElement()

    /**
     * A poll with question and answer options.
     */
    data class Poll(val question: String, val options: List<String>, val allowsMultiple: Boolean) : RenderElement()

    /**
     * A bullet list (unordered or ordered), where each item is a styled line of text.
     */
    data class ListType(val items: List<List<InlineText>>, val ordered: Boolean = false) : RenderElement()

    /**
     * A simple table structure with rows of cells. Each cell contains styled text.
     *
     * @param rows The table's rows, each a list of cell contents.
     * @param hasHeader Whether the first row is a header.
     */
    data class Table(val rows: List<List<List<InlineText>>>, val hasHeader: Boolean) : RenderElement()

    /**
     * An embedded external media element like YouTube, Twitter, etc.
     */
    data class Embed(val url: String, val service: EmbedService, val caption: String?) : RenderElement()
}

