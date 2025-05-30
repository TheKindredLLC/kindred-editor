package com.thekindred.kindrededitor.state

import com.thekindred.kindrededitor.model.EditorBlock
import com.thekindred.kindrededitor.model.PostDocument
import com.thekindred.kindrededitor.ops.EditorOps

/**
 * Represents an in-memory editing session with undo/redo and block-level editing operations.
 *
 * Intended for use in real-time editing tools or collaborative editors.
 */
class EditorSession(initial: PostDocument) {

    private val history = UndoStack<PostDocument>().apply {
        push(initial)
    }

    /**
     * The current state of the document.
     */
    val document: PostDocument
        get() = history.current ?: PostDocument()

    /**
     * Updates the document and tracks it in undo history.
     */
    fun update(newDoc: PostDocument) {
        history.push(newDoc)
    }

    /**
     * Undoes the last change. Returns the new state if successful.
     */
    fun undo(): PostDocument? = history.undo()

    /**
     * Redoes a previously undone change.
     */
    fun redo(): PostDocument? = history.redo()

    /**
     * Adds a block at the given index.
     */
    fun insertBlock(block: EditorBlock, at: Int): PostDocument {
        val updated = document.copy(
            blocks = EditorOps.insertBlock(document.blocks, block, at)
        )
        update(updated)
        return updated
    }

    /**
     * Removes a block at the given index.
     */
    fun removeBlock(at: Int): PostDocument {
        val updated = document.copy(
            blocks = EditorOps.removeBlock(document.blocks, at)
        )
        update(updated)
        return updated
    }

    /**
     * Replaces a block at the given index.
     */
    fun replaceBlock(at: Int, block: EditorBlock): PostDocument {
        val updated = document.copy(
            blocks = EditorOps.replaceBlock(document.blocks, at, block)
        )
        update(updated)
        return updated
    }
}