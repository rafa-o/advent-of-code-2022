package day12

import java.io.File
import java.util.ArrayDeque
import java.util.Queue

/*
 * This uses BFS to solve. hyper-neutrino has a good visual explanation
 * on today's problem: https://www.youtube.com/watch?v=xhe79JubaZI
 */
fun main() {
    val input = parseInput()
    solutionPart1(input)
    solutionPart2(input)
}

private fun parseInput(): List<String> {
    val filePath = "${System.getProperty("user.dir")}/src/main/kotlin/day12/Day12Input.txt"
    val heightmap = File(filePath).readText(Charsets.UTF_8)
        .trim()
        .split("\n")

    return heightmap
}

private fun solutionPart1(heightmap: List<String>) {
    println("Day 12 Solution")

    val (startingPos, _, matrix) = getStartEndPositionsAndNumberMatrix(heightmap)

    val queue: Queue<Triple<Int, Int, Int>> = ArrayDeque()
    queue.add(Triple(0, startingPos.first, startingPos.second))

    val visited = mutableSetOf(startingPos)
    outer@ while (queue.isNotEmpty()) {
        val (distance, row, col) = queue.remove()

        for ((neighborRow, neighborCol) in getNeighbors(row, col, matrix)) {
            if (Pair(neighborRow, neighborCol) in visited) continue

            // distance between neighbor nodes should differ at most by 1
            if (matrix[neighborRow][neighborCol] - matrix[row][col] > 1) continue

            if (matrix[neighborRow][neighborCol] == 26) {
                println("Minimum steps to final point is: ${distance + 1}")
                break@outer
            }

            visited.add(Pair(neighborRow, neighborCol))
            queue.add(Triple(distance + 1, neighborRow, neighborCol))
        }
    }
}

private fun solutionPart2(heightmap: List<String>) {
    println("Day 12 Solution - Part 2")
    val (_, endingPos, matrix) = getStartEndPositionsAndNumberMatrix(heightmap)

    val queue: Queue<Triple<Int, Int, Int>> = ArrayDeque()
    queue.add(Triple(0, endingPos.first, endingPos.second))

    val visited = mutableSetOf(endingPos)
    outer@ while (queue.isNotEmpty()) {
        val (distance, row, col) = queue.remove()

        for ((neighborRow, neighborCol) in getNeighbors(row, col, matrix)) {
            if (Pair(neighborRow, neighborCol) in visited) continue

            // distance between neighbor nodes should differ at most by -1 since we're going down
            if (matrix[neighborRow][neighborCol] - matrix[row][col] < -1) continue

            if (matrix[neighborRow][neighborCol] == 0) {
                println("Minimum steps to final point is: ${distance + 1}")
                break@outer
            }

            visited.add(Pair(neighborRow, neighborCol))
            queue.add(Triple(distance + 1, neighborRow, neighborCol))
        }
    }
}

fun getNeighbors(row: Int, col: Int, matrix: Array<IntArray>): List<Pair<Int, Int>> {
    val neighbors = mutableListOf<Pair<Int, Int>>()

    if (row != 0) neighbors.add(Pair(row - 1, col))
    if (row < matrix.size - 1) neighbors.add(Pair(row + 1, col))
    if (col != 0) neighbors.add(Pair(row, col - 1))
    if (col < matrix[0].size - 1) neighbors.add(Pair(row, col + 1))

    return neighbors
}

private fun getStartEndPositionsAndNumberMatrix(heightmap: List<String>): Triple<Pair<Int, Int>, Pair<Int, Int>, Array<IntArray>> {
    var startingPos = Pair(0, 0)
    var endingPos = Pair(0, 0)
    val matrix = Array(heightmap.size) { IntArray(heightmap[0].length) { 0 } }

    for (i in heightmap.indices) {
        for (j in heightmap[i].indices) {
            if (heightmap[i][j] == 'S') {
                startingPos = Pair(i, j)
                // makes starting char be value 0
                matrix[i][j] = 0
            } else if (heightmap[i][j] == 'E') {
                // makes END char be value 26
                endingPos = Pair(i, j)
                matrix[i][j] = 26
            } else {
                matrix[i][j] = heightmap[i][j] - 'a'
            }
        }
    }

    return Triple(startingPos, endingPos, matrix)
}
