package com.thekindred.kindrededitor.util

/**
 * A utility for identifying and extracting mention-like tokens from text.
 *
 * This is a simplified parser that looks for @mentions using a fixed regex pattern.
 */
object MentionParser {

    private val mentionRegex = Regex("@(\\w{2,32})")

    /**
     * Extracts all potential usernames mentioned in a string.
     *
     * @param input Raw input text
     * @return A list of lowercase usernames (without @)
     */
    fun extractMentionUsernames(input: String): List<String> {
        return mentionRegex.findAll(input).mapNotNull { match ->
            match.groups[1]?.value?.lowercase()
        }.toList()
    }
}