package com.thekindred.kindrededitor.util.analysis

import com.thekindred.kindrededitor.model.EditorBlock
import com.thekindred.kindrededitor.model.PostDocument
import com.thekindred.kindrededitor.model.PostSummary

/**
 * Utilities to extract preview information from a [PostDocument].
 */
object ContentSummary {

    /**
     * Generates a content preview of the given document.
     *
     * @param document The post content.
     * @param title Optional explicit title if stored separately.
     */
    fun generate(document: PostDocument, title: String? = null): PostSummary {
        var snippet: String? = null
        var imageUrl: String? = null
        var videoUrl: String? = null
        var containsPoll = false

        for (block in document.blocks) {
            when (block) {
                is EditorBlock.Paragraph -> {
                    if (snippet == null) {
                        val raw = block.chunks.joinToString(" ") { it.text }.trim()
                        if (raw.isNotEmpty()) {
                            snippet = raw.take(160)
                        }
                    }
                }
                is EditorBlock.Image -> if (imageUrl == null) imageUrl = block.url
                is EditorBlock.Video -> if (videoUrl == null) videoUrl = block.url
                is EditorBlock.Poll -> containsPoll = true
                else -> {} // Skip Quote, Code, Divider
            }

            // Exit early if we've gathered enough
            if (snippet != null && imageUrl != null && videoUrl != null && containsPoll) break
        }

        return PostSummary(
            title = title,
            plainTextSnippet = snippet ?: "",
            firstImageUrl = imageUrl,
            firstVideoUrl = videoUrl,
            containsPoll = containsPoll
        )
    }
}
