package day06

import java.io.File

fun main() {
    val input = parseInput()
    solutionPart1(input)
    solutionPart2(input)
}

private fun parseInput(): String {
    val filePath = "${System.getProperty("user.dir")}/src/main/kotlin/day06/Day06Input.txt"
    val input = File(filePath).readText(Charsets.UTF_8)
        .trim()

    return input
}

private fun solutionPart1(datastream: String) {
    println("Day 06 Solution")
    var left = 0
    var right = 3

    val chars = mutableSetOf<Char>()
    chars.add(datastream[right])
    while (left < right) {
        val c = datastream[left]
        if (c in chars) {
            chars.clear()
            right++
            chars.add(datastream[right])
            left = right - 3
            continue
        }

        chars.add(datastream[left])
        left++
    }

    println("Characters before start-of-packet marker: ${right + 1}")
}

private fun solutionPart2(datastream: String) {
    println("Day 06 Solution - Part 2")
    println(datastream)
    var left = 0
    var right = 13

    val chars = mutableSetOf<Char>()
    chars.add(datastream[right])
    while (left < right) {
        val c = datastream[left]
        if (c in chars) {
            chars.clear()
            right++
            chars.add(datastream[right])
            left = right - 13
            continue
        }

        chars.add(datastream[left])
        left++
    }

    println("Characters before start-of-packet marker: ${right + 1}")
}
