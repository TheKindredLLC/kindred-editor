package com.thekindred.kindrededitor.model

import com.thekindred.kindrededitor.model.enums.Alignment
import com.thekindred.kindrededitor.model.enums.ListType
import kotlinx.serialization.Serializable
import kotlin.time.ExperimentalTime
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

/**
 * Represents a single block element in a post editor document.
 * Each block corresponds to a distinct layout unit, such as a paragraph,
 * image, quote, or interactive element.
 */
@OptIn(ExperimentalUuidApi::class, ExperimentalTime::class)
@Serializable
sealed class EditorBlock {
    abstract val id: Uuid // Unique identifier
    open val metadata: BlockMetadata = BlockMetadata() // For tracking changes
    abstract val alignment: Alignment // Content alignment


    /**
     * A block representing a paragraph of rich text.
     * Uses [TextChunk] to support inline styling, links, and emoji.
     *
     * @param chunks The list of inline content chunks in this paragraph.
     */
    @Serializable
    data class Paragraph(
        val chunks: List<TextChunk> = emptyList(),
        override val id: Uuid = Uuid.random(),
        override val alignment: Alignment = Alignment.AUTO
    ) : EditorBlock()

    /**
     * A block representing an image.
     *
     * @param url The image URL or data URI.
     * @param altText Optional alternative text for screen readers or fallback.
     */
    @Serializable
    data class Image(
        val url: String,
        val altText: String? = null,
        override val id: Uuid = Uuid.random(),
        override val alignment: Alignment = Alignment.AUTO
    ) : EditorBlock()

    /**
     * A block representing an embedded video.
     *
     * @param url The video URL (e.g., YouTube, Vimeo, or direct MP4).
     * @param caption Optional text caption displayed below the video.
     */
    @Serializable
    data class Video(
        val url: String,
        val caption: String? = null,     // for figcaption
        val track: CaptionTrack? = null,
        override val id: Uuid = Uuid.random(),
        override val alignment: Alignment = Alignment.AUTO
    ) : EditorBlock()

    /**
     * A block representing a quoted section of text.
     *
     * @param text The quote body.
     * @param author Optional attribution or citation for the quote.
     */
    @Serializable
    data class Quote(
        val text: String,
        val author: String? = null,
        override val id: Uuid = Uuid.random(),
        override val alignment: Alignment = Alignment.AUTO
    ) : EditorBlock()

    /**
     * A block for displaying syntax-highlighted code.
     *
     * @param code The raw code content.
     * @param language Optional language identifier for syntax highlighting (e.g., "kotlin", "js").
     */
    @Serializable
    data class CodeBlock(
        val code: String,
        val language: String? = null,
        override val id: Uuid = Uuid.random(),
        override val alignment: Alignment = Alignment.AUTO
    ) : EditorBlock()

    /**
     * A visual horizontal divider used to separate sections of content.
     *
     * @param lengthPercent Horizontal span of the divider as a percentage of container width (0-100).
     * @param thickness Line thickness in pixels (or logical pixels depending on rendering context).
     * @param color Optional hex color code (e.g., "#CCCCCC"). Null to use theme default.
     */
    @Serializable
    data class Divider(
        val lengthPercent: Int = 100,
        val thickness: Int = 1,
        val color: String? = null,
        override val id: Uuid = Uuid.random(),
        override val alignment: Alignment = Alignment.AUTO
    ) : EditorBlock()

    /**
     * A poll block representing a question with selectable answer options.
     *
     * @param question The poll prompt.
     * @param options List of available answer options.
     * @param allowsMultipleAnswers Whether users can select more than one option.
     */
    @Serializable
    data class Poll(
        val question: String,
        val options: List<String>,
        val allowsMultipleAnswers: Boolean = false,
        override val id: Uuid = Uuid.random(),
        override val alignment: Alignment = Alignment.AUTO
    ) : EditorBlock()

    /**
     * A block representing a heading element, used for section titles or emphasis.
     *
     * @param id A unique identifier for the block.
     * @param level The heading level (1–6), where 1 represents the highest importance (e.g., H1).
     *              Determines the visual and semantic weight of the heading.
     * @param text The actual text content of the heading.
     * @param alignment Optional alignment of the heading text (e.g., LEFT, CENTER, RIGHT, AUTO).
     */
    @Serializable
    data class Heading(
        override val id: Uuid = Uuid.random(),
        val level: Int, // 1-6 for H1-H6
        val text: String,
        override val alignment: Alignment = Alignment.AUTO
    ) : EditorBlock()

    /**
     * A block representing an ordered or unordered list.
     *
     * @param id A unique identifier for the block.
     * @param items The list of items in this block, each of which may contain nested content.
     * @param type The type of list (UNORDERED for bullets, ORDERED for numbered).
     * @param alignment Optional alignment of the list relative to the container.
     */
    @Serializable
    data class ListBlock(
        override val id: Uuid = Uuid.random(),
        val items: List<ListItem>,
        val type: ListType = ListType.UNORDERED,
        override val alignment: Alignment = Alignment.AUTO
    ) : EditorBlock()

    /**
     * A block representing a tabular layout of rows and cells.
     *
     * @param id A unique identifier for the block.
     * @param rows The rows in the table, each consisting of one or more cells.
     * @param hasHeader Whether the first row should be treated as a table header.
     * @param alignment Optional alignment of the table as a whole within the document.
     */
    @Serializable
    data class Table(
        override val id: Uuid = Uuid.random(),
        val rows: List<TableRow>,
        val hasHeader: Boolean = true,
        override val alignment: Alignment = Alignment.AUTO
    ) : EditorBlock()

    /**
     * A block representing an external embedded resource (e.g., video, tweet, map).
     *
     * @param id A unique identifier for the block.
     * @param url The URL of the embedded content.
     * @param service The recognized embed service provider (e.g., YOUTUBE, TWITTER).
     * @param aspectRatio Optional width-to-height ratio for maintaining media proportions.
     * @param caption Optional description or annotation displayed beneath the embed.
     * @param alignment Optional alignment of the embed container within the document.
     */
    @Serializable
    data class Embed(
        override val id: Uuid = Uuid.random(),
        val url: String,
        val service: EmbedService,
        val aspectRatio: Double? = null,
        val caption: String? = null,
        override val alignment: Alignment = Alignment.AUTO
    ) : EditorBlock()

}
