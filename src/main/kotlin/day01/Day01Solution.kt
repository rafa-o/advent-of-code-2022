package day01

import java.io.File
import java.util.PriorityQueue
import kotlin.math.max

fun main() {
    val input = parseInput()
    solutionPart1(allCalories = input)
    solutionPart2(allCalories = input)
}

private fun solutionPart1(allCalories: List<String>) {
    println("Day 01 Solution")
    var maxCalories = Integer.MIN_VALUE
    var currentElfCalories = 0
    allCalories.forEach { calorieLine ->
        if (calorieLine.isNotEmpty()) {
            currentElfCalories += calorieLine.toInt()
        } else {
            maxCalories = max(maxCalories, currentElfCalories)
            currentElfCalories = 0
        }
    }

    maxCalories = max(maxCalories, currentElfCalories)
    println("Max Calories: $maxCalories")
}

private fun solutionPart2(allCalories: List<String>) {
    println("Day 01 Solution - Part 2")

    val minHeap = PriorityQueue<Int>()
    var currentElfCalories = 0
    allCalories.forEach { calorieLine ->
        if (calorieLine.isNotEmpty()) {
            currentElfCalories += calorieLine.toInt()
        } else {
            addSumToHeap(currentElfCalories, minHeap)
            currentElfCalories = 0
        }
    }

    addSumToHeap(currentElfCalories, minHeap)
    val topThreeSum = minHeap.sum()
    println("Top 3 Elves Sum: $topThreeSum")
}

private fun addSumToHeap(sum: Int, heap: PriorityQueue<Int>) {
    heap.add(sum)
    if (heap.size > 3) heap.remove()
}

private fun parseInput(): List<String> {
    val filePath = "${System.getProperty("user.dir")}/src/main/kotlin/Day01/Day01Input.txt"
    val allCalories = File(filePath).readText(Charsets.UTF_8)
        .split("\n")

    return allCalories
}
