package com.thekindred.kindrededitor.util

import com.thekindred.kindrededitor.util.media.EmojiRegistry
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class EmojiRegistryTest {

    @Test
    fun testGetKnownEmoji() {
        val emoji = EmojiRegistry.get(":smile:")
        assertEquals(":smile:", emoji?.shortcode)
        assertEquals("Smiling Face", emoji?.name)
    }

    @Test
    fun testGetUnknownEmoji() {
        val emoji = EmojiRegistry.get(":notarealemoji:")
        assertNull(emoji)
    }

    @Test
    fun testEmojiListIncludesBasics() {
        val all = EmojiRegistry.all().map { it.shortcode }
        assertTrue(all.contains(":smile:"))
    }
}
