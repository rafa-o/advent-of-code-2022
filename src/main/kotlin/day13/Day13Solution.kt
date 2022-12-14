package day13

import java.io.File

fun main() {
    val input = parseInput()
    solutionPart1(input)
    solutionPart2(input)
}

private fun parseInput(): List<String> {
    val filePath = "${System.getProperty("user.dir")}/src/main/kotlin/day13/Day13Input.txt"
    val pairs = File(filePath).readText(Charsets.UTF_8)
        .trim()
        .split("\n")

    return pairs
}

private fun solutionPart1(lines: List<String>) {
    println("Day 13 Solution")

    val packets = lines.filter { it.isNotBlank() }
        .map { it.parsePacketData() }
    val sum = packets.chunked(2).mapIndexed { i, it -> if (it[0] < it[1]) i + 1 else 0 }.sum()
    println("Sum of indexes of packets in the right order: $sum")
}

private fun solutionPart2(lines: List<String>) {
    println("Day 13 Solution - Part 2")

    val packets = lines.filter { it.isNotBlank() }
        .map { it.parsePacketData() }

    val two = "[[2]]".parsePacketData()
    val six = "[[6]]".parsePacketData()
    val sortedPackets = (packets + listOf(two, six)).sorted()

    val decoderKey = (sortedPackets.indexOf(two) + 1) * (sortedPackets.indexOf(six) + 1)
    println("Decoder key: $decoderKey")
}

// got this solution from github user dfings (dfings/advent-of-code)
interface PacketData : Comparable<PacketData>

private data class ListValue(val values: List<PacketData>) : PacketData {
    override fun toString() = values.toString()

    override operator fun compareTo(other: PacketData): Int = when (other) {
        is ListValue -> values.zip(other.values).asSequence()
            .map { (a, b) -> a.compareTo(b) }
            .firstOrNull { it != 0 } ?: values.size.compareTo(other.values.size)
        else -> compareTo(ListValue(listOf(other)))
    }
}

private data class IntValue(val value: Int) : PacketData {
    override fun toString() = value.toString()
    override operator fun compareTo(other: PacketData): Int = when (other) {
        is IntValue -> value.compareTo(other.value)
        else -> ListValue(listOf(this)).compareTo(other)
    }
}

private fun String.parsePacketData() = ArrayDeque(toList()).parsePacketData()
private fun ArrayDeque<Char>.parsePacketData(): PacketData {
    if (first() != '[') return IntValue(joinToString("").toInt())

    removeFirst()
    val values = mutableListOf<PacketData>()
    while (size > 1) {
        val text = ArrayDeque<Char>()
        var braceCounter = 0
        while (true) {
            val character = removeFirst()
            when (character) {
                '[' -> braceCounter++
                ']' -> if (--braceCounter < 0) break
                ',' -> if (braceCounter == 0) break
            }
            text.add(character)
        }
        values.add(text.parsePacketData())
    }
    return ListValue(values)
}
