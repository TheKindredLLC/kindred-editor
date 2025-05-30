package com.thekindred.kindrededitor.util

import com.thekindred.kindrededitor.model.EditorBlock
import com.thekindred.kindrededitor.model.InlineText
import com.thekindred.kindrededitor.model.PostDocument
import com.thekindred.kindrededitor.model.RenderElement
import com.thekindred.kindrededitor.model.TextChunk

/**
 * Converts a [PostDocument] into a structured, platform-neutral list of [RenderElement]s.
 */
object RendererUtils {

    fun render(doc: PostDocument): List<RenderElement> {
        return doc.blocks.mapNotNull { block ->
            when (block) {
                is EditorBlock.Paragraph -> RenderElement.Paragraph(
                    block.chunks.map { it.toInline() }
                )
                is EditorBlock.Image -> RenderElement.Image(block.url, block.altText)
                is EditorBlock.Video -> RenderElement.Video(block.url, block.caption)
                is EditorBlock.CodeBlock -> RenderElement.Code(block.code, block.language)
                is EditorBlock.Quote -> RenderElement.Quote(block.text, block.author)
                is EditorBlock.Divider -> RenderElement.Divider(
                    block.lengthPercent, block.thickness, block.color
                )
                is EditorBlock.Poll -> RenderElement.Poll(
                    block.question, block.options, block.allowsMultipleAnswers
                )
            }
        }
    }

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