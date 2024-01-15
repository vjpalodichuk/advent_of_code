package com.capital7software.aoc2015.days;

import com.capital7software.aoc.lib.AdventOfCodeSolution;
import com.capital7software.aoc.lib.grid.Sleigh;

import java.time.Instant;
import java.util.List;

/**
 * --- Day 3: Perfectly Spherical Houses in a Vacuum ---
 * Santa is delivering presents to an infinite two-dimensional grid of houses.
 * <p>
 * He begins by delivering a present to the house at his starting location, and then an elf at the North Pole
 * calls him via radio and tells him where to move next. Moves are always exactly one house to the north (^),
 * south (v), east (&#62;), or west (&#60;). After each move, he delivers another present to the house at his new location.
 * <p>
 * However, the elf back at the North Pole has had a little too much eggnog, and so his directions are a little
 * off, and Santa ends up visiting some houses more than once. How many houses receive at least one present?
 * <p>
 * For example:
 * <p>
 * &#62; delivers presents to 2 houses: one at the starting location, and one to the east.
 * ^&#62;v&#60; delivers presents to 4 houses in a square, including twice to the house at his starting/ending location.
 * ^v^v^v^v^v delivers a bunch of presents to some very lucky children at only 2 houses.
 * <p>
 * This year, how many houses receive at least one present?
 * <p>
 * Your puzzle answer was 2081.
 * <p>
 * --- Part Two ---
 * The next year, to speed up the process, Santa creates a robot version of himself, Robo-Santa,
 * to deliver presents with him.
 * <p>
 * Santa and Robo-Santa start at the same location (delivering two presents to the same starting house),
 * then take turns moving based on instructions from the elf, who is eggnoggedly reading from the same
 * script as the previous year.
 * <p>
 * This year, how many houses receive at least one present?
 * <p>
 * For example:
 * <p>
 * ^v delivers presents to 3 houses, because Santa goes north, and then Robo-Santa goes south.
 * ^&#62;v&#60; now delivers presents to 3 houses, and Santa and Robo-Santa end up back where they started.
 * ^v^v^v^v^v now delivers presents to 11 houses, with Santa going one direction and Robo-Santa going the other.
 * <p>
 * Your puzzle answer was 2341
 */
public class Day03 implements AdventOfCodeSolution {
    /**
     * Instantiates the solution instance.
     */
    public Day03() {

    }

    private final Sleigh sleigh = new Sleigh();

    @Override
    public String getDefaultInputFilename() {
        return "inputs/input_day_03-01.txt";
    }

    @Override
    public void runPart1(List<String> input) {

        for (var route : input) {
            var start = Instant.now();
            int routeId = deliverGifts(route);
            var total = getUniqueHouseCount(routeId);
            var end = Instant.now();

            System.out.printf("Santa delivered at least one gift to %d houses!%n", total);
            printTiming(start, end);
        }
    }

    @Override
    public void runPart2(List<String> input) {

        for (var route : input) {
            var start = Instant.now();
            int routeId = deliverGiftsWithRoboSanta(route);
            var total = getUniqueHouseCount(routeId);
            var end = Instant.now();

            System.out.printf("Santa and Robo Santa delivered at least one gift to %d houses!%n", total);
            printTiming(start, end);
        }
    }

    /**
     * Delivers the gifts using Santa's Sleigh by following the specified route.
     * Returns the ID of the route.
     *
     * @param route The delivery route to follow.
     * @return The ID of the route.
     */
    public int deliverGifts(String route) {
        return sleigh.deliverGifts(route);
    }

    /**
     * Delivers the gifts using Santa's Sleigh and Robo Santa by following the specified route.
     * Returns the ID of the route.
     *
     * @param route The delivery route to follow.
     * @return The ID of the route.
     */
    public int deliverGiftsWithRoboSanta(String route) {
        return sleigh.deliverGiftsWithRoboSanta(route);
    }

    /**
     * Returns the unique number of houses delivered gifts for the specified route.
     *
     * @param routeId The route to retrieve the unique number of houses for.
     * @return The unique number of houses delivered gifts for the specified route.
     */
    public long getUniqueHouseCount(int routeId) {
        return sleigh.getUniqueHouseCount(routeId);
    }
}
