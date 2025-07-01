package com.thekindred.kindrededitor.model

import com.thekindred.kindrededitor.util.serialization.InstantSerializer
import kotlinx.serialization.Serializable
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@Serializable
data class BlockMetadata @OptIn(ExperimentalTime::class) constructor(
    @Serializable(with = InstantSerializer::class) val createdAt: Instant = Clock.System.now(),
    @Serializable(with = InstantSerializer::class) val modifiedAt: Instant = Clock.System.now()
)
