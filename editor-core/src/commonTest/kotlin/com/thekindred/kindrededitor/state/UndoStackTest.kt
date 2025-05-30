package com.thekindred.kindrededitor.state

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class UndoStackTest {

    @Test
    fun testPushAndUndo() {
        val stack = UndoStack<String>()
        stack.push("A")
        stack.push("B")
        assertEquals("B", stack.current)

        stack.undo()
        assertEquals("A", stack.current)
    }

    @Test
    fun testRedo() {
        val stack = UndoStack<String>()
        stack.push("A")
        stack.push("B")
        stack.undo()
        stack.redo()
        assertEquals("B", stack.current)
    }

    @Test
    fun testNoUndoWhenEmpty() {
        val stack = UndoStack<String>()
        assertNull(stack.undo())
    }

    @Test
    fun testNoRedoWhenEmpty() {
        val stack = UndoStack<String>()
        assertNull(stack.redo())
    }
}