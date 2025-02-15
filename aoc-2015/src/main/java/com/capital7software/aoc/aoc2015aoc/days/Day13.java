package com.capital7software.aoc.aoc2015aoc.days;

import com.capital7software.aoc.lib.AdventOfCodeSolution;
import com.capital7software.aoc.lib.graph.parser.Day13Parser;
import com.capital7software.aoc.lib.graph.path.HamiltonianPathfinder;
import com.capital7software.aoc.lib.graph.path.PathfinderProperties;
import com.capital7software.aoc.lib.graph.path.PathfinderResult;
import com.capital7software.aoc.lib.graph.path.PathfinderStatus;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * --- Day 13: Knights of the Dinner Table ---<br><br>
 * In years past, the holiday feast with your family hasn't gone so well. Not everyone gets along!
 * This year, you resolve, will be different. You're going to find the optimal seating arrangement
 * and avoid all those awkward conversations.
 *
 * <p><br>
 * You start by writing up a list of everyone invited and the amount their happiness would increase
 * or decrease if they were to find themselves sitting next to each other person. You have a
 * circular table that will be just big enough to fit everyone comfortably, and so each person
 * will have exactly two neighbors.
 *
 * <p><br>
 * For example, suppose you have only four attendees planned, and you calculate their potential
 * happiness as follows:
 * <ul>
 *     <li>
 *         Alice would gain 54 happiness units by sitting next to Bob.
 *     </li>
 *     <li>
 *         Alice would lose 79 happiness units by sitting next to Carol.
 *     </li>
 *     <li>
 *         Alice would lose 2 happiness units by sitting next to David.
 *     </li>
 *     <li>
 *         Bob would gain 83 happiness units by sitting next to Alice.
 *     </li>
 *     <li>
 *         Bob would lose 7 happiness units by sitting next to Carol.
 *     </li>
 *     <li>
 *         Bob would lose 63 happiness units by sitting next to David.
 *     </li>
 *     <li>
 *         Carol would lose 62 happiness units by sitting next to Alice.
 *     </li>
 *     <li>
 *         Carol would gain 60 happiness units by sitting next to Bob.
 *     </li>
 *     <li>
 *         Carol would gain 55 happiness units by sitting next to David.
 *     </li>
 *     <li>
 *         David would gain 46 happiness units by sitting next to Alice.
 *     </li>
 *     <li>
 *         David would lose 7 happiness units by sitting next to Bob.
 *     </li>
 *     <li>
 *         David would gain 41 happiness units by sitting next to Carol.
 *     </li>
 * </ul>
 * Then, if you seat Alice next to David, Alice would lose 2 happiness units
 * (because David talks so much), but David would gain 46 happiness units
 * (because Alice is such a good listener), for a total change of 44.
 *
 * <p><br>
 * If you continue around the table, you could then seat Bob next to Alice
 * (Bob gains 83, Alice gains 54). Finally, seat Carol, who sits next to
 * Bob (Carol gains 60, Bob loses 7) and David (Carol gains 55, David gains 41).
 * The arrangement looks like this:
 *
 * <p><br>
 * <code>
 *  &nbsp;&nbsp;&nbsp; +41 &nbsp; +46<br>
 * +55&nbsp;&nbsp; David &nbsp;-2<br>
 * Carol &nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Alice<br>
 * +60 &nbsp;&nbsp; Bob &nbsp;&nbsp;+54<br>
 *  &nbsp;&nbsp;&nbsp; -7 &nbsp; +83<br>
 * </code>
 *
 * <p><br>
 * After trying every other seating arrangement in this hypothetical scenario,
 * you find that this one is the most optimal, with a total change in happiness of 330.
 *
 * <p><br>
 * What is the total change in happiness for the optimal seating arrangement of the actual
 * guest list?
 *
 * <p><br>
 * Your puzzle answer was 618.
 *
 * <p><br>
 * --- Part Two ---<br><br>
 * In all the commotion, you realize that you forgot to seat yourself. At this point, you're
 * pretty apathetic toward the whole thing, and your happiness wouldn't really go up or down
 * regardless of who you sit next to. You assume everyone else would be just as ambivalent
 * about sitting next to you, too.
 *
 * <p><br>
 * So, add yourself to the list, and give all happiness relationships that involve you a score
 * of 0.
 *
 * <p><br>
 * What is the total change in happiness for the optimal seating arrangement that actually includes
 * yourself?
 *
 * <p><br>
 * Your puzzle answer was 601.
 */
public class Day13 implements AdventOfCodeSolution {
  private static final Logger LOGGER = LoggerFactory.getLogger(Day13.class);

  /**
   * Instantiates the solution instance.
   */
  public Day13() {

  }

  @Override
  public String getDefaultInputFilename() {
    return "inputs/input_day_13-01.txt";
  }

  @Override
  public void runPart1(List<String> input) {
    var start = Instant.now();
    var sum = greatestChangeInHappiness(input);
    var end = Instant.now();
    LOGGER.info("The greatest change in happiness is: {}", sum);
    logTimings(LOGGER, start, end);
  }

  @Override
  public void runPart2(List<String> input) {
    try {
      ClassLoader classloader = Thread.currentThread().getContextClassLoader();
      var url = classloader.getResource("inputs/input_day_13-02.txt");
      assert url != null;
      var path = Paths.get(url.toURI());
      List<String> inputLines = Files.readAllLines(path);

      var start = Instant.now();
      var sum = greatestChangeInHappiness(inputLines);
      var end = Instant.now();
      LOGGER.info("The greatest change in happiness with me included is: {}", sum);
      logTimings(LOGGER, start, end);
    } catch (URISyntaxException | IOException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Uses a Graph to find Hamiltonian Cycles and returns the path
   * with the greatest cost, which represents the greatest change in happiness.
   *
   * @param happiness The seating arrangement that specifies the levels of happiness.
   * @return The path with the greatest cost, which represents the greatest change in happiness.
   */
  public long greatestChangeInHappiness(List<String> happiness) {
    var graph = new Day13Parser().parse(happiness, "day13");

    if (graph.isEmpty()) {
      throw new RuntimeException("A valid Graph is required! " + graph);
    }

    var props = new Properties();
    props.put(PathfinderProperties.DETECT_CYCLES, Boolean.TRUE);
    props.put(PathfinderProperties.SUM_PATH, Boolean.TRUE);
    props.put(PathfinderProperties.STARTING_VERTICES, graph.get().getVertices());

    final var results = new ArrayList<PathfinderResult<String, Integer>>(150);
    final var pathFinder = new HamiltonianPathfinder<String, Integer>();
    pathFinder.find(graph.get(), props, result -> {
      results.add(result);
      return PathfinderStatus.CONTINUE;
    }, null);

    return results
        .stream()
        .mapToInt(PathfinderResult::cost)
        .max()
        .orElse(0);
  }
}
