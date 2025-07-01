package com.thekindred.kindrededitor.interfaces

import com.thekindred.kindrededitor.model.PostDocument

/**
 * Represents an atomic, reversible transformation on a [PostDocument].
 *
 * Commands are the primary mechanism by which content in the editor is mutated.
 * Each command defines both an `apply()` and `undo()` method to support reversible operations.
 *
 * This interface is designed for integration with an undo/redo stack and event dispatching system.
 *
 * Example use cases include:
 * - Inserting a new block
 * - Updating a block's text or alignment
 * - Merging or splitting blocks
 * - Deleting a selection
 *
 * Implementations should ensure their transformations are:
 * - **Deterministic**: Same input yields same result
 * - **Pure**: Do not modify the input document (copy on write)
 * - **Composable**: Can be chained with other commands
 *
 * @property description A short human-readable description of the command, useful for debugging or tooling.
 */
interface EditorCommand {

    /**
     * Applies the transformation represented by this command to the given [PostDocument].
     *
     * @param document The document to apply the command to.
     * @return A new [PostDocument] instance with the transformation applied.
     */
    fun apply(document: PostDocument): PostDocument

    /**
     * Reverses the transformation previously applied by this command.
     *
     * This should return the document to its prior state before `apply()` was called.
     * If the command was never applied, `undo()` must still be safe and produce a valid state.
     *
     * @param document The document to revert.
     * @return A new [PostDocument] instance with the command undone.
     */
    fun undo(document: PostDocument): PostDocument

    /**
     * A descriptive label or explanation of what this command does.
     * Useful for logging, debugging, or showing in the UI (e.g. command history or undo tooltips).
     */
    val description: String
}
