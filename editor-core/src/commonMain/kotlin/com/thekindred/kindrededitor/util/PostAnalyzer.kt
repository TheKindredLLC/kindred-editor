package com.thekindred.kindrededitor.util

import com.thekindred.kindrededitor.model.EditorBlock
import com.thekindred.kindrededitor.model.PostDocument
import com.thekindred.kindrededitor.model.PostMetrics

/**
 * Provides analytical insight into post content.
 */
object PostAnalyzer {

    fun analyze(document: PostDocument): PostMetrics {
        var totalWords = 0
        var totalChars = 0
        val blockCount = document.blocks.size
        var imageCount = 0
        var videoCount = 0
        var mentionCount = 0
        var emojiCount = 0
        var linkCount = 0
        var hasPoll = false
        var hasCode = false
        var hasQuote = false

        for (block in document.blocks) {
            when (block) {
                is EditorBlock.Paragraph -> {
                    for (chunk in block.chunks) {
                        totalChars += chunk.text.length
                        totalWords += chunk.text.split(Regex("\\s+")).count { it.isNotBlank() }

                        if (chunk.mention != null) mentionCount++
                        if (chunk.emoji != null) emojiCount++
                        if (chunk.link != null) linkCount++
                    }
                }

                is EditorBlock.Image -> imageCount++
                is EditorBlock.Video -> videoCount++
                is EditorBlock.Poll -> hasPoll = true
                is EditorBlock.CodeBlock -> hasCode = true
                is EditorBlock.Quote -> hasQuote = true
                is EditorBlock.Divider -> {}
            }
        }

        return PostMetrics(
            totalWords = totalWords,
            totalChars = totalChars,
            blockCount = blockCount,
            imageCount = imageCount,
            videoCount = videoCount,
            mentionCount = mentionCount,
            emojiCount = emojiCount,
            linkCount = linkCount,
            hasPoll = hasPoll,
            hasCode = hasCode,
            hasQuote = hasQuote
        )
    }
}