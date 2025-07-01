package com.thekindred.kindrededitor.util

import com.thekindred.kindrededitor.model.*
import com.thekindred.kindrededitor.model.enums.Alignment
import com.thekindred.kindrededitor.util.text.InputNormalizer
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue
import kotlin.uuid.ExperimentalUuidApi


@OptIn(ExperimentalUuidApi::class)
class InputNormalizerTest {

    private val config = NormalizationConfig(removeEmptyBlocks = true, allowEmptyParagraphs = false)
    private val permissiveConfig = NormalizationConfig(removeEmptyBlocks = false, allowEmptyParagraphs = true)

        @Test
    fun test_emptyParagraph_removed() {
        val doc = PostDocument(listOf(EditorBlock.Paragraph(chunks = listOf(), alignment = Alignment.START)))
        val result = InputNormalizer.normalizeDocument(doc, config)
        assertEquals(0, result.blocks.size)
    }

    @Test
    fun test_paragraph_with_text_kept() {
        val doc = PostDocument(listOf(
            EditorBlock.Paragraph(
                chunks = listOf(TextChunk(" Hello ")),
                alignment = Alignment.START
            )
        ))
        val result = InputNormalizer.normalizeDocument(doc, config)
        assertEquals(1, result.blocks.size)
        val para = result.blocks.first() as EditorBlock.Paragraph
        assertEquals(" Hello ", para.chunks.first().text)
    }

    @Test
    fun test_chunk_merging() {
        val doc = PostDocument(listOf(
            EditorBlock.Paragraph(
                chunks = listOf(
                    TextChunk("Hello", bold = true),
                    TextChunk(" ", bold = true),
                    TextChunk("World", bold = true)
                ),
                alignment = Alignment.START
            )
        ))
        val result = InputNormalizer.normalizeDocument(doc, config)
        val para = result.blocks.first() as EditorBlock.Paragraph
        assertEquals(1, para.chunks.size)
        assertEquals("Hello World", para.chunks.first().text)
    }

    @Test
    fun test_quote_blank_text_removed() {
        val block = EditorBlock.Quote(text = "   ", alignment = Alignment.START)
        val result = InputNormalizer.normalizeBlock(block, config)
        assertNull(result)
    }

    @Test
    fun test_image_with_url_kept() {
        val block = EditorBlock.Image(url = "https://example.com", alignment = Alignment.CENTER)
        val result = InputNormalizer.normalizeBlock(block, config)
        assertNotNull(result)
    }

    @Test
    fun test_video_with_blank_url_removed() {
        val block = EditorBlock.Video(url = "   ", alignment = Alignment.START)
        val result = InputNormalizer.normalizeBlock(block, config)
        assertNull(result)
    }

    @Test
    fun test_code_trimmed() {
        val block = EditorBlock.CodeBlock(code = "  println(\"Hello\")  ", language = "kotlin", alignment = Alignment.START)
        val result = InputNormalizer.normalizeBlock(block, config) as EditorBlock.CodeBlock
        assertEquals("println(\"Hello\")", result.code)
    }

    @Test
    fun test_poll_trim_and_filter_options() {
        val block = EditorBlock.Poll(
            question = "  What's your favorite color? ",
            options = listOf("Blue", " ", "Red"),
            alignment = Alignment.CENTER
        )
        val result = InputNormalizer.normalizeBlock(block, config) as EditorBlock.Poll
        assertEquals("What's your favorite color?", result.question)
        assertEquals(listOf("Blue", "Red"), result.options)
    }

    @Test
    fun test_heading_trimmed_and_kept() {
        val block = EditorBlock.Heading(level = 2, text = "  Welcome!  ", alignment = Alignment.CENTER)
        val result = InputNormalizer.normalizeBlock(block, config) as EditorBlock.Heading
        assertEquals("Welcome!", result.text)
    }

    @Test
    fun test_listBlock_keeps_items_with_content() {
        val block = EditorBlock.ListBlock(
            items = listOf(
                ListItem(
                    chunks = listOf(TextChunk("Keep me")),
                    subItems = listOf(
                        ListItem(chunks = listOf(TextChunk("SubItem")))
                    )
                ),
                ListItem(
                    chunks = listOf(TextChunk("Keep me too")),
                    subItems = emptyList()
                )
            ),
            alignment = Alignment.START
        )

        val result = InputNormalizer.normalizeBlock(block, config) as EditorBlock.ListBlock

        // Both items should remain since they both have non-empty chunks
        assertEquals(2, result.items.size)

        // Verify first item
        val firstItem = result.items[0]
        assertEquals(1, firstItem.chunks.size)
        assertEquals("Keep me", firstItem.chunks.first().text)
        assertEquals(1, firstItem.subItems.size)

        // Verify second item
        val secondItem = result.items[1]
        assertEquals(1, secondItem.chunks.size)
        assertEquals("Keep me too", secondItem.chunks.first().text)
        assertTrue(secondItem.subItems.isEmpty())
    }

    @Test
    fun test_listBlock_filters_empty_items() {
        val block = EditorBlock.ListBlock(
            items = listOf(
                ListItem(
                    chunks = listOf(TextChunk("Keep me")),
                    subItems = emptyList()
                ),
                ListItem(
                    chunks = emptyList(),
                    subItems = emptyList()
                )
            ),
            alignment = Alignment.START
        )

        val result = InputNormalizer.normalizeBlock(block, config) as EditorBlock.ListBlock

        // Only the item with non-empty chunks should remain
        assertEquals(1, result.items.size)
        val remaining = result.items.first()
        assertEquals(1, remaining.chunks.size)
        assertEquals("Keep me", remaining.chunks.first().text)
    }

    @Test
    fun test_table_with_rows_kept() {
        val row = TableRow(
            cells = listOf(
                TableCell(chunks = listOf(TextChunk("Header"))),
                TableCell(chunks = listOf(TextChunk("Value")))
            )
        )

        val block = EditorBlock.Table(
            rows = listOf(row),
            hasHeader = true,
            alignment = Alignment.CENTER
        )

        val result = InputNormalizer.normalizeBlock(block, config)
        assertNotNull(result)
    }

    @Test
    fun test_embed_with_blank_url_removed() {
        val block = EditorBlock.Embed(
            url = " ",
            service = EmbedService.YOUTUBE,
            alignment = Alignment.START
        )
        val result = InputNormalizer.normalizeBlock(block, config)
        assertNull(result)
    }

    @Test
    fun test_document_with_mixed_blocks_normalized() {
        val doc = PostDocument(listOf(
            EditorBlock.Paragraph(chunks = listOf(), alignment = Alignment.START),
            EditorBlock.Image(url = "https://example.com/image.png", alignment = Alignment.CENTER),
            EditorBlock.Quote(text = "   ", alignment = Alignment.END),
            EditorBlock.CodeBlock(code = "fun main() {}", alignment = Alignment.START)
        ))
        val result = InputNormalizer.normalizeDocument(doc, config)
        assertEquals(2, result.blocks.size)
        assertTrue(result.blocks.any { it is EditorBlock.Image })
        assertTrue(result.blocks.any { it is EditorBlock.CodeBlock })
    }

    @Test
    fun test_normalizeChunks_directly() {
        val chunks = listOf(
            TextChunk("Hello", bold = true),
            TextChunk(" ", bold = true),
            TextChunk("World", bold = true),
            TextChunk("", bold = false), // Empty chunk should be removed
            TextChunk("!", bold = true)
        )

        val result = InputNormalizer.normalizeChunks(chunks)
        assertEquals(1, result.size)
        assertEquals("Hello World!", result[0].text)
    }

    @Test
    fun test_styled_whitespace_chunks_kept() {
        // Test that whitespace-only chunks with styling are preserved
        val chunks = listOf(
            TextChunk("Hello", bold = false),
            TextChunk(" ", bold = true), // Styled space should be kept as separate chunk
            TextChunk("World", bold = false)
        )

        val result = InputNormalizer.normalizeChunks(chunks)
        assertEquals(3, result.size)
        assertEquals("Hello", result[0].text)
        assertEquals(" ", result[1].text)
        assertTrue(result[1].bold)
        assertEquals("World", result[2].text)
    }

    @Test
    fun test_divider_block_kept() {
        val block = EditorBlock.Divider(alignment = Alignment.CENTER)
        val result = InputNormalizer.normalizeBlock(block, config)
        assertNotNull(result)
        assertTrue(result is EditorBlock.Divider)
    }

    @Test
    fun test_empty_image_with_permissive_config() {
        val block = EditorBlock.Image(url = "", alignment = Alignment.START)

        // With strict config, should be removed
        val strictResult = InputNormalizer.normalizeBlock(block, config)
        assertNull(strictResult)

        // With permissive config, should be kept
        val permissiveResult = InputNormalizer.normalizeBlock(block, permissiveConfig)
        assertNotNull(permissiveResult)
    }

    @Test
    fun test_empty_poll_behavior() {
        // Poll with blank question and non-empty options list (even if options are blank)
        // should be kept according to InputNormalizer.kt line 89
        val blockWithBlankOptions = EditorBlock.Poll(
            question = "   ",
            options = listOf(" ", "  "),
            alignment = Alignment.START
        )
        val resultWithBlankOptions = InputNormalizer.normalizeBlock(blockWithBlankOptions, config)
        assertNotNull(resultWithBlankOptions)

        // Poll with blank question and truly empty options list should be removed
        val blockWithEmptyOptions = EditorBlock.Poll(
            question = "   ",
            options = emptyList(),
            alignment = Alignment.START
        )
        val resultWithEmptyOptions = InputNormalizer.normalizeBlock(blockWithEmptyOptions, config)
        assertNull(resultWithEmptyOptions)
    }

    @Test
    fun test_empty_table_removed() {
        val block = EditorBlock.Table(
            rows = emptyList(),
            hasHeader = false,
            alignment = Alignment.START
        )
        val result = InputNormalizer.normalizeBlock(block, config)
        assertNull(result)
    }

    @Test
    fun test_empty_paragraph_kept_with_permissive_config() {
        val doc = PostDocument(listOf(
            EditorBlock.Paragraph(chunks = listOf(), alignment = Alignment.START)
        ))

        // With strict config, should be removed
        val strictResult = InputNormalizer.normalizeDocument(doc, config)
        assertEquals(0, strictResult.blocks.size)

        // With permissive config, should be kept
        val permissiveResult = InputNormalizer.normalizeDocument(doc, permissiveConfig)
        assertEquals(1, permissiveResult.blocks.size)
        assertTrue(permissiveResult.blocks.first() is EditorBlock.Paragraph)
    }

    @Test
    fun test_chunks_with_different_styles_not_merged() {
        val chunks = listOf(
            TextChunk("Bold", bold = true),
            TextChunk("NotBold", bold = false),
            TextChunk("Italic", italic = true)
        )

        val result = InputNormalizer.normalizeChunks(chunks)
        assertEquals(3, result.size)
        assertEquals("Bold", result[0].text)
        assertEquals("NotBold", result[1].text)
        assertEquals("Italic", result[2].text)
    }
}
