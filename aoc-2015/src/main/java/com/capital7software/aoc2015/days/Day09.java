package com.capital7software.aoc2015.days;

import com.capital7software.aoc2015.lib.AdventOfCodeSolution;
import com.capital7software.aoc2015.lib.graph.parser.Day09Parser;
import com.capital7software.aoc2015.lib.graph.path.MinimumSpanningTreeKruskal;
import com.capital7software.aoc2015.lib.graph.path.MinimumSpanningTreePrim;

import java.time.Instant;
import java.util.List;

/**
 * --- Day 9: All in a Single Night ---
 * Every year, Santa manages to deliver all of his presents in a single night.
 * <p>
 * This year, however, he has some new locations to visit; his elves have provided him the distances
 * between every pair of locations. He can start and end at any two (different) locations he wants,
 * but he must visit each location exactly once. What is the shortest distance he can travel to achieve this?
 * <p>
 * For example, given the following distances:
 * <p>
 * London to Dublin = 464
 * London to Belfast = 518
 * Dublin to Belfast = 141
 * The possible routes are therefore:
 * <p>
 * Dublin -> London -> Belfast = 982
 * London -> Dublin -> Belfast = 605
 * London -> Belfast -> Dublin = 659
 * Dublin -> Belfast -> London = 659
 * Belfast -> Dublin -> London = 605
 * Belfast -> London -> Dublin = 982
 * The shortest of these is London -> Dublin -> Belfast = 605, and so the answer is 605 in this example.
 * <p>
 */
public class Day09 implements AdventOfCodeSolution {
    @Override
    public String getDefaultInputFilename() {
        return "inputs/input_day_09-01.txt";
    }

    @Override
    public void runPart1(List<String> input) {
        var start = Instant.now();
        var shortestDistance = mstKruskal(input);
        var end = Instant.now();
        System.out.printf("The distance of the shortest route is: %d%n", shortestDistance);
        printTiming(start, end);
    }

    @Override
    public void runPart2(List<String> input) {
        var start = Instant.now();
        var shortestDistance = mstPrim(input);
        var end = Instant.now();
        System.out.printf("The distance of the shortest route is: %d%n", shortestDistance);
        printTiming(start, end);
    }

    public long distanceOfShortestRouteVisitingEachNodeOnce(List<String> routes) {
        var graph = new Day09Parser().parse(routes, "day09");
        var mstBuilder = new MinimumSpanningTreeKruskal<String, Integer>();

        if (graph.isEmpty()) {
            throw new RuntimeException("A valid Graph is required! " + graph);
        }

        var spanningTree = mstBuilder.spanningTree(graph.get());

        return spanningTree
                .getEdges()
                .stream()
                .filter(it -> it.getWeight().isPresent())
                .mapToInt(it -> it.getWeight().get())
                .sum();
    }

    public long mstKruskal(List<String> routes) {
        var graph = new Day09Parser().parse(routes, "day09");
        var mstBuilder = new MinimumSpanningTreeKruskal<String, Integer>();

        if (graph.isEmpty()) {
            throw new RuntimeException("A valid Graph is required! " + graph);
        }

        var spanningTree = mstBuilder.spanningTree(graph.get());

        return spanningTree
                .getEdges()
                .stream()
                .filter(it -> it.getWeight().isPresent())
                .mapToInt(it -> it.getWeight().get())
                .sum();
    }

    public long mstPrim(List<String> routes) {
        var graph = new Day09Parser().parse(routes, "day09");
        var mstBuilder = new MinimumSpanningTreePrim<String, Integer>(0, Integer.MAX_VALUE);

        if (graph.isEmpty()) {
            throw new RuntimeException("A valid Graph is required! " + graph);
        }

        var spanningTree = mstBuilder.spanningTree(graph.get());

        return spanningTree
                .getEdges()
                .stream()
                .filter(it -> it.getWeight().isPresent())
                .mapToInt(it -> it.getWeight().get())
                .sum();
    }
}
