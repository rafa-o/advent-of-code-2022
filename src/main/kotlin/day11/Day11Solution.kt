package day11

import java.io.File

fun main() {
    val input = parseInput()
    solutionPart1(input)
    solutionPart2(input)
}

private fun parseInput(): List<String> {
    val filePath = "${System.getProperty("user.dir")}/src/main/kotlin/day11/Day11Input.txt"
    val notes = File(filePath).readText(Charsets.UTF_8)
        .trim()
        .split("\n\n")

    return notes
}

private fun solutionPart1(notes: List<String>) {
    println("Day 11 Solution")
    val monkeys = getMonkeys(notes)
    playKeepAwayGame(monkeys = monkeys, rounds = 20, reliefFactor = 3L, mod = null)

    val monkeyBusinessLevel = monkeys.map { it.inspections }
        .sortedDescending()
        .take(2)
        .reduce { acc, next -> acc * next }

    println("Monkey business level: $monkeyBusinessLevel")
}

private fun solutionPart2(notes: List<String>) {
    println("Day 11 Solution - Part 2")
    val monkeys = getMonkeys(notes)
    val mod = monkeys.map { it.divisibleBy }
        .fold(1L) { acc, next -> acc * next }

    playKeepAwayGame(monkeys = monkeys, rounds = 10_000, reliefFactor = 1L, mod = mod)

    val monkeyBusinessLevel = monkeys.map { it.inspections }
        .sortedDescending()
        .take(2)
        .reduce { acc, next -> acc * next }

    println("Monkey business level: $monkeyBusinessLevel")
}

private fun getMonkeys(notes: List<String>): Array<Monkey> {
    val monkeys = notes.map { s ->
        val (items, operation, test, ifTrue, ifFalse) = s.split("\n").drop(1).map { it.substringAfter(": ").trim() }

        val startingItems = items.split(", ").map { it.toLong() }.toMutableList()
        val divisibleBy = test.substring(test.lastIndexOf(" ")).trim().toLong()
        val ifTrueMonkey = ifTrue.substring(ifTrue.lastIndexOf(" ")).trim().toInt()
        val ifFalseMonkey = ifFalse.substring(ifFalse.lastIndexOf(" ")).trim().toInt()

        val (_, operator, operand) = operation.substringAfter(" = ").split(" ")

        val getWorryValueFunc = when (operator[0]) {
            '*' -> { a: Long -> a * if (operand == "old") a else operand.toLong() }
            '+' -> { a: Long -> a + if (operand == "old") a else operand.toLong() }
            else -> throw IllegalArgumentException("Unsupported operator $operator")
        }

        val getWorryValueWithModFunc = when (operator[0]) {
            '*' -> { a: Long, mod: Long -> (a * if (operand == "old") a else operand.toLong()) % mod }
            '+' -> { a: Long, mod: Long -> (a + if (operand == "old") a else operand.toLong()) % mod }
            else -> throw IllegalArgumentException("Unsupported operator $operator")
        }

        val getDestinationMonkeyFunc = { a: Long -> if (a % divisibleBy == 0L) ifTrueMonkey else ifFalseMonkey }

        Monkey(
            items = startingItems,
            getWorryValue = getWorryValueFunc,
            getDestinationMonkey = getDestinationMonkeyFunc,
            getWorryValueWithMod = getWorryValueWithModFunc,
            divisibleBy = divisibleBy
        )
    }

    return monkeys.toTypedArray()
}

private fun playKeepAwayGame(monkeys: Array<Monkey>, rounds: Int, reliefFactor: Long, mod: Long?) {
    repeat(rounds) {
        for (monkey in monkeys) {
            while (monkey.items.isNotEmpty()) {
                val item = monkey.items.removeFirst()
                val worryLevel = if (mod != null) {
                    monkey.getWorryValueWithMod(item, mod)
                } else monkey.getWorryValue(item) / reliefFactor

                val destinationMonkeyIdx = monkey.getDestinationMonkey(worryLevel)
                monkey.inspections++
                monkeys[destinationMonkeyIdx].items.add(worryLevel)
            }
        }
    }
}

private data class Monkey(
    val items: MutableList<Long>,
    val getWorryValue: (Long) -> Long,
    val getWorryValueWithMod: (Long, Long) -> Long,
    val getDestinationMonkey: (Long) -> Int,
    var inspections: Long = 0L,
    val divisibleBy: Long // only used in Part 2
)
