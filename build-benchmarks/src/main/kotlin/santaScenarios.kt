import org.jetbrains.kotlin.build.benchmarks.dsl.*

fun santaScenarios() =
    suite {
        val santaTrackerAppIndexingReceiver = changeableFile("santaTracker/AppIndexingReceiver")

        defaultTasks(Tasks.ASSEMBLE)
        val defaultArguments = listOf(
                "--no-build-cache",
                "--info"
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
        }

        scenario("clean build") {
            step {
                doNotMeasure()
                runTasks(Tasks.CLEAN)
            }
            step {
            }
        }

        scenario("(leaf, KotlinJVM) santa tracker add public function") {
            step {
                changeFile(santaTrackerAppIndexingReceiver, TypeOfChange.ADD_PUBLIC_FUNCTION)
            }
        }

        scenario("(leaf, KotlinJVM) santa tracker add private function") {
            step {
                changeFile(santaTrackerAppIndexingReceiver, TypeOfChange.ADD_PRIVATE_FUNCTION)
            }
        }
    }
