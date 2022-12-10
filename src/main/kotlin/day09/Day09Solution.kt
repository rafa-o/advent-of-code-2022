package day09

import java.io.File
import kotlin.math.abs

fun main() {
    val input = parseInput()
    solutionPart1(input)
    solutionPart2(input)
}

private fun parseInput(): List<String> {
    val filePath = "${System.getProperty("user.dir")}/src/main/kotlin/day09/Day09Input.txt"
    val input = File(filePath).readText(Charsets.UTF_8)
        .trim()
        .split("\n")

    return input
}

private fun solutionPart1(movements: List<String>) {
    println("Day 09 Solution")

    var headPos = Pair(0, 0)
    var tailPos = Pair(0, 0)
    val tailPositions = mutableSetOf(tailPos)

    movements.forEach { move ->
        println(move)
        val (direction, count) = move.split(" ")
        repeat(count.toInt()) {
            headPos = moveHead(headPos, direction)
            tailPos = updateTailPos(tailPos, headPos)
            tailPositions.add(tailPos)
        }
    }

    println("Positions visited by tail: ${tailPositions.size}")
}

private fun moveHead(headPos: Pair<Int, Int>, direction: String): Pair<Int, Int> {
    return when (direction[0]) {
        'U' -> Pair(headPos.first + 1, headPos.second)
        'L' -> Pair(headPos.first, headPos.second - 1)
        'D' -> Pair(headPos.first - 1, headPos.second)
        'R' -> Pair(headPos.first, headPos.second + 1)
        else -> throw IllegalArgumentException("Unrecognized direction $direction")
    }
}

private fun updateTailPos(tailPos: Pair<Int, Int>, headPos: Pair<Int, Int>): Pair<Int, Int> {
    val rowDiff = abs(headPos.first - tailPos.first)
    val colDiff = abs(headPos.second - tailPos.second)

    when (rowDiff + colDiff) {
        3 -> {
            // move diagonally
            val newRow = tailPos.first + if (headPos.first > tailPos.first) 1 else -1
            val newCol = tailPos.second + if (headPos.second > tailPos.second) 1 else -1
            return Pair(newRow, newCol)
        }
        2 -> {
            return if (rowDiff == 0) {
                // move vertically
                val newCol = tailPos.second + if (headPos.second > tailPos.second) 1 else -1
                Pair(tailPos.first, newCol)
            } else if (colDiff == 0) {
                // move horizontally
                val newRow = tailPos.first + if (headPos.first > tailPos.first) 1 else -1
                Pair(newRow, tailPos.second)
            } else {
                // it's diagonally adjacent
                tailPos
            }
        }
    }

    return tailPos
}

private fun solutionPart2(movements: List<String>) {
    println("Day 09 Solution - Part 2")

    val knots = Array(10) { Pair(0, 0) }
    val tailPositions = mutableSetOf(knots[0])

    val movingDistances = mapOf(
        "R" to Pair(1, 0),
        "U" to Pair(0, 1),
        "L" to Pair(-1, 0),
        "D" to Pair(0, -1),
    )

    movements.forEach { move ->
        println(move)
        val (direction, count) = move.split(" ")
        repeat(count.toInt()) {
            move(movingDistances[direction]!!, knots)
            tailPositions.add(knots[9])
        }
    }

    println("Positions visited by tail: ${tailPositions.size}")
}

private fun isTouching(x1: Int, y1: Int, x2: Int, y2: Int): Boolean {
    return abs(x1 - x2) <= 1 && abs(y1 - y2) <= 1
}

private fun move(distanceToMove: Pair<Int, Int>, knots: Array<Pair<Int, Int>>) {
    // start by updating the head (first knot)
    knots[0] = Pair(knots[0].first + distanceToMove.first, knots[0].second + distanceToMove.second)

    for (i in 1..9) {
        val (headX, headY) = knots[i - 1]
        var (tailX, tailY) = knots[i]

        if (!isTouching(headX, headY, tailX, tailY)) {
            val signX = if (headX == tailX) 0 else (headX - tailX) / abs(headX - tailX)
            val signY = if (headY == tailY) 0 else (headY - tailY) / abs(headY - tailY)

            tailX += signX
            tailY += signY
        }

        knots[i] = Pair(tailX, tailY)
    }
}
