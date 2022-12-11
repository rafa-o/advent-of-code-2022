package day10

import java.io.File
import kotlin.math.abs

fun main() {
    val input = parseInput()
    solutionPart1(input)
    solutionPart2(input)
}

private fun parseInput(): List<String> {
    val filePath = "${System.getProperty("user.dir")}/src/main/kotlin/day10/Day10Input.txt"
    val instructions = File(filePath).readText(Charsets.UTF_8)
        .trim()
        .split("\n")

    return instructions
}

private fun solutionPart1(instructions: List<String>) {
    println("Day 10 Solution")

    var x = 1
    var cycles = 0
    val notableCycles = arrayOf(20, 60, 100, 140, 180, 220)
    var signalStrengthSum = 0
    instructions.forEach { instruction ->
        val split = instruction.split(" ")
        if (split[0] == "noop") {
            cycles++

            if (cycles in notableCycles) {
                signalStrengthSum += cycles * x
            }
        } else {
            val v = split[1].toInt()

            repeat(2) {
                cycles++

                if (cycles in notableCycles) {
                    signalStrengthSum += cycles * x
                }
            }
            // updates x only after two cycles
            x += v
        }
    }

    println("Signal strength sum: $signalStrengthSum")
}

private fun solutionPart2(instructions: List<String>) {
    println("Day 10 Solution - Part 2")

    val xValues = Array(241) { 0 }
    var currentX = 1
    var cycles = 0

    // maps the value of X for each of the 240 cycles
    instructions.forEach { instruction ->
        val split = instruction.split(" ")
        if (split[0] == "noop") {
            cycles++
            xValues[cycles] = currentX
        } else {
            val v = split[1].toInt()
            cycles++
            xValues[cycles] = currentX

            currentX += v

            cycles++
            xValues[cycles] = currentX
        }
    }

    // builds final output array
    val answer = Array(6) { Array(40) { "  " } }
    for (row in 0 until 6) {
        for (col in 0 until 40) {
            val counter = row * 40 + col + 1
            if (abs(xValues[counter - 1] - col) <= 1) {
                answer[row][col] = "##"
            }
        }
    }

    println(
        answer.joinToString(
            separator = "",
            prefix = "\n",
            postfix = "",
            transform = { a ->
                a.joinToString(
                    separator = "",
                    prefix = "",
                    postfix = "\n"
                )
            }
        )
    )
}
