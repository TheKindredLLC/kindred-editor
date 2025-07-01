package com.thekindred.kindrededitor.util

import com.thekindred.kindrededitor.model.HtmlExportConfig
import com.thekindred.kindrededitor.model.InlineText
import com.thekindred.kindrededitor.model.PostDocument
import com.thekindred.kindrededitor.model.RenderElement
import com.thekindred.kindrededitor.util.RendererUtils.render


/**
 * Converts a [PostDocument] into basic HTML using the given [HtmlExportConfig].
 */
object HtmlExporter {

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

                        // ✅ Only add <track> if explicitly requested and vttUrl is provided
                        if (config.renderClosedCaptions && element.track?.format.equals("vtt", ignoreCase = true)) {
                            append("""<track kind="captions" src="${element.track?.src}" label="captions" default />""")
                        }



                        append("</video>")

                        // ✅ Figcaption is for visual context, separate from closed captions
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
                }
            }
        }
    }

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
            html += " ${chunk.emoji.shortcode}" // Or load actual character from a registry if needed
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

    private fun escapeHtml(text: String): String {
        return text
            .replace("&", "&amp;")
            .replace("<", "&lt;")
            .replace(">", "&gt;")
    }
}