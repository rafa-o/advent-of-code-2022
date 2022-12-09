package day08

import java.io.File

fun main() {
    val input = parseInput()
    solutionPart1(input)
    solutionPart2(input)
}

private fun parseInput(): List<List<Int>> {
    val filePath = "${System.getProperty("user.dir")}/src/main/kotlin/day08/Day08Input.txt"
    val input = File(filePath).readText(Charsets.UTF_8)
        .trim()
        .split("\n")
        .map { row -> row.map { col -> Character.getNumericValue(col) } }

    return input
}

private fun solutionPart1(forestMap: List<List<Int>>) {
    println("Day 08 Solution")
    val rows = forestMap.size
    val cols = forestMap[0].size
    var visibleTrees = (rows * 2) + (cols * 2) - 4 // start counting the edges

    for (i in 1 until rows - 1) {
        for (j in 1 until cols - 1) {
            if (visibleFromAbove(forestMap[i][j], i, j, forestMap) ||
                visibleFromBelow(forestMap[i][j], i, j, forestMap) ||
                visibleFromTheLeft(forestMap[i][j], i, j, forestMap) ||
                visibleFromTheRight(forestMap[i][j], i, j, forestMap)
            ) {
                visibleTrees++
            }
        }
    }

    println("Visible trees: $visibleTrees")
}

private fun visibleFromAbove(value: Int, row: Int, col: Int, forestMap: List<List<Int>>): Boolean {
    return if (row > 0) {
        forestMap[row - 1][col] < value && visibleFromAbove(value, row - 1, col, forestMap)
    } else true
}

private fun visibleFromBelow(value: Int, row: Int, col: Int, forestMap: List<List<Int>>): Boolean {
    return if (row < forestMap.size - 1) {
        forestMap[row + 1][col] < value && visibleFromBelow(value, row + 1, col, forestMap)
    } else true
}

private fun visibleFromTheLeft(value: Int, row: Int, col: Int, forestMap: List<List<Int>>): Boolean {
    return if (col > 0) {
        forestMap[row][col - 1] < value && visibleFromTheLeft(value, row, col - 1, forestMap)
    } else true
}

private fun visibleFromTheRight(value: Int, row: Int, col: Int, forestMap: List<List<Int>>): Boolean {
    return if (col < forestMap[0].size - 1) {
        forestMap[row][col + 1] < value && visibleFromTheRight(value, row, col + 1, forestMap)
    } else true
}

private fun solutionPart2(forestMap: List<List<Int>>) {
    println("Day 08 Solution - Part 2")

    val rows = forestMap.size
    val cols = forestMap[0].size
    var maxScenicScore = 0L

    for (i in 1 until rows - 1) {
        for (j in 1 until cols - 1) {
            val treeScore = getScenicScoreAbove(forestMap[i][j], i, j, forestMap) *
                getScenicScoreLeft(forestMap[i][j], i, j, forestMap) *
                getScenicScoreBelow(forestMap[i][j], i, j, forestMap) *
                getScenicScoreRight(forestMap[i][j], i, j, forestMap)

            maxScenicScore = maxScenicScore.coerceAtLeast(treeScore)
        }
    }

    println("Max possible scenic score: $maxScenicScore")
}

private fun getScenicScoreAbove(value: Int, row: Int, col: Int, forestMap: List<List<Int>>, score: Long = 0L): Long {
    return if (row > 0) {
        if (forestMap[row - 1][col] < value) getScenicScoreAbove(value, row - 1, col, forestMap, score + 1L) else score + 1L
    } else score
}

private fun getScenicScoreBelow(value: Int, row: Int, col: Int, forestMap: List<List<Int>>, score: Long = 0L): Long {
    return if (row < forestMap.size - 1) {
        if (forestMap[row + 1][col] < value) getScenicScoreBelow(value, row + 1, col, forestMap, score + 1L) else score + 1L
    } else score
}

private fun getScenicScoreLeft(value: Int, row: Int, col: Int, forestMap: List<List<Int>>, score: Long = 0L): Long {
    return if (col > 0) {
        if (forestMap[row][col - 1] < value) getScenicScoreLeft(value, row, col - 1, forestMap, score + 1L) else score + 1L
    } else score
}

private fun getScenicScoreRight(value: Int, row: Int, col: Int, forestMap: List<List<Int>>, score: Long = 0L): Long {
    return if (col < forestMap[0].size - 1) {
        if (forestMap[row][col + 1] < value) getScenicScoreRight(value, row, col + 1, forestMap, score + 1L) else score + 1L
    } else score
}
