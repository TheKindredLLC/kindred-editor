package com.thekindred.kindrededitor.util.text

import com.thekindred.kindrededitor.model.EditorBlock
import com.thekindred.kindrededitor.model.NormalizationConfig
import com.thekindred.kindrededitor.model.PostDocument
import com.thekindred.kindrededitor.model.TextChunk
import kotlin.uuid.ExperimentalUuidApi

/**
 * Provides normalization and cleanup utilities for user-generated content
 * represented by a [PostDocument].
 *
 * This component ensures that:
 * - Redundant or empty blocks are removed based on configurable rules.
 * - Inline text chunks are merged and trimmed to avoid unnecessary duplication.
 * - Structural integrity is maintained across all block types.
 *
 * Use this before saving, exporting, or processing a document to ensure
 * consistent and valid content.
 */
object InputNormalizer {

    /**
     * Normalizes all [EditorBlock]s in the given [PostDocument] using the provided [config].
     *
     * @param document The post document to normalize.
     * @param config Controls which empty or redundant elements are removed or retained.
     * @return A new [PostDocument] with normalized block content.
     */
    fun normalizeDocument(
        document: PostDocument,
        config: NormalizationConfig
    ): PostDocument {
        val normalizedBlocks = document.blocks
            .mapNotNull { normalizeBlock(it, config) }

        return document.copy(blocks = normalizedBlocks)
    }

    /**
     * Normalizes a single [EditorBlock] by trimming, filtering, and cleaning its internal content.
     *
     * Depending on the block type, this may:
     * - Remove the block entirely if it is empty and the config disallows it.
     * - Trim whitespace from text fields.
     * - Filter out invalid or empty elements within the block (e.g., list items, poll options).
     * - Merge adjacent [TextChunk]s with matching styles in paragraph blocks, including preserving
     *   intentional spacing when applicable (e.g., non-empty `" "` chunks).
     *
     * @param block The editor block to normalize.
     * @param config The normalization rules to apply.
     * @return The normalized block, or null if it should be removed.
     */
    @OptIn(ExperimentalUuidApi::class)
    fun normalizeBlock(
        block: EditorBlock,
        config: NormalizationConfig
    ): EditorBlock? {
        return when (block) {
            is EditorBlock.Paragraph -> {
                val cleaned = normalizeChunks(block.chunks)
                if (cleaned.isEmpty() && !config.allowEmptyParagraphs) null
                else block.copy(chunks = cleaned)
            }

            is EditorBlock.Quote -> {
                if (block.text.isBlank() && config.removeEmptyBlocks) null
                else block.copy(text = block.text.trim())
            }

            is EditorBlock.Image -> {
                if (block.url.isBlank() && config.removeEmptyBlocks) null
                else block
            }

            is EditorBlock.Video -> {
                if (block.url.isBlank() && config.removeEmptyBlocks) null
                else block
            }

            is EditorBlock.CodeBlock -> {
                if (block.code.isBlank() && config.removeEmptyBlocks) null
                else block.copy(code = block.code.trim())
            }

            is EditorBlock.Divider -> block

            is EditorBlock.Poll -> {
                if (block.question.isBlank() && block.options.isEmpty() && config.removeEmptyBlocks) {
                    null
                } else {
                    block.copy(
                        question = block.question.trim(),
                        options = block.options.map { it.trim() }.filter { it.isNotEmpty() }
                    )
                }
            }

            is EditorBlock.Heading -> {
                if (block.text.isBlank() && config.removeEmptyBlocks) null
                else block.copy(text = block.text.trim())
            }

            is EditorBlock.ListBlock -> {
                val cleanedItems = block.items.filter { it.chunks.isNotEmpty() || it.subItems.isNotEmpty() }
                if (cleanedItems.isEmpty() && config.removeEmptyBlocks) null
                else block.copy(items = cleanedItems)
            }

            is EditorBlock.Table -> {
                if (block.rows.isEmpty() && config.removeEmptyBlocks) null
                else block
            }

            is EditorBlock.Embed -> {
                if (block.url.isBlank() && config.removeEmptyBlocks) null
                else block
            }
        }
    }

    /**
     * Cleans and merges inline [TextChunk]s by:
     * - Removing chunks with only whitespace.
     * - Merging consecutive chunks with identical styles and metadata.
     *
     * @param chunks A list of inline text chunks to normalize.
     * @return A list of cleaned and merged chunks.
     */
    fun normalizeChunks(
        chunks: List<TextChunk>
    ): List<TextChunk> {
    val cleaned = mutableListOf<TextChunk>()

    for (chunk in chunks) {
        // Keep styled whitespace chunks (like bold space)
        val isWhitespaceOnly = chunk.text.isNotEmpty() && chunk.text.isBlank()
        val shouldKeep = chunk.text.isNotEmpty() && (!chunk.text.isBlank() || hasStyle(chunk))

        if (!shouldKeep) continue

        val last = cleaned.lastOrNull()
        if (last != null && last.mergeableWith(chunk)) {
            // Merge adjacent chunks with matching style
            cleaned[cleaned.lastIndex] = last.copy(text = last.text + chunk.text)
        } else {
            cleaned.add(chunk)
        }
    }

    return cleaned
    }


    private fun hasStyle(chunk: TextChunk): Boolean {
        return chunk.bold || chunk.italic || chunk.underline || chunk.strikethrough ||
               chunk.highlight || chunk.color != null || chunk.fontSize != null ||
               chunk.link != null || chunk.mention != null || chunk.emoji != null
    }

    /**
     * Compares two [TextChunk]s to determine if they have matching style and metadata
     * and could be safely merged into a single chunk.
     *
     * @receiver The first text chunk.
     * @param other The chunk to compare with.
     * @return True if the two chunks are mergeable; false otherwise.
     */
    private fun TextChunk.mergeableWith(other: TextChunk): Boolean {
        return this.bold == other.bold &&
            this.italic == other.italic &&
            this.underline == other.underline &&
            this.strikethrough == other.strikethrough &&
            this.link == other.link &&
            this.mention == other.mention &&
            this.emoji == other.emoji &&
            this.fontSize == other.fontSize &&
            this.color == other.color &&
            this.highlight == other.highlight
    }
}
