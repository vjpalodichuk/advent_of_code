plugins {
    id("capital7software.library-conventions")
}

version = "1.0-SNAPSHOT"

dependencies {
    implementation(project(":aoc-library"))
}

tasks.withType<JavaExec>().configureEach {
    jvmArgs = listOf("-Xss4m")
    group = "aoc"
    description = "Advent of Code 2023 Puzzles"
    classpath = sourceSets["main"].runtimeClasspath
    mainClass = "com.capital7software.aoc.aoc2023.AdventOfCode2023Runner"
}

tasks.register("run2023", JavaExec::class)

tasks.register("day14", JavaExec::class) {
    mainClass = "com.capital7software.aoc.aoc2023.days.Day14"
}
tasks.register("day15", JavaExec::class) {
    mainClass = "com.capital7software.aoc.aoc2023.days.Day15"
}
tasks.register("day16", JavaExec::class) {
    mainClass = "com.capital7software.aoc.aoc2023.days.Day16"
}
tasks.register("day17", JavaExec::class) {
    mainClass = "com.capital7software.aoc.aoc2023.days.Day17"
}
tasks.register("day18", JavaExec::class) {
    mainClass = "com.capital7software.aoc.aoc2023.days.Day18"
}
tasks.register("day19", JavaExec::class) {
    mainClass = "com.capital7software.aoc.aoc2023.days.Day19"
}
tasks.register("day20", JavaExec::class) {
    mainClass = "com.capital7software.aoc.aoc2023.days.Day20"
}
tasks.register("day21", JavaExec::class) {
    mainClass = "com.capital7software.aoc.aoc2023.days.Day21"
}
tasks.register("day22", JavaExec::class) {
    mainClass = "com.capital7software.aoc.aoc2023.days.Day22"
}
tasks.register("day23", JavaExec::class) {
    mainClass = "com.capital7software.aoc.aoc2023.days.Day23"
}
tasks.register("day24", JavaExec::class) {
    mainClass = "com.capital7software.aoc.aoc2023.days.Day24"
}
tasks.register("day25", JavaExec::class) {
    mainClass = "com.capital7software.aoc.aoc2023.days.Day25"
}
