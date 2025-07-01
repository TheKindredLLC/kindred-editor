package com.thekindred.kindrededitor.model.enums

import kotlinx.serialization.Serializable

/**
 * Text alignment modes for editor blocks.
 * - Use [AUTO] to follow platform or locale directionality (start for LTR, end for RTL).
 * - Use [LEFT], [CENTER], or [RIGHT] to explicitly align content.
 * - Use [JUSTIFY] for full-width alignment when supported.
 */
@Serializable
enum class Alignment {
    AUTO,
    START,
    END,
    LEFT,
    CENTER,
    RIGHT,
    JUSTIFY
}