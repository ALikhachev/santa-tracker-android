import org.jetbrains.kotlin.build.benchmarks.dsl.*

fun santaScenarios() =
    suite {
        val santaTrackerAppIndexingReceiver = changeableFile("santaTracker/AppIndexingReceiver")
        val commonAudioPlayer = changeableFile("common/AudioPlayer")
        val santaTrackerResource = changeableFile("santaTracker/resource")

        defaultTasks(Tasks.ASSEMBLE)
        val defaultArguments = arrayOf(
                "--no-build-cache",
                "--info",
                "--watch-fs",
        )

        val parallelBuild = arrayOf(
                *defaultArguments,
                "--parallel",
        )

        val nonParallelBuild = arrayOf(
                *defaultArguments,
                "--no-parallel",
        )

        defaultJdk = System.getenv("JDK_11")
        defaultArguments(*nonParallelBuild)

        scenario("clean build") {
            step {
                doNotMeasure()
                runTasks(Tasks.CLEAN)
            }
            step {
            }
            repeat = 3U
        }

        scenario("clean build parallel") {
            arguments(*parallelBuild)
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

        scenario("Run santa-tracker tests") {
            step {
                runTasks(Tasks.SANTA_TRACKER_TEST)
            }
            repeat = 5U
        }

        scenario("Run santa-tracker tests after changes") {
            step {
                changeFile(santaTrackerAppIndexingReceiver, TypeOfChange.ADD_PRIVATE_FUNCTION)
                runTasks(Tasks.SANTA_TRACKER_TEST)
            }
            repeat = 5U
        }
    }
