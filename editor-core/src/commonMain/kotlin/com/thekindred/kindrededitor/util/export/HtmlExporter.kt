package com.thekindred.kindrededitor.util.export

import com.thekindred.kindrededitor.model.HtmlExportConfig
import com.thekindred.kindrededitor.model.InlineText
import com.thekindred.kindrededitor.model.PostDocument
import com.thekindred.kindrededitor.model.RenderElement
import com.thekindred.kindrededitor.util.export.RendererUtils.render


/**
 * Converts a [PostDocument] into HTML based on the defined [HtmlExportConfig].
 *
 * This exporter transforms a structured [PostDocument] composed of [RenderElement]s
 * into semantic HTML. The result can be used for rendering posts on the web,
 * storing as HTML content, or sharing externally.
 *
 * Supported blocks include:
 * - Paragraphs with rich inline formatting
 * - Quotes with optional attribution
 * - Images with alt text
 * - Videos with optional captions and caption tracks (e.g. VTT)
 * - Syntax-highlighted code blocks
 * - Horizontal dividers with inline styles
 * - Poll questions with multiple options
 *
 * Export behavior can be modified using [HtmlExportConfig] flags.
 */
object HtmlExporter {

    /**
     * Converts a [PostDocument] to a complete HTML string using the provided [HtmlExportConfig].
     *
     * @param post The [PostDocument] to convert.
     * @param config Configuration that controls rendering options (e.g. inline styles, captions).
     * @return HTML string representing the rendered post.
     */
    fun export(post: PostDocument, config: HtmlExportConfig = HtmlExportConfig.Default): String {
        return buildString {
            render(post).forEach { element ->
                when (element) {
                    is RenderElement.Paragraph -> {
                        append("<p>")
                        element.chunks.forEach { chunk ->
                            append(applyStyles(chunk, config))
                        }
                        append("</p>\n")
                    }

                    is RenderElement.Quote -> {
                        append("<blockquote>")
                        append(escapeHtml(element.text))
                        element.author?.let {
                            append("<footer>— ${escapeHtml(it)}</footer>")
                        }
                        append("</blockquote>\n")
                    }

                    is RenderElement.Image -> {
                        append("<img src=\"${element.url}\"")
                        element.alt?.let { append(" alt=\"${escapeHtml(it)}\"") }
                        append(" />\n")
                    }

                    is RenderElement.Video -> {
                        append("<figure>")
                        append("<video controls src=\"${element.url}\">")

                        // ✅ Only add <track> if explicitly requested and src is provided
                        val track = element.track
                        if (
                            config.renderClosedCaptions &&
                            track != null &&
                            track.format.equals("vtt", ignoreCase = true) &&
                            track.src.isNotBlank()
                        ) {
                            append("""<track kind="captions" src="${track.src}" label="${track.label ?: "captions"}" default />""")
                        }

                        append("</video>")

                        // ✅ Figcaption is for visible context below the video, separate from subtitles
                        if (!element.caption.isNullOrBlank()) {
                            append("<figcaption>${escapeHtml(element.caption)}</figcaption>")
                        }

                        append("</figure>\n")
                    }

                    is RenderElement.Code -> {
                        append("<pre><code")
                        element.language?.let { append(" class=\"language-${it}\"") }
                        append(">")
                        append(escapeHtml(element.code))
                        append("</code></pre>\n")
                    }

                    is RenderElement.Divider -> {
                        val style = if (config.includeInlineStyles)
                            " style=\"width:${element.lengthPercent}%; height:${element.thickness}px;" +
                                    (element.color?.let { " background-color:$it;" } ?: "") + "\""
                        else ""
                        append("<hr$style />\n")
                    }

                    is RenderElement.Poll -> {
                        append("<div class=\"poll\"><strong>${escapeHtml(element.question)}</strong><ul>")
                        element.options.forEach { option ->
                            append("<li>${escapeHtml(option)}</li>")
                        }
                        append("</ul></div>\n")
                    }

                    is RenderElement.Embed -> {
                        append("<div class=\"embed\">")
                        append("""<iframe src="${element.url}" frameborder="0"""")
                        append(" allowfullscreen></iframe>")
                        if (!element.caption.isNullOrBlank()) {
                            append("<p class=\"embed-caption\">${escapeHtml(element.caption)}</p>")
                        }
                        append("</div>\n")
                    }

                    is RenderElement.Heading -> {
                        val level = element.level.coerceIn(1, 6)
                        append("<h$level>${escapeHtml(element.text)}</h$level>\n")
                    }

                    is RenderElement.ListType -> {
                        val tag = if (element.ordered) "ol" else "ul"
                        append("<$tag>")
                        for (item in element.items) {
                            append("<li>")
                            item.forEach { chunk ->
                                append(applyStyles(chunk, config))
                            }
                            append("</li>")
                        }
                        append("</$tag>\n")
                    }

                    is RenderElement.Table -> {
                        append("<table>")
                        element.rows.forEachIndexed { rowIndex, row ->
                            append("<tr>")
                            row.forEach { cellChunks ->
                                val tag = if (element.hasHeader && rowIndex == 0) "th" else "td"
                                append("<$tag>")
                                cellChunks.forEach { chunk ->
                                    append(applyStyles(chunk, config))
                                }
                                append("</$tag>")
                            }
                            append("</tr>")
                        }
                        append("</table>\n")
                    }
                }
            }
        }
    }

    /**
     * Applies inline HTML tags and styles to the given [InlineText] chunk.
     *
     * Handles rich text styling such as bold, italic, underline, strikethrough,
     * links, mentions, emoji rendering, and inline styles like font size and color.
     *
     * @param chunk The text chunk to style.
     * @param config Controls how emoji and styles are rendered.
     * @return A string of HTML representing the styled chunk.
     */
    private fun applyStyles(chunk: InlineText, config: HtmlExportConfig): String {
        var html = escapeHtml(chunk.text)

        if (chunk.bold) html = "<strong>$html</strong>"
        if (chunk.italic) html = "<em>$html</em>"
        if (chunk.underline) html = "<u>$html</u>"
        if (chunk.strikethrough) html = "<s>$html</s>"

        if (chunk.link != null) {
            val target = if (config.openLinksInNewTab) " target=\"_blank\"" else ""
            html = "<a href=\"${chunk.link}\"$target>$html</a>"
        }

        if (chunk.mention != null) {
            html = "<span class=\"mention\">@${escapeHtml(chunk.mention.displayName)}</span>"
        }

        if (chunk.emoji != null && config.emojiAsUnicode) {
            html += " ${chunk.emoji.shortcode}" // Unicode or plain text shortcode
        } else if (chunk.emoji != null) {
            html += " <img class=\"emoji\" src=\"${chunk.emoji.url}\" alt=\"${chunk.emoji.shortcode}\" />"
        }

        if (config.includeInlineStyles && (chunk.fontSize != null || chunk.color != null || chunk.highlight)) {
            val styles = mutableListOf<String>()
            chunk.fontSize?.let { styles.add("font-size:${it}px") }
            chunk.color?.let { styles.add("color:$it") }
            if (chunk.highlight) styles.add("background-color:yellow")

            html = "<span style=\"${styles.joinToString(";")}\">$html</span>"
        }

        return html
    }

    /**
     * Escapes raw text for safe HTML rendering.
     *
     * Replaces `<`, `>`, and `&` with their corresponding HTML entities.
     *
     * @param text The raw text to escape.
     * @return Escaped HTML-safe text.
     */
    private fun escapeHtml(text: String): String {
        return text
            .replace("&", "&amp;")
            .replace("<", "&lt;")
            .replace(">", "&gt;")
    }
}
