import org.gradle.nativeplatform.platform.internal.DefaultNativePlatform

plugins {
  id("java")
  id("org.openjfx.javafxplugin") version "0.0.13"
  id("com.github.johnrengelman.shadow") version "7.1.2"
}

group = "scdk-csv-stat-changer"
version = "1.0-SNAPSHOT"

java {
  sourceCompatibility = JavaVersion.VERSION_17
  targetCompatibility = JavaVersion.VERSION_17
}

// This is so it picks up new builds on jitpack
configurations.all {
  resolutionStrategy.cacheChangingModulesFor(0, "seconds")
}

val lwjglVersion = "3.2.3"
var lwjglNatives = ""

if(DefaultNativePlatform.getCurrentOperatingSystem().isWindows) {
  lwjglNatives = "natives-windows"
}

if(DefaultNativePlatform.getCurrentOperatingSystem().isLinux) {
  lwjglNatives = "natives-linux"
}

if(DefaultNativePlatform.getCurrentOperatingSystem().isMacOsX) {
  lwjglNatives = "natives-macos"
}

repositories {
  mavenCentral()
  mavenLocal() // Uncomment to use mavenLocal version of LoD engine
  maven { url = uri("https://jitpack.io") }
  flatDir { dirs("libs") }
}

dependencies {
    // compile against SC 3.0 APIs (these are NOT bundled into your mod)
    compileOnly(files("libs/mod-loader-4.2.0.jar"))
    compileOnly(files("libs/lod-game-snapshot.jar"))

    // Keep this — your mod depends on it for CSV parsing
    implementation("com.opencsv:opencsv:5.7.1")

    // Optional runtime dependencies (harmless to keep)
    runtimeOnly("org.lwjgl", "lwjgl", classifier = lwjglNatives)
    runtimeOnly("org.lwjgl", "lwjgl-glfw", classifier = lwjglNatives)
    runtimeOnly("org.lwjgl", "lwjgl-nuklear", classifier = lwjglNatives)
    runtimeOnly("org.lwjgl", "lwjgl-opengl", classifier = lwjglNatives)
    runtimeOnly("org.lwjgl", "lwjgl-stb", classifier = lwjglNatives)

    testImplementation("junit:junit:4.13.2")
}


javafx {
  version = "18.0.2"
  modules("javafx.controls", "javafx.fxml")
}

sourceSets {
  main {
    java {
      srcDirs("src/main/java")
      exclude(".gradle", "build", "files")
    }
  }
}

buildscript {
  repositories {
    gradlePluginPortal()
  }
  /*dependencies {
    implementation("com.github.johnrengelman.shadow:7.1.2")
  }*/
}

apply(plugin = "com.github.johnrengelman.shadow")
apply(plugin = "java")

tasks.jar {
  exclude("*.jar")
}
