package com.thekindred.kindrededitor.model

import kotlinx.serialization.Serializable

@Serializable
data class TableRow(
    val cells: List<TableCell>
)
