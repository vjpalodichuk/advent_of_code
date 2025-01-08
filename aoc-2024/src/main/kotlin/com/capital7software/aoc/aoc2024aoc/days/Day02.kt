package com.capital7software.aoc.aoc2024aoc.days

import com.capital7software.aoc.lib.AdventOfCodeSolution
import com.capital7software.aoc.lib.math.RedNosedReports
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings
import kotlin.system.measureNanoTime
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * **--- Day 2: Red-Nosed Reports ---**
 *
 * Fortunately, the first location The Historians want to search isn't a long walk
 * from the Chief Historian's office.
 *
 * While the [Red-Nosed Reindeer nuclear fusion/fission plant](https://adventofcode.com/2015/day/19)
 * appears to contain no sign of the Chief Historian, the engineers there run
 * up to you as soon as they see you. Apparently, they **still** talk about the
 * time Rudolph was saved through molecular synthesis from a single electron.
 *
 * They're quick to add that - since you're already here - they'd really appreciate
 * your help analyzing some unusual data from the Red-Nosed reactor. You turn to
 * check if The Historians are waiting for you, but they seem to have already divided
 * into groups that are currently searching every corner of the facility. You offer
 * to help with the unusual data.
 *
 * The unusual data (your puzzle input) consists of many **reports**, one report
 * per line. Each report is a list of numbers called **levels** that are separated
 * by spaces. For example:
 *
 * ```
 * 7 6 4 2 1
 * 1 2 7 8 9
 * 9 7 6 2 1
 * 1 3 2 4 5
 * 8 6 4 4 1
 * 1 3 6 7 9
 * ```
 *
 * This example data contains six reports each containing five levels.
 *
 * The engineers are trying to figure out which reports are **safe**.
 * The Red-Nosed reactor safety systems can only tolerate levels that are
 * either gradually increasing or gradually decreasing. So, a report only
 * counts as safe if both of the following are true:
 *
 * - The levels are either **all increasing** or **all decreasing**.
 * - Any two adjacent levels differ by **at least one** and **at most three**.
 *
 * In the example above, the reports can be found safe or unsafe by checking those rules:
 *
 * - ```7 6 4 2 1```: **Safe** because the levels are all decreasing by 1 or 2.
 * - ```1 2 7 8 9```: **Unsafe** because 2 7 is an increase of 5.
 * - ```9 7 6 2 1```: **Unsafe** because 6 2 is a decrease of 4.
 * - ```1 3 2 4 5```: **Unsafe** because 1 3 is increasing but 3 2 is decreasing.
 * - ```8 6 4 4 1```: **Unsafe** because 4 4 is neither an increase nor a decrease.
 * - ```1 3 6 7 9```: **Safe** because the levels are all increasing by 1, 2, or 3.
 *
 * So, in this example, **```2```** reports are **safe**.
 *
 * Analyze the unusual data from the engineers. **How many reports are safe?**
 *
 * Your puzzle answer was ```341```.
 *
 * **--- Part Two ---**
 *
 * The engineers are surprised by the low number of safe reports until they realize they
 * forgot to tell you about the Problem Dampener.
 *
 * The Problem Dampener is a reactor-mounted module that lets the reactor safety systems
 * **tolerate a single bad level** in what would otherwise be a safe report. It's like
 * the bad level never happened!
 *
 * Now, the same rules apply as before, except if removing a single level from an unsafe
 * report would make it safe, the report instead counts as safe.
 *
 * More of the above example's reports are now safe:
 *
 * - ```7 6 4 2 1```: **Safe** without removing any level.
 * - ```1 2 7 8 9```: **Unsafe** regardless of which level is removed.
 * - ```9 7 6 2 1```: **Unsafe** regardless of which level is removed.
 * - ```1 3 2 4 5```: **Safe** by removing the second level, 3.
 * - ```8 6 4 4 1```: **Safe** by removing the third level, 4.
 * - ```1 3 6 7 9```: **Safe** without removing any level.
 *
 * Thanks to the Problem Dampener, **```4```** reports are actually **safe**!
 *
 * Update your analysis by handling situations where the Problem Dampener can
 * remove a single level from unsafe reports. **How many reports are now safe?**
 *
 * Your puzzle answer was ```404```.
 */
class Day02 : AdventOfCodeSolution {
  companion object {
    private val log: Logger = LoggerFactory.getLogger(Day02::class.java)
  }

  override fun getDefaultInputFilename(): String = "inputs/input_day_02-01.txt"

  override fun runPart1(input: List<String>) {
    var answer: Long
    val elapsed = measureNanoTime {
      answer = calculateNumberOfSafeReports(input)
    }
    log.info("$answer is the number of safe reports!")
    logTimings(log, elapsed)
  }

  override fun runPart2(input: List<String>) {
    var answer: Long
    val elapsed = measureNanoTime {
      answer = calculateNumberOfSafeReports(input, true)
    }
    log.info("$answer is the number of safe reports tolerating 1 error level!")
    logTimings(log, elapsed)
  }

  /**
   * Returns the calculated number of safe reports.
   *
   * @param input The list of reports.
   * @param useDampener True to allow for a single error in the levels.
   * @return The calculated number of safe reports.
   */
  @SuppressFBWarnings
  fun calculateNumberOfSafeReports(input: List<String>, useDampener: Boolean = false): Long {
    return RedNosedReports(input).calculateNumberOfSafeReports(useDampener)
  }
}
