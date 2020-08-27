import org.jetbrains.kotlin.build.benchmarks.*

fun main() {
    mainImpl(santaScenarios(), "../.") // expected working dir is %SPACE_PROJECT_PATH%/build-benchmarks/
}
