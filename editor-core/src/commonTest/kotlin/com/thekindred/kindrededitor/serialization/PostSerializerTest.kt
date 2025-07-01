package com.thekindred.kindrededitor.serialization

import com.thekindred.kindrededitor.model.EditorBlock
import com.thekindred.kindrededitor.model.PostDocument
import com.thekindred.kindrededitor.model.TextChunk
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class)
class PostSerializerTest {

    @Test
    fun testRoundTripSerialization() {
        val post = PostDocument(
            blocks = listOf(
                EditorBlock.Paragraph(
                    chunks = listOf(
                        TextChunk(text = "Hello", bold = true),
                        TextChunk(text = " world", italic = true)
                    )
                ),
                EditorBlock.Divider(lengthPercent = 50, thickness = 2, color = "#000000")
            ),
            metadata = mapOf("tags" to "test")
        )

        val json = PostSerializer.encode(post)
        val restored = PostSerializer.decode(json)

        assertEquals(post, restored, "Deserialized post should match original")
    }

    @Test
    fun testEmptyPostSerialization() {
        val post = PostDocument()
        val json = PostSerializer.encode(post)
        val parsed = PostSerializer.decode(json)

        assertEquals(post, parsed, "Empty posts should round-trip safely")
    }

    @Test
    fun testInvalidJsonFailsDeserialization() {
        val invalidJson = """{ "blocks": "not an array" }"""

        assertFailsWith<Exception> {
            PostSerializer.decode(invalidJson)
        }
    }
}