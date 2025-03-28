// Top-level build file where you can add configuration options common to all sub-projects/modules.
import com.android.build.gradle.BaseExtension
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion
import org.jetbrains.kotlin.gradle.dsl.jvm.JvmTargetValidationMode
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  alias(libs.plugins.android.application) apply false
  alias(libs.plugins.android.library) apply false
  alias(libs.plugins.kotlin) apply false
  alias(libs.plugins.kotlin.compose) apply false
  alias(libs.plugins.kotlin.serialization) apply false
//  alias(libs.plugins.aboutlibraries) apply false
//  kotlin("kapt") version "2.0.20" apply false
//  id("com.google.dagger.hilt.android") version "2.52" apply false
//  kotlin("jvm") version "2.0.20" apply false
}

//buildscript {
//  dependencies { classpath(libs.androidx.navigation.safe.args.gradle.plugin) }
//}

fun Project.configureBaseExtension() {
  extensions.findByType(BaseExtension::class)?.run {
    compileSdkVersion(34)

    defaultConfig {
      minSdk = 26
      targetSdk = 31
      versionCode = 1
      versionName = "0.0.1"
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
