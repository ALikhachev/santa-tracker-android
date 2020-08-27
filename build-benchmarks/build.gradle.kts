plugins {
    kotlin("jvm") version "1.4.0"
    application
}

group = "org.jetbrains"
version = "unspecified"

repositories {
    jcenter()
    maven { url = uri("https://repo.gradle.org/gradle/libs-releases") }
    mavenLocal()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("org.jetbrains:kotlin-build-benchmarks:1.0-SNAPSHOT")
}

application {
    mainClassName = "RunBenchmarksKt"
}