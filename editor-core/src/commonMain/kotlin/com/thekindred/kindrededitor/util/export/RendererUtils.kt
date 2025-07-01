package com.thekindred.kindrededitor.util.export

import com.thekindred.kindrededitor.model.EditorBlock
import com.thekindred.kindrededitor.model.InlineText
import com.thekindred.kindrededitor.model.PostDocument
import com.thekindred.kindrededitor.model.RenderElement
import com.thekindred.kindrededitor.model.TextChunk

/**
 * Utility object responsible for transforming a [PostDocument] into a list of [RenderElement]s
 * that can be consumed by the rendering layer (e.g., HTML export, Compose UI, etc.).
 *
 * Each [EditorBlock] is converted into a corresponding [RenderElement] based on its content type.
 */
object RendererUtils {

    fun render(doc: PostDocument): List<RenderElement> {
        return doc.blocks.map { block ->
            when (block) {
                is EditorBlock.Paragraph -> RenderElement.Paragraph(
                    block.chunks.map { it.toInline() }
                )
                is EditorBlock.Image -> RenderElement.Image(block.url, block.altText)
                is EditorBlock.Video -> RenderElement.Video(block.url, block.caption, block.track)
                is EditorBlock.CodeBlock -> RenderElement.Code(block.code, block.language)
                is EditorBlock.Quote -> RenderElement.Quote(block.text, block.author)
                is EditorBlock.Divider -> RenderElement.Divider(
                    block.lengthPercent, block.thickness, block.color
                )
                is EditorBlock.Poll -> RenderElement.Poll(
                    block.question, block.options, block.allowsMultipleAnswers
                )

                is EditorBlock.Heading -> RenderElement.Paragraph( // You may want a dedicated Heading type
                    listOf(
                        InlineText(
                            text = block.text,
                            bold = true,
                            fontSize = when (block.level) {
                                1 -> 32
                                2 -> 28
                                3 -> 24
                                4 -> 20
                                5 -> 18
                                else -> 16
                            }
                        )
                    )
                )

                is EditorBlock.ListBlock -> {
                    val items = block.items.map { item ->
                        item.chunks.map { it.toInline() }
                    }
                    RenderElement.ListType(items)
                }

                is EditorBlock.Table -> RenderElement.Table(
                    rows = block.rows.map { row ->
                        row.cells.map { cell ->
                            cell.chunks.map { it.toInline() }
                        }
                    },
                    hasHeader = block.hasHeader
                )

                is EditorBlock.Embed -> RenderElement.Embed(
                    url = block.url,
                    service = block.service,
                    caption = block.caption
                )
            }
        }
    }

    /**
     * Converts a [TextChunk] into an [InlineText] representation suitable for rendering.
     * Preserves style attributes like bold, italic, link, emoji, and font metadata.
     *
     * @receiver The original [TextChunk] with raw inline styling.
     * @return A render-friendly [InlineText] equivalent of the input chunk.
     */
    private fun TextChunk.toInline(): InlineText {
        return InlineText(
            text = text,
            bold = bold,
            italic = italic,
            underline = underline,
            strikethrough = strikethrough,
            highlight = highlight,
            color = color,
            fontSize = fontSize,
            link = link,
            mention = mention,
            emoji = emoji
        )
    }
}
