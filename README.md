# Kindred Editor

**A portable, extensible, and cross-platform post editor model built for Kotlin Multiplatform.**

`kindred-editor` provides a rich, structured foundation for composing and rendering posts across social, blogging, and community platforms. It is designed to be completely UI-agnostic, render-consistent, and easy to integrate into Kotlin applications on Web, Android, iOS, or Desktop.

> **Note:** This project intentionally does not include any UI components. The UI implementation is the responsibility of the consuming project that implements this module.

---

## ✨ Features

- 🧱 **Composable Block-Based Structure**  
  Supports paragraphs, headings, images, videos, polls, code blocks, and more.

- 🎨 **Inline Rich Text Styling**  
  Bold, italic, underline, color, highlight, emojis, mentions.

- 🧑‍🤝‍🧑 **Mentions, Tags, and Reactions** (future-ready)

- 🌐 **Cross-Platform Consistency**  
  Serialize once, render consistently across platforms.

- 📤 **Media Embeds and Uploads**  
  Designed to handle image and video uploads, embeds, and alt text.

- ⚙️ **Modular Architecture**  
  Decoupled rendering layers for Kobweb, Compose Multiplatform, or your own frontend.

- ♿ **Accessibility-Aware**  
  Structured with future accessibility and screen reader support in mind.

---

## 📦 Module Overview

| Module                     | Purpose                                                                |
|----------------------------|------------------------------------------------------------------------|
| `editor-core`              | Shared model & business logic (EditorBlock, TextChunk, etc.) - UI-free |

---

## 🔧 Example Usage

> Usage will depend on the rendering layer you implement.
> Example (model only):

```kotlin
val post = listOf(
    EditorBlock.Paragraph(
        listOf(
            TextChunk("Hello "),
            TextChunk(
                elements = listOf(EmojiToken(":wave:", "wave", "/emojis/wave.svg"))
            ),
            TextChunk(" world!")
        )
    ),
    EditorBlock.Image(
        url = "https://cdn.example.com/hero.png",
        altText = "Hero image"
    )
)
```
---

## 📦 Installation

> Publishing to Maven Central coming soon.

Until then, you can include this library via a local Gradle module or Git submodule.

```kotlin
// settings.gradle.kts
include(":kindred-editor")

// Or, if using Git submodule:
git submodule add https://github.com/TheKindred/kindred-editor.git
```

---

## 🤝 Contributing

We welcome issues, feedback, and PRs!
If you'd like to help shape the direction of this project — especially around accessibility, internationalization, or editor UX — feel free to open a discussion.

To contribute:

1. Fork the repository

2. Create a feature branch

3. Open a pull request with a clear description

---

## 🛡 License

This project is licensed under the **MIT License**.

```text
MIT License

Copyright (c) 2025 The Kindred

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
