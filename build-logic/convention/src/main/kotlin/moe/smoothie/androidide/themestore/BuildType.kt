package moe.smoothie.androidide.themestore.convention

/**
 * This is shared between :app and :benchmarks module to provide configurations type safety.
 */
enum class BuildType(val applicationIdSuffix: String? = null) {
    DEBUG(".debug"),
    RELEASE,
}
