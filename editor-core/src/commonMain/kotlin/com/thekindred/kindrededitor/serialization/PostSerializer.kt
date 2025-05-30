package com.thekindred.kindrededitor.serialization

import com.thekindred.kindrededitor.model.PostDocument
import kotlinx.serialization.json.Json


/**
 * A utility for serializing and deserializing [PostDocument] instances to and from JSON.
 *
 * This uses a preconfigured [Json] instance to ensure consistent formatting,
 * including support for default values, unknown field tolerance, and compact output.
 */
object PostSerializer {

    /**
     * The shared JSON instance used for all post (de)serialization.
     *
     * This configuration:
     * - Enables pretty printing (optional for debugging)
     * - Preserves default values
     * - Ignores unknown fields (for forward compatibility)
     */
    val json = Json {
        prettyPrint = true
        encodeDefaults = true
        ignoreUnknownKeys = true
    }

    /**
     * Serializes a [PostDocument] to a JSON string.
     *
     * @param post The post to serialize.
     * @return A JSON-formatted string representing the post.
     */
    fun encode(post: PostDocument): String = json.encodeToString(post)

    /**
     * Deserializes a JSON string into a [PostDocument].
     *
     * @param jsonString The JSON input string.
     * @return The deserialized post object.
     * @throws kotlinx.serialization.SerializationException if input is invalid.
     */
    fun decode(jsonString: String): PostDocument = json.decodeFromString(jsonString)
}