package com.thekindred.kindrededitor.util

import com.thekindred.kindrededitor.model.EditorBlock
import com.thekindred.kindrededitor.model.TextChunk
import com.thekindred.kindrededitor.util.text.BlockUtils
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class)
class BlockUtilsTest {

    @Test
    fun testIsEmpty() {
        val empty = EditorBlock.Paragraph()
        val whitespace = EditorBlock.Paragraph(listOf(TextChunk("   ")))
        val nonEmpty = EditorBlock.Paragraph(listOf(TextChunk("text")))

        assertTrue(BlockUtils.isEmpty(empty))
        assertTrue(BlockUtils.isEmpty(whitespace))
        assertFalse(BlockUtils.isEmpty(nonEmpty))
    }

    @Test
    fun testFlattenText() {
        val chunks = listOf(
            TextChunk("One "),
            TextChunk("Two"),
            TextChunk(" Three")
        )
        val result = BlockUtils.flattenText(chunks)
        assertEquals("One Two Three", result)
    }

    @Test
    fun testIsMergeable() {
        val p1 = EditorBlock.Paragraph()
        val p2 = EditorBlock.Paragraph()
        val quote = EditorBlock.Quote("Quote")

        assertTrue(BlockUtils.isMergeable(p1, p2))
        assertFalse(BlockUtils.isMergeable(p1, quote))
    }
}
