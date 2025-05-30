package com.thekindred.kindrededitor.util

import com.thekindred.kindrededitor.model.PostChangeSummary
import com.thekindred.kindrededitor.model.PostDocument

/**
 * Compares two [PostDocument] instances and summarizes differences.
 */
object PostDiff {

    fun diff(old: PostDocument, new: PostDocument): PostChangeSummary {
        val oldBlocks = old.blocks
        val newBlocks = new.blocks

        val added = mutableListOf<Int>()
        val removed = mutableListOf<Int>()
        val modified = mutableListOf<Int>()

        val maxLength = maxOf(oldBlocks.size, newBlocks.size)

        for (i in 0 until maxLength) {
            when {
                i >= oldBlocks.size -> added.add(i)
                i >= newBlocks.size -> removed.add(i)
                oldBlocks[i] != newBlocks[i] -> modified.add(i)
            }
        }

        return PostChangeSummary(
            addedBlocks = added,
            removedBlocks = removed,
            modifiedBlocks = modified,
            hasSignificantChanges = added.isNotEmpty() || removed.isNotEmpty() || modified.isNotEmpty()
        )
    }
}