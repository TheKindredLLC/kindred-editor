package com.thekindred.kindrededitor.model

import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

/**
 * Holds contextual information about the environment in which a command is executed.
 * This may include selection data, timestamps, active modes, or feature flags.
 *
 * Command implementations can use this context to adapt their behavior based on
 * current user input or editor state without tightly coupling to UI concerns.
 *
 * This context object is **immutable**. It should be recreated whenever state changes,
 * ensuring that commands remain pure and composable.
 *
 * @property document The current editable [PostDocument] the command will operate on.
 * @property selection The current user selection within the document, if applicable.
 * @property timestamp The time at which the command was issued (UTC).
 * @property metadata Optional map for attaching additional contextual data.
 */
data class CommandContext @OptIn(ExperimentalTime::class) constructor(
    val document: PostDocument,
    val selection: SelectionState? = null,
    val timestamp: Instant = Clock.System.now(),
    val metadata: Map<String, Any?> = emptyMap()
)