package com.thekindred.kindrededitor.state

/**
 * A stack-based undo/redo history for any serializable editing state.
 *
 * @param T The type being tracked (e.g., [PostDocument]).
 * @param maxDepth Optional cap on undo history size (default: 50).
 */
class UndoStack<T>(private val maxDepth: Int = 50) {

    private val undo = ArrayDeque<T>()
    private val redo = ArrayDeque<T>()

    var current: T? = null
        private set

    /**
     * Records a new state. Clears redo history.
     */
    fun push(newState: T) {
        current?.let {
            undo.addLast(it)
            if (undo.size > maxDepth) undo.removeFirst()
        }
        redo.clear()
        current = newState
    }

    /**
     * Undoes the last state change.
     * Moves the current state to redo and restores previous.
     */
    fun undo(): T? {
        if (undo.isEmpty()) return null
        redo.addLast(current!!)
        current = undo.removeLast()
        return current
    }

    /**
     * Redoes the most recently undone state.
     */
    fun redo(): T? {
        if (redo.isEmpty()) return null
        undo.addLast(current!!)
        current = redo.removeLast()
        return current
    }

    fun canUndo(): Boolean = undo.isNotEmpty()
    fun canRedo(): Boolean = redo.isNotEmpty()
}