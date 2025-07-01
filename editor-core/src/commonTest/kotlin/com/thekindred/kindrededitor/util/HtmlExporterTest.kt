package com.thekindred.kindrededitor.util

import com.thekindred.kindrededitor.model.EditorBlock
import com.thekindred.kindrededitor.model.EmojiToken
import com.thekindred.kindrededitor.model.HtmlExportConfig
import com.thekindred.kindrededitor.model.MentionToken
import com.thekindred.kindrededitor.model.PostDocument
import com.thekindred.kindrededitor.model.TextChunk
import com.thekindred.kindrededitor.util.export.HtmlExporter
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class)
class HtmlExporterTest {

    @Test
    fun test_export_paragraphWithStyles_outputsCorrectHTML() {
        val block = EditorBlock.Paragraph(
            chunks = listOf(
                TextChunk("Bold", bold = true),
                TextChunk(" "),
                TextChunk("Italic", italic = true),
                TextChunk(" "),
                TextChunk("Normal")
            )
        )
        val html = HtmlExporter.export(PostDocument(listOf(block)))

        println("Rendered HTML:\n$html")

        assertTrue("<strong>Bold</strong>" in html)
        assertTrue("<em>Italic</em>" in html)
    }

    @Test
    fun test_export_imageBlock_outputsImgTag() {
        val block = EditorBlock.Image(url = "https://example.com/image.png", altText = "Sample")
        val html = HtmlExporter.export(PostDocument(listOf(block)))
        assertTrue("<img" in html && "src=\"https://example.com/image.png\"" in html)
    }

    @Test
    fun test_export_videoBlock_includesFigureAndCaption() {
        val block = EditorBlock.Video(
            url = "https://example.com/video.mp4",
            caption = "Watch this"
        )
        val html = HtmlExporter.export(PostDocument(listOf(block)))

        println("Rendered HTML:\n$html")

        assertTrue("<figure>" in html, "Expected <figure> wrapper")
        assertTrue("<video" in html, "Expected <video> tag")

        // No track tag expected since no CaptionTrack was provided

        assertTrue("<track" in html, "Expected <track> for captions")
        assertTrue("<figcaption>Watch this</figcaption>" in html, "Expected visible figcaption")
    }

    @Test
    fun test_export_poll_rendersOptionsCorrectly() {
        val block = EditorBlock.Poll(question = "Favorite color?", options = listOf("Red", "Blue"))
        val html = HtmlExporter.export(PostDocument(listOf(block)))
        assertTrue("Favorite color?" in html && "Red" in html && "Blue" in html)
    }

    @Test
    fun test_export_codeBlock_includesLanguageClass() {
        val block = EditorBlock.CodeBlock(code = "println(\"Hello\")", language = "kotlin")
        val html = HtmlExporter.export(PostDocument(listOf(block)))
        assertTrue("<code class=\"language-kotlin\">" in html)
    }

    @Test
    fun test_export_divider_hasCorrectStyle() {
        val block = EditorBlock.Divider(lengthPercent = 80, thickness = 2, color = "#ccc")
        val html = HtmlExporter.export(PostDocument(listOf(block)))
        assertTrue("<hr" in html && "width:80%" in html && "height:2px" in html && "background-color:#ccc" in html)
    }

    @Test
    fun test_escapeHtml_specialChars() {
        val block = EditorBlock.Paragraph(
            chunks = listOf(
                TextChunk("<script>alert('XSS')</script>")
            )
        )
        val html = HtmlExporter.export(PostDocument(listOf(block)))
        assertTrue("&lt;script&gt;" in html && "&lt;/script&gt;" in html)
    }

    @Test
    fun test_link_opensInNewTab_true_addsTargetBlank() {
        val chunk = TextChunk(text = "Click here", link = "https://example.com")
        val block = EditorBlock.Paragraph(listOf(chunk))
        val config = HtmlExportConfig(openLinksInNewTab = true)

        val html = HtmlExporter.export(PostDocument(listOf(block)), config)

        println("Rendered HTML:\n$html")
        assertTrue("<a href=\"https://example.com\" target=\"_blank\">" in html)
    }

    @Test
    fun test_link_opensInNewTab_false_omitsTarget() {
        val chunk = TextChunk(text = "Click here", link = "https://example.com")
        val block = EditorBlock.Paragraph(listOf(chunk))
        val config = HtmlExportConfig(openLinksInNewTab = false)

        val html = HtmlExporter.export(PostDocument(listOf(block)), config)

        println("Rendered HTML:\n$html")
        assertTrue("<a href=\"https://example.com\">" in html)
        assertFalse("target=\"_blank\"" in html)
    }

    @Test
    fun test_emojiAsUnicode_true_rendersShortcodeAsText() {
        val chunk = TextChunk(
            text = "Smile",
            emoji = EmojiToken(
                shortcode = ":smile:",
                name = "Smiling Face",
                url = "https://cdn.example.com/smile.png"
            )
        )
        val block = EditorBlock.Paragraph(listOf(chunk))
        val config = HtmlExportConfig(emojiAsUnicode = true)

        val html = HtmlExporter.export(PostDocument(listOf(block)), config)

        println("Rendered HTML:\n$html")
        assertTrue(":smile:" in html)
        assertFalse("<img" in html)
    }

    @Test
    fun test_emojiAsUnicode_false_rendersEmojiAsImage() {
        val chunk = TextChunk(
            text = "Smile",
            emoji = EmojiToken(
                shortcode = ":smile:",
                name = "Smiling Face",
                url = "https://cdn.example.com/smile.png"
            )
        )
        val block = EditorBlock.Paragraph(listOf(chunk))
        val config = HtmlExportConfig(emojiAsUnicode = false)

        val html = HtmlExporter.export(PostDocument(listOf(block)), config)

        println("Rendered HTML:\n$html")
        assertTrue("<img" in html)
        assertTrue("src=\"https://cdn.example.com/smile.png\"" in html)
    }

    @Test
    fun test_inlineStyles_true_rendersSpanWithStyles() {
        val chunk = TextChunk(
            text = "Styled Text",
            fontSize = 16,
            color = "#FF0000",
            highlight = true
        )
        val block = EditorBlock.Paragraph(listOf(chunk))
        val config = HtmlExportConfig(includeInlineStyles = true)

        val html = HtmlExporter.export(PostDocument(listOf(block)), config)

        println("Rendered HTML:\n$html")
        assertTrue("style=" in html)
        assertTrue("font-size:16px" in html)
        assertTrue("color:#FF0000" in html)
        assertTrue("background-color:yellow" in html)
    }

    @Test
    fun test_inlineStyles_false_doesNotRenderSpanStyle() {
        val chunk = TextChunk(
            text = "Styled Text",
            fontSize = 16,
            color = "#FF0000",
            highlight = true
        )
        val block = EditorBlock.Paragraph(listOf(chunk))
        val config = HtmlExportConfig(includeInlineStyles = false)

        val html = HtmlExporter.export(PostDocument(listOf(block)), config)

        println("Rendered HTML:\n$html")
        assertFalse("style=" in html)
        assertFalse("font-size" in html)
        assertFalse("color" in html)
        assertFalse("background-color" in html)
    }

    @Test
    fun test_underline_rendersU() {
        val chunk = TextChunk("Underlined", underline = true)
        val block = EditorBlock.Paragraph(listOf(chunk))
        val html = HtmlExporter.export(PostDocument(listOf(block)))

        println("Rendered HTML:\n$html")
        assertTrue("<u>Underlined</u>" in html)
    }

    @Test
    fun test_strikethrough_rendersS() {
        val chunk = TextChunk("Struck", strikethrough = true)
        val block = EditorBlock.Paragraph(listOf(chunk))
        val html = HtmlExporter.export(PostDocument(listOf(block)))

        println("Rendered HTML:\n$html")
        assertTrue("<s>Struck</s>" in html)
    }

    @Test
    fun test_mention_rendersSpanMention() {
        val chunk = TextChunk(
            text = "John",
            mention = MentionToken(userId = "123", displayName = "john_doe")
        )
        val block = EditorBlock.Paragraph(listOf(chunk))
        val html = HtmlExporter.export(PostDocument(listOf(block)))

        println("Rendered HTML:\n$html")
        assertTrue("""<span class="mention">@john_doe</span>""" in html)
    }

    @Test
    fun test_link_rendersAnchorTag() {
        val chunk = TextChunk(
            text = "Visit",
            link = "https://example.com"
        )
        val block = EditorBlock.Paragraph(listOf(chunk))
        val config = HtmlExportConfig(openLinksInNewTab = false)
        val html = HtmlExporter.export(PostDocument(listOf(block)), config)

        println("Rendered HTML:\n$html")
        assertTrue("""<a href="https://example.com">Visit</a>""" in html)
    }

    @Test
    fun test_fullStyleChunk_rendersAllTagsInOrder() {
        val chunk = TextChunk(
            text = "Styled",
            bold = true,
            italic = true,
            underline = true,
            strikethrough = true
        )
        val block = EditorBlock.Paragraph(listOf(chunk))
        val html = HtmlExporter.export(PostDocument(listOf(block)))

        println("Rendered HTML:\n$html")
        assertTrue("<strong>" in html)
        assertTrue("<em>" in html)
        assertTrue("<u>" in html)
        assertTrue("<s>" in html)
    }

    @Test
    fun test_emptyPostDocument_rendersEmptyString() {
        val html = HtmlExporter.export(PostDocument(emptyList()))
        println("Rendered HTML:\n$html")
        assertEquals("", html.trim())
    }

    @Test
    fun test_quoteWithEmptyTextAndNoAuthor_rendersEmptyBlockquote() {
        val block = EditorBlock.Quote(text = "", author = null)
        val html = HtmlExporter.export(PostDocument(listOf(block)))

        println("Rendered HTML:\n$html")
        assertTrue("<blockquote></blockquote>" in html)
    }

    @Test
    fun test_pollWithNoOptions_rendersEmptyList() {
        val block = EditorBlock.Poll(question = "Your favorite?", options = emptyList())
        val html = HtmlExporter.export(PostDocument(listOf(block)))

        println("Rendered HTML:\n$html")
        assertTrue("Your favorite?" in html)
        assertTrue("<ul></ul>" in html)
    }

    @Test
    fun test_mixedContent_rendersAllBlocks() {
        val blocks = listOf(
            EditorBlock.Paragraph(listOf(TextChunk("Hello ", bold = true))),
            EditorBlock.Quote("Be kind.", "Author"),
            EditorBlock.Image("https://img", "alt text"),
            EditorBlock.Video("https://vid.mp4", "a caption"),
            EditorBlock.Divider(),
            EditorBlock.CodeBlock("val x = 1", "kotlin"),
            EditorBlock.Poll("Pick one", listOf("A", "B", "C"))
        )

        val html = HtmlExporter.export(PostDocument(blocks))

        println("Rendered HTML:\n$html")

        assertTrue("<p>" in html)
        assertTrue("<blockquote>" in html)
        assertTrue("<img" in html)
        assertTrue("<video" in html)
        assertTrue("<hr" in html)
        assertTrue("<pre><code" in html)
        assertTrue("<div class=\"poll\">" in html)
    }

    @Test
    fun test_largePost_rendersSuccessfully() {
        val blocks = buildList {
            repeat(100) {
                add(EditorBlock.Paragraph(listOf(TextChunk("Line $it"))))
            }
        }

        val html = HtmlExporter.export(PostDocument(blocks))

        println("Rendered large HTML with ${blocks.size} blocks")
        assertTrue(html.count { it == '<' } >= 100) // crude sanity check
    }

}
