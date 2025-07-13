package com.ailtontech.gymlife

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform