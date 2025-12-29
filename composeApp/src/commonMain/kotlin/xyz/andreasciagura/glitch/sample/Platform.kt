package xyz.andreasciagura.glitch.sample

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform