import com.android.build.gradle.BaseExtension
import com.android.build.gradle.BaseExtension

import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion
import org.jetbrains.kotlin.gradle.dsl.jvm.JvmTargetValidationMode
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask

import io.gitlab.arturbosch.detekt.Detekt

plugins {
  alias(libs.plugins.android.application) apply false
  alias(libs.plugins.android.library) apply false
  alias(libs.plugins.kotlin.android) apply false
  alias(libs.plugins.kotlin.compose) apply false
  alias(libs.plugins.kotlin.serialization) apply false
  alias(libs.plugins.kapt) apply false
  alias(libs.plugins.ksp) apply false
  alias(libs.plugins.hilt) apply false
  alias(libs.plugins.detek) apply false
  alias(libs.plugins.version.catalog.update) apply false
  alias(libs.plugins.versions.ben.manes) apply false
  alias(libs.plugins.aboutlibraries) apply false
}

buildscript {
  dependencies { classpath(libs.androidx.navigation.safe.args.gradle.plugin) }
}

fun Project.configureBaseExtension() {
  extensions.findByType(BaseExtension::class)?.run {
    compileSdkVersion(35)

    defaultConfig {
      minSdk = 26
      targetSdk = 28
      versionCode = 100
      versionName = "1.0.0"
    }

    compileOptions {
      sourceCompatibility = JavaVersion.VERSION_17
      targetCompatibility = JavaVersion.VERSION_17
    }
  }
}

subprojects {
  plugins.withId("com.android.application") { configureBaseExtension() }
  plugins.withId("com.android.library") { configureBaseExtension() }

  tasks.withType<KotlinCompile>().configureEach {
    compilerOptions {
      apiVersion = KotlinVersion.KOTLIN_2_0
      languageVersion = KotlinVersion.KOTLIN_2_0
      jvmTarget = JvmTarget.JVM_17
      jvmTargetValidationMode = JvmTargetValidationMode.WARNING
      freeCompilerArgs.add("-Xjvm-default=all")
    }
  }
}

tasks.register<Delete>("clean") { delete(rootProject.layout.buildDirectory) }

detekt {
    toolVersion = DependencyVersions.Detekt
    debug = true
    parallel = true
    autoCorrect = true
    input = files("src/main/kotlin")
}

tasks.withType<Detekt> {
    this.jvmTarget = "1.8"
    this.classpath.setFrom(project.configurations.getByName("detekt"))
    exclude("**/ignore/**")
}

val detektAll by tasks.registering(Detekt::class) {
    this.description = "Runs over whole code base without the starting overhead for each module."
    this.parallel = true
    this.buildUponDefaultConfig = true
    this.include("**/*.kt")
    this.exclude("**/resources/**")
    this.exclude("**/build/**")
    this.exclude("**/*.kts")
    this.exclude("**/ignore/**")
    this.jvmTarget = "1.8"
    this.classpath.setFrom(project.configurations.getByName("detekt"))
}
