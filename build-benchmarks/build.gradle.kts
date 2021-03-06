plugins {
    application
}

group = "org.jetbrains"
version = "unspecified"

buildscript {
    val kotlinVersion = System.getenv("KOTLIN_VERSION") ?: "1.4.255-SNAPSHOT"
    val kotlinRepo = "https://buildserver.labs.intellij.net/guestAuth/app/rest/builds/buildType:(id:Kotlin_KotlinDev_CompilerDistAndMavenArtifacts),number:$kotlinVersion,branch:default:any/artifacts/content/maven"
    extra["kotlinRepo"] = kotlinRepo

    repositories {
        mavenLocal()
        maven {
            url = uri(kotlinRepo)
        }
    }

    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
    }
}

apply(plugin = "kotlin")

val kotlinRepo: String by extra
repositories {
    maven { url = uri("https://repo.gradle.org/gradle/libs-releases") }
    mavenLocal()
    maven {
        url = uri(kotlinRepo)
    }
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("org.jetbrains:kotlin-build-benchmarks:1.0-SNAPSHOT")
}

application {
    mainClassName = "RunBenchmarksKt"
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().all {
    kotlinOptions {
        freeCompilerArgs += listOf(
                "-Xuse-experimental=kotlin.ExperimentalUnsignedTypes"
        )
    }
}