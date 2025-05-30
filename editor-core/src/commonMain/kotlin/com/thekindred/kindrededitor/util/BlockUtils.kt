package com.thekindred.kindrededitor.util

import com.thekindred.kindrededitor.model.EditorBlock
import com.thekindred.kindrededitor.model.TextChunk

/**
 * Utility functions for working with [EditorBlock] instances.
 */
object BlockUtils {

    /**
     * Checks whether the given [EditorBlock.Paragraph] is effectively empty.
     *
     * A paragraph is considered empty if it contains no chunks or only chunks with blank text.
     *
     * @param paragraph The paragraph block to check.
     * @return `true` if the paragraph has no meaningful content, `false` otherwise.
     */
    fun isEmpty(paragraph: EditorBlock.Paragraph): Boolean {
        return paragraph.chunks.all { it.text.isBlank() }
    }

    /**
     * Extracts plain text from a [TextChunk] list, ignoring all formatting.
     *
     * @param chunks A list of styled text chunks.
     * @return A single concatenated plain text string.
     */
    fun flattenText(chunks: List<TextChunk>): String {
        return chunks.joinToString("") { it.text }
    }

    /**
     * Checks whether a block can be merged with another of the same type.
     *
     * Only [Paragraph] blocks are currently considered mergeable.
     *
     * @param a First block
     * @param b Second block
     * @return `true` if the two blocks can be merged, `false` otherwise
     */
    fun isMergeable(a: EditorBlock, b: EditorBlock): Boolean {
        return a is EditorBlock.Paragraph && b is EditorBlock.Paragraph
    }
}