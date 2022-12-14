package day05

import java.io.File
import java.util.ArrayDeque

fun main() {
    val input = parseInput()
    solutionPart1(input)
    solutionPart2(input)
}

private fun parseInput(): List<String> {
    val filePath = "${System.getProperty("user.dir")}/src/main/kotlin/day05/Day05Input.txt"
    val cargoLoad = File(filePath).readText(Charsets.UTF_8)
        .split("\n\n")

    return cargoLoad
}

private fun solutionPart1(input: List<String>) {
    println("Day 05 Solution")
    val (cargoLoad, movingPlan) = input
    val stacks = getCargoStacks(cargoLoad)
    val movingProcedures = getMovingProcedures(movingPlan)

    for (procedure in movingProcedures) {
        val (quantity, sourceStackIdx, destinationStackIdx) = procedure

        repeat(quantity) {
            stacks[destinationStackIdx].push(stacks[sourceStackIdx].pop())
        }
    }

    val message = StringBuffer()
    for (i in stacks.indices) message.append(stacks[i].pop())

    println("Message: $message")
}

private fun solutionPart2(input: List<String>) {
    println("Day 05 Solution - Part 2")
    val (cargoLoad, movingPlan) = input
    val stacks = getCargoStacks(cargoLoad)
    val movingProcedures = getMovingProcedures(movingPlan)

    for (procedure in movingProcedures) {
        val (quantity, sourceStackIdx, destinationStackIdx) = procedure

        val tempStack = ArrayDeque<Char>()
        repeat(quantity) {
            tempStack.push(stacks[sourceStackIdx].pop())
        }

        while (tempStack.isNotEmpty()) {
            stacks[destinationStackIdx].push(tempStack.pop())
        }
    }

    val message = StringBuffer()
    for (i in stacks.indices) message.append(stacks[i].pop())

    println("Message: $message")
}

private fun getCargoStacks(cargoLoad: String): Array<ArrayDeque<Char>> {
    val numberOfStacks = Character.getNumericValue(cargoLoad.last())
    val stacks = Array(numberOfStacks) { ArrayDeque<Char>() }

    val cargoLines = cargoLoad.split("\n")
    // we start with the last line to see which stacks we have on the input
    for ((crateStackIndex, c) in cargoLines.last().withIndex()) {
        if (c.isDigit()) {
            // when we find a digit (stack number), we move up on the lines adding the crates
            // to the stack, using crateStackIndex as the index in the string where we can find the crate

            val stackAccessIdx = Character.getNumericValue(c) - 1
            for (i in cargoLines.lastIndex - 1 downTo 0) {
                val currentLine = cargoLines[i]
                if (crateStackIndex < currentLine.length && currentLine[crateStackIndex] != ' ') {
                    stacks[stackAccessIdx].push(currentLine[crateStackIndex])
                }
            }
        }
    }

    return stacks
}

private fun getMovingProcedures(movingPlan: String): List<Triple<Int, Int, Int>> {
    val instructions = mutableListOf<Triple<Int, Int, Int>>()
    movingPlan.trim().split("\n").forEach { movingProcedure ->
        // extract the numbers from the instruction line
        val (quantity, sourceStackIdx, destinationStackIdx) = Regex("[0-9]+").findAll(movingProcedure)
            .map { it.value.toInt() }
            .toList()

        instructions.add(Triple(quantity, sourceStackIdx - 1, destinationStackIdx - 1))
    }

    return instructions
}
