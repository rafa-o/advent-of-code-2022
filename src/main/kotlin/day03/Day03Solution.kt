package day03

import java.io.File

fun main() {
    val input = parseInput()
    solutionPart1(rucksackContents = input)
    solutionPart2(rucksackContents = input)
}

private fun parseInput(): List<String> {
    val filePath = "${System.getProperty("user.dir")}/src/main/kotlin/day03/Day03Input.txt"
    val rucksackContents = File(filePath).readText(Charsets.UTF_8)
        .trim()
        .split("\n")

    return rucksackContents
}

private fun solutionPart1(rucksackContents: List<String>) {
    println("Day 03 Solution")
    var itemPrioritySum = 0

    rucksack@for (rucksackLoad in rucksackContents) {
        val middle = rucksackLoad.length / 2
        var left = 0
        var right = rucksackLoad.lastIndex

        val leftCompartment = mutableSetOf<Char>()
        val rightCompartment = mutableSetOf<Char>()
        while (left < middle && right >= middle) {
            val leftItem = rucksackLoad[left]
            if (leftItem in rightCompartment) {
                itemPrioritySum += getItemPriority(leftItem)
                continue@rucksack
            }
            leftCompartment.add(leftItem)

            val rightItem = rucksackLoad[right]
            if (rightItem in leftCompartment) {
                itemPrioritySum += getItemPriority(rightItem)
                continue@rucksack
            }
            rightCompartment.add(rightItem)

            left++
            right--
        }
    }

    println("Item priority sum: $itemPrioritySum")
}

private fun solutionPart2(rucksackContents: List<String>) {
    println("Day 03 Solution - Part 2")
    var groupBadgeSum = 0

    rucksackContents.chunked(3) {
        val (ruckSack1, ruckSack2, ruckSack3) = it.map { contents -> contents.toSet() }
        for (item in ruckSack1) {
            if (item in ruckSack2 && item in ruckSack3) {
                groupBadgeSum += getItemPriority(item)
                break
            }
        }
    }

    println("Group badge sum: $groupBadgeSum")
}

private fun getItemPriority(item: Char): Int {
    val lowerCaseOffset = 1
    val upperCaseOffset = 27

    return if (item.isLowerCase()) item - 'a' + lowerCaseOffset else item - 'A' + upperCaseOffset
}
