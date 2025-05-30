package com.thekindred.kindrededitor.ops

import com.thekindred.kindrededitor.model.EditorBlock
import com.thekindred.kindrededitor.model.enums.StyleFlag
import com.thekindred.kindrededitor.model.TextChunk
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class EditorOpsTest {

    @Test
    fun testInsertBlock() {
        val blocks = listOf(EditorBlock.Paragraph(listOf(TextChunk("A"))))
        val newBlock = EditorBlock.Paragraph(listOf(TextChunk("B")))
        val result = EditorOps.insertBlock(blocks, newBlock, 1)

        assertEquals(2, result.size)
        assertEquals("B", (result[1] as EditorBlock.Paragraph).chunks[0].text)
    }

    @Test
    fun testRemoveBlock() {
        val blocks = listOf(
            EditorBlock.Paragraph(listOf(TextChunk("A"))),
            EditorBlock.Paragraph(listOf(TextChunk("B")))
        )
        val result = EditorOps.removeBlock(blocks, 0)
        assertEquals(1, result.size)
        assertEquals("B", (result[0] as EditorBlock.Paragraph).chunks[0].text)
    }

    @Test
    fun testReplaceBlock() {
        val blocks = listOf(EditorBlock.Paragraph(listOf(TextChunk("Old"))))
        val replacement = EditorBlock.Paragraph(listOf(TextChunk("New")))
        val result = EditorOps.replaceBlock(blocks, 0, replacement)

        assertEquals("New", (result[0] as EditorBlock.Paragraph).chunks[0].text)
    }

    @Test
    fun testToggleStyle() {
        val original = TextChunk("text", bold = false)
        val toggled = EditorOps.toggleStyle(original, StyleFlag.Bold)
        assertTrue(toggled.bold)
    }

    @Test
    fun testMergeParagraphs() {
        val a = EditorBlock.Paragraph(listOf(TextChunk("A")))
        val b = EditorBlock.Paragraph(listOf(TextChunk("B")))
        val merged = EditorOps.mergeParagraphs(a, b)

        assertNotNull(merged)
        assertEquals(2, merged.chunks.size)
    }

    @Test
    fun testSplitParagraph() {
        val para = EditorBlock.Paragraph(listOf(TextChunk("One"), TextChunk("Two")))
        val (first, second) = EditorOps.splitParagraph(para, 1)!!

        assertEquals(1, first.chunks.size)
        assertEquals("Two", second.chunks[0].text)
    }

    @Test
    fun testMoveBlock() {
        val a = EditorBlock.Paragraph(listOf(TextChunk("A")))
        val b = EditorBlock.Paragraph(listOf(TextChunk("B")))
        val blocks = listOf(a, b)
        val result = EditorOps.moveBlock(blocks, 0, 1)

        assertEquals("B", (result[0] as EditorBlock.Paragraph).chunks[0].text)
    }
}