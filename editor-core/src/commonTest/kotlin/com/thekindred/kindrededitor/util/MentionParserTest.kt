package com.thekindred.kindrededitor.util

import com.thekindred.kindrededitor.util.text.MentionParser
import kotlin.test.Test
import kotlin.test.assertEquals

class MentionParserTest {

    @Test
    fun testExtractValidMentions() {
        val text = "Hey @alice and @bob32, check this out!"
        val mentions = MentionParser.extractMentionUsernames(text)
        assertEquals(listOf("alice", "bob32"), mentions)
    }

    @Test
    fun testNoMentions() {
        val mentions = MentionParser.extractMentionUsernames("No tags here!")
        assertEquals(emptyList(), mentions)
    }

    @Test
    fun testMalformedMentions() {
        val text = "Invalid: @, @@, @."
        val mentions = MentionParser.extractMentionUsernames(text)
        assertEquals(emptyList(), mentions)
    }
}
