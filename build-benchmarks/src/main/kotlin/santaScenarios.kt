import org.jetbrains.kotlin.build.benchmarks.dsl.*

fun santaScenarios() =
    suite {
        val santaTrackerAppIndexingReceiver = changeableFile("santaTracker/AppIndexingReceiver")
        val commonAudioPlayer = changeableFile("common/AudioPlayer")
        val santaTrackerResource = changeableFile("santaTracker/resource")

        defaultTasks(Tasks.ASSEMBLE)
        val defaultArguments = listOf(
                "--no-build-cache",
                "--info",
                "--watch-fs",
        )
        defaultArguments(
            *defaultArguments.toTypedArray(),
            if (System.getenv("BUILD_PARALLEL") == "true") "--parallel" else "--no-parallel"
        )
        val maxWorkers: String? = System.getenv("MAX_WORKERS")
        if (maxWorkers?.toIntOrNull() != null) {
            defaultArguments("--max-workers=$maxWorkers")
        }
        defaultJdk = System.getenv("JDK_8")

        scenario("clean build parallel") {
            arguments(*defaultArguments.toTypedArray(), "--parallel")
            step {
                doNotMeasure()
                runTasks(Tasks.CLEAN)
            }
            step {
            }
            repeat = 5U
        }

        scenario("clean build") {
            step {
                doNotMeasure()
                runTasks(Tasks.CLEAN)
            }
            step {
            }
            repeat = 3U
        }

        scenario("(non-leaf) common add public function") {
            step {
                changeFile(commonAudioPlayer, TypeOfChange.ADD_PUBLIC_FUNCTION)
            }
        }

        scenario("(non-leaf) common add private function") {
            step {
                changeFile(commonAudioPlayer, TypeOfChange.ADD_PRIVATE_FUNCTION)
            }
        }

        scenario("(leaf, KotlinJVM) santa tracker add public function") {
            step {
                changeFile(santaTrackerAppIndexingReceiver, TypeOfChange.ADD_PUBLIC_FUNCTION)
            }
            trackedMetrics(setOf("GRADLE_BUILD.EXECUTION.COMPILATION_TASKS.KotlinCompile.RUN_COMPILER.INCREMENTAL_COMPILATION"))
        }

        scenario("(leaf, KotlinJVM) santa tracker add private function") {
            step {
                changeFile(santaTrackerAppIndexingReceiver, TypeOfChange.ADD_PRIVATE_FUNCTION)
            }
            trackedMetrics(setOf("GRADLE_BUILD.EXECUTION.COMPILATION_TASKS.KotlinCompile.RUN_COMPILER.INCREMENTAL_COMPILATION"))
        }

        scenario("(resource) santa tracker change resource") {
            step {
                changeFile(santaTrackerResource, TypeOfChange.CHANGE_ANDROID_RESOURCE)
            }
        }
    }
