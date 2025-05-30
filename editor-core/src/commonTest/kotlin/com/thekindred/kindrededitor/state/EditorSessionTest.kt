package com.thekindred.kindrededitor.state

import com.thekindred.kindrededitor.model.EditorBlock
import com.thekindred.kindrededitor.model.PostDocument
import com.thekindred.kindrededitor.model.TextChunk
import kotlin.test.Test
import kotlin.test.assertEquals

class EditorSessionTest {

    @Test
    fun testInsertAndUndo() {
        val initial = PostDocument()
        val session = EditorSession(initial)

        val para = EditorBlock.Paragraph(listOf(TextChunk("New")))
        session.insertBlock(para, 0)
        assertEquals(1, session.document.blocks.size)

        session.undo()
        assertEquals(0, session.document.blocks.size)
    }

    @Test
    fun testReplaceBlock() {
        val para = EditorBlock.Paragraph(listOf(TextChunk("Old")))
        val doc = PostDocument(blocks = listOf(para))
        val session = EditorSession(doc)

        val newPara = EditorBlock.Paragraph(listOf(TextChunk("Updated")))
        session.replaceBlock(0, newPara)
        assertEquals("Updated", (session.document.blocks[0] as EditorBlock.Paragraph).chunks[0].text)
    }
}
