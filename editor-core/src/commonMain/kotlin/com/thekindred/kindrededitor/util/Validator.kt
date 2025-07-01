package com.thekindred.kindrededitor.util

import com.thekindred.kindrededitor.model.EditorBlock
import com.thekindred.kindrededitor.model.PostDocument
import com.thekindred.kindrededitor.model.ValidationConfig
import com.thekindred.kindrededitor.model.ValidationError

/**
 * Validates a post document before publishing or displaying.
 */
object Validator {

    /**
     * Returns a list of problems with this post. If the list is empty, the post is valid.
     */
fun validate(
        document: PostDocument,
        config: ValidationConfig = ValidationConfig.Default
    ): List<ValidationError> {
        val errors = mutableListOf<ValidationError>()

        // ───── Global Constraints ─────

        if (config.maxBlocks != null && document.blocks.size > config.maxBlocks) {
            errors.add(
                ValidationError(
                    message = "Too many blocks (${document.blocks.size} > ${config.maxBlocks})",
                    blockIndex = -1,
                    userMessage = "This post is too long. Please shorten it."
                )
            )
        }

        if (config.maxChars != null) {
            val totalChars = document.blocks.sumOf {
                if (it is EditorBlock.Paragraph) it.chunks.sumOf { chunk -> chunk.text.length } else 0
            }
            if (totalChars > config.maxChars) {
                errors.add(
                    ValidationError(
                        message = "Post exceeds max character count ($totalChars > ${config.maxChars})",
                        blockIndex = -1,
                        userMessage = "Your post is too long. Try trimming it down."
                    )
                )
            }
        }

        var imageCount = 0
        var videoCount = 0
        var mentionCount = 0
        var linkCount = 0

        // ───── Per-Block Validation ─────

        document.blocks.forEachIndexed { index, block ->
            when (block) {
                is EditorBlock.Paragraph -> {
                    if (!config.allowEmptyParagraphs) {
                        val isBlank = block.chunks.all {
                            it.text.isBlank() && it.emoji == null && it.mention == null
                        }
                        if (isBlank) {
                            errors.add(
                                ValidationError(
                                    message = "Empty paragraph",
                                    blockIndex = index,
                                    userMessage = "You left a paragraph empty. Please write something or remove it."
                                )
                            )
                        }
                    }

                    mentionCount += block.chunks.count { it.mention != null }
                    linkCount += block.chunks.count { it.link != null }
                }

                is EditorBlock.Image -> {
                    if (block.url.isBlank()) {
                        errors.add(
                            ValidationError(
                                message = "Image block missing URL",
                                blockIndex = index,
                                userMessage = "One of your images is missing a link."
                            )
                        )
                    }
                    imageCount++
                }

                is EditorBlock.Video -> {
                    if (block.url.isBlank()) {
                        errors.add(
                            ValidationError(
                                message = "Video block missing URL",
                                blockIndex = index,
                                userMessage = "One of your videos is missing a link."
                            )
                        )
                    }
                    videoCount++
                }

                is EditorBlock.Poll -> {
                    if (block.options.size < 2) {
                        errors.add(
                            ValidationError(
                                message = "Poll has fewer than 2 options",
                                blockIndex = index,
                                userMessage = "Polls must have at least 2 answer choices."
                            )
                        )
                    }
                }

                is EditorBlock.CodeBlock -> {
                    if (block.code.isBlank()) {
                        errors.add(
                            ValidationError(
                                message = "Code block is empty",
                                blockIndex = index,
                                userMessage = "You added a code block but didn’t include any code."
                            )
                        )
                    }
                }

                is EditorBlock.Quote -> {
                    // No validation required
                }

                is EditorBlock.Divider -> {
                    // No validation required
                }
            }

            if (block::class in config.disallowedBlockTypes) {
                errors.add(
                    ValidationError(
                        message = "Block type not allowed: ${block::class.simpleName}",
                        blockIndex = index,
                        userMessage = "This type of content isn’t allowed in your post."
                    )
                )
            }
        }

        // ───── Aggregate Media & Mention Limits ─────

        if (config.maxImages != null && imageCount > config.maxImages) {
            errors.add(
                ValidationError(
                    message = "Too many images ($imageCount > ${config.maxImages})",
                    blockIndex = -1,
                    userMessage = "You’ve added too many images."
                )
            )
        }

        if (config.maxVideos != null && videoCount > config.maxVideos) {
            errors.add(
                ValidationError(
                    message = "Too many videos ($videoCount > ${config.maxVideos})",
                    blockIndex = -1,
                    userMessage = "Only ${config.maxVideos} video(s) allowed per post."
                )
            )
        }

        if (config.maxMentions != null && mentionCount > config.maxMentions) {
            errors.add(
                ValidationError(
                    message = "Too many mentions ($mentionCount > ${config.maxMentions})",
                    blockIndex = -1,
                    userMessage = "You’ve mentioned too many people."
                )
            )
        }

        if (config.maxLinks != null && linkCount > config.maxLinks) {
            errors.add(
                ValidationError(
                    message = "Too many links ($linkCount > ${config.maxLinks})",
                    blockIndex = -1,
                    userMessage = "You’ve included too many links."
                )
            )
        }

        return errors
    }
}