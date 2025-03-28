import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin)
    id("kotlin-parcelize")
//    id("androidx.navigation.safeargs.kotlin")
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
//    id("com.google.dagger.hilt.android")
//    kotlin("kapt")
//    kotlin("jvm")
    alias(libs.plugins.aboutlibraries)
}

android {
    namespace = "moe.smoothie.androidide.themestore"
    compileSdk = 34

    androidResources {
        @Suppress("UnstableApiUsage")
        generateLocaleConfig = true
    }

    defaultConfig {
        applicationId = "moe.smoothie.androidide.themestore"

        vectorDrawables.useSupportLibrary = true
    }
    
    signingConfigs {
        create("general") {
            storeFile = file("test.keystore")
            keyAlias = "test"
            keyPassword = "test"
            storePassword = "test"
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            signingConfig = signingConfigs.getByName("general")
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        debug {
            applicationIdSuffix = ".debug"
            isMinifyEnabled = false
            signingConfig = signingConfigs.getByName("general")
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

//    compileOptions { isCoreLibraryDesugaringEnabled = true }

    packaging {
        resources.excludes.addAll(
            arrayOf(
                "META-INF/README.md",
                "META-INF/CHANGES",
                "bundle.properties",
                "plugin.properties"
            )
        )

        jniLibs { useLegacyPackaging = true }
    }

    lint {
        abortOnError = false
        disable += listOf("MaterialDesignInsteadOrbitDesign")
    }

    buildFeatures {
        viewBinding = true
        buildConfig = true
        compose = true
    }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    //implementation(libs.hilt.android)
    implementation(libs.okhttp)
    implementation(libs.okhttp.coroutines)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.coil.compose)
    implementation(libs.coil.svg)
    implementation (libs.androidx.navigation.compose)

    implementation(libs.aboutlibraries.core)
    implementation(libs.aboutlibraries.compose.m3)

    //implementation(libs.androidx.hilt.navigation.compose)

    //kapt(libs.hilt.android.compiler)

    //testImplementation(libs.junit)
    //testImplementation(libs.hilt.android.testing)

    //kaptTest(libs.hilt.compiler)

    //androidTestImplementation(libs.androidx.junit)
    //androidTestImplementation(libs.androidx.espresso.core)
    //androidTestImplementation(platform(libs.androidx.compose.bom))
    //androidTestImplementation(libs.androidx.ui.test.junit4)
    //androidTestImplementation(libs.hilt.android.testing)
    //androidTestImplementation(libs.androidx.navigation.testing)

    //kaptAndroidTest(libs.hilt.compiler)

    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}

//kapt {
//    correctErrorTypes = true
//}

private fun getSecretProperty(name: String): String {
    val file = project.rootProject.file("token.properties")

    return if (file.exists()) {
        val properties = Properties().also { it.load(file.inputStream()) }
        properties.getProperty(name) ?: ""
    } else ""
}
