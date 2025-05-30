package com.thekindred.kindrededitor.ops

import com.thekindred.kindrededitor.model.EditorBlock
import com.thekindred.kindrededitor.model.enums.StyleFlag
import com.thekindred.kindrededitor.model.TextChunk

/**
 * Utility functions for programmatic editing of [EditorBlock] and [TextChunk] content.
 *
 * Intended for backend use, preprocessing, or editor-layer manipulation.
 */
object EditorOps {

    /**
     * Inserts a new block at the specified index in a post.
     *
     * @param blocks The current list of blocks.
     * @param block The block to insert.
     * @param index The target index (0 = prepend). If out of bounds, appends to the end.
     * @return A new list with the inserted block.
     */
    fun insertBlock(blocks: List<EditorBlock>, block: EditorBlock, index: Int): List<EditorBlock> {
        return when {
            index <= 0 -> listOf(block) + blocks
            index >= blocks.size -> blocks + block
            else -> blocks.subList(0, index) + block + blocks.subList(index, blocks.size)
        }
    }

    /**
     * Removes the block at the given index.
     *
     * @param blocks The block list.
     * @param index The index to remove.
     * @return A new list without the removed block. Returns unchanged list if index is invalid.
     */
    fun removeBlock(blocks: List<EditorBlock>, index: Int): List<EditorBlock> {
        return if (index in blocks.indices) {
            blocks.toMutableList().apply { removeAt(index) }
        } else blocks
    }

    /**
     * Toggles a style on a [TextChunk] and returns a new modified copy.
     *
     * @param chunk The original text chunk.
     * @param style The style to toggle.
     * @return A new chunk with the specified style flipped.
     */
    fun toggleStyle(chunk: TextChunk, style: StyleFlag): TextChunk {
        return when (style) {
            StyleFlag.Bold -> chunk.copy(bold = !chunk.bold)
            StyleFlag.Italic -> chunk.copy(italic = !chunk.italic)
            StyleFlag.Underline -> chunk.copy(underline = !chunk.underline)
            StyleFlag.Strikethrough -> chunk.copy(strikethrough = !chunk.strikethrough)
            StyleFlag.Highlight -> chunk.copy(highlight = !chunk.highlight)
        }
    }

    /**
     * Merges two [EditorBlock.Paragraph]s into one by appending chunks.
     *
     * @param a First paragraph
     * @param b Second paragraph
     * @return A new merged paragraph. If types don't match, returns null.
     */
    fun mergeParagraphs(a: EditorBlock, b: EditorBlock): EditorBlock.Paragraph? {
        return if (a is EditorBlock.Paragraph && b is EditorBlock.Paragraph) {
            EditorBlock.Paragraph(a.chunks + b.chunks)
        } else null
    }

    /**
     * Replaces a block at the given index with a new one.
     *
     * @param blocks The current list of blocks.
     * @param index The index to replace.
     * @param newBlock The block to insert.
     * @return A new list with the block replaced, or the original list if index is invalid.
     */
    fun replaceBlock(blocks: List<EditorBlock>, index: Int, newBlock: EditorBlock): List<EditorBlock> {
        return if (index in blocks.indices) {
            blocks.toMutableList().apply { set(index, newBlock) }
        } else blocks
    }

    /**
     * Splits a [Paragraph] block into two at the given chunk index.
     *
     * @param block The paragraph to split.
     * @param atIndex The index at which to split the chunk list.
     * @return A pair of new paragraphs (before, after) or null if invalid input.
     */
    fun splitParagraph(block: EditorBlock.Paragraph, atIndex: Int): Pair<EditorBlock.Paragraph, EditorBlock.Paragraph>? {
        return if (atIndex in 0..block.chunks.size) {
            val first = EditorBlock.Paragraph(block.chunks.subList(0, atIndex))
            val second = EditorBlock.Paragraph(block.chunks.subList(atIndex, block.chunks.size))
            first to second
        } else null
    }

    /**
     * Moves a block from one position to another in the list.
     *
     * @param blocks The current list of blocks.
     * @param fromIndex Source index.
     * @param toIndex Target index.
     * @return A new list with the block moved. Returns the original list if indices are invalid.
     */
    fun moveBlock(blocks: List<EditorBlock>, fromIndex: Int, toIndex: Int): List<EditorBlock> {
        if (fromIndex !in blocks.indices || toIndex !in blocks.indices || fromIndex == toIndex) return blocks

        val mutable = blocks.toMutableList()
        val block = mutable.removeAt(fromIndex)
        mutable.add(toIndex, block)
        return mutable
    }

}