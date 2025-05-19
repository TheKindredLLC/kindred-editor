package com.thekindred.kindrededitor

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform