package day04

import java.io.File

fun main() {
    val input = parseInput()
    solutionPart1(input)
    solutionPart2(input)
}

private fun parseInput(): List<String> {
    val filePath = "${System.getProperty("user.dir")}/src/main/kotlin/day04/Day04Input.txt"
    val assigments = File(filePath).readText(Charsets.UTF_8)
        .trim()
        .split("\n")

    return assigments
}

private fun solutionPart1(assignments: List<String>) {
    println("Day 04 Solution")
    var overlappingPairs = 0

    assignments.forEach { assigment ->
        val (first, second) = assigment.split(",")
        val (range1Start: Int, range1End: Int) = first.split("-").map { it.toInt() }
        val (range2Start: Int, range2End: Int) = second.split("-").map { it.toInt() }

        if (range1Start >= range2Start && range1End <= range2End || // range 1 is contained in range 2
            range2Start >= range1Start && range2End <= range1End // or range 2 is contained in range 1
        ) {
            overlappingPairs++
        }
    }

    println("Overlapping pairs: $overlappingPairs")
}

private fun solutionPart2(assignments: List<String>) {
    println("Day 04 Solution - Part 2")
    var overlappingPairs = 0

    assignments.forEach { assigment ->
        val (first, second) = assigment.split(",")
        val (range1Start: Int, range1End: Int) = first.split("-").map { it.toInt() }
        val (range2Start: Int, range2End: Int) = second.split("-").map { it.toInt() }

        if ((range1Start <= range2End) && (range1End >= range2Start)) {
            overlappingPairs++
        }
    }

    println("Overlapping pairs: $overlappingPairs")
}
