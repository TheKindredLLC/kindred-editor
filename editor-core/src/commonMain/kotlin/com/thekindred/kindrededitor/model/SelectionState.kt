package com.thekindred.kindrededitor.model

import kotlinx.serialization.Serializable

/**
 * Represents the current selection or caret position in a document.
 *
 * This structure supports both collapsed selections (a single caret position)
 * and expanded ranges (multi-block or multi-chunk selections).
 *
 * All positions are **block-aware**, and can optionally include inline chunk indices
 * (e.g., for text blocks like paragraphs or headings).
 *
 * This model is **immutable** — updates should be applied via new instances.
 *
 * @property anchor The starting position of the selection.
 * @property focus The ending position of the selection.
 * @property isCollapsed True if anchor and focus are identical (caret only).
 */
@Serializable
data class SelectionState(
    val anchor: SelectionPosition,
    val focus: SelectionPosition
) {
    /**
     * Returns true if the selection does not span any range (caret only).
     */
    val isCollapsed: Boolean
        get() = anchor == focus
}

