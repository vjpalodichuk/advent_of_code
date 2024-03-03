plugins {
  id("capital7software.library-conventions")
}

version = rootProject.version

dependencies {
  implementation(project(":aoc-library"))
}

tasks.withType<JavaExec>().configureEach {
  jvmArgs = listOf("-Xss4m")
  group = "aoc"
  description = "Advent of Code 2017 Puzzles"
  classpath = sourceSets["main"].runtimeClasspath
  mainClass = "com.capital7software.aoc.aoc2017aoc.AdventOfCode2017Runner"
}

tasks.register("run2017", JavaExec::class)

tasks.named("compileJava", JavaCompile::class.java) {
  options.compilerArgumentProviders.add(CommandLineArgumentProvider {
    // Provide compiled Kotlin classes to javac – needed for Java/Kotlin mixed sources to work
    listOf("--patch-module", "com.capital7software.aoc.aoc2017aoc=${sourceSets["main"].output.asPath}")
  })
}
