package day14

import java.io.File
import kotlin.math.sign

/*
 * TODO: replace with Kotlin's Complex Number library (http://kotlinmath.org/2020/03/03/complex-numbers/)
 *  based on hyper-neutrino's video (https://www.youtube.com/watch?v=Uf_IF_3RbKw)
 *
 */
fun main() {
    val input = parseInput()
    solutionPart1(input)
    solutionPart2(input)
}

private fun parseInput(): List<String> {
    val filePath = "${System.getProperty("user.dir")}/src/main/kotlin/day14/Day14Input.txt"
    val input = File(filePath).readText(Charsets.UTF_8)
        .trim()
        .split("\n")

    return input
}

private fun solutionPart1(input: List<String>) {
    println("Day 14 Solution")
    println(input)

    val cave = createCave(input)
    val source = Point.from("500,0")
    val bottom = cave.maxOf { it.y }

    val restingUnitsOfSand = cave.fillSand(finishWhen = { it.y >= bottom }, source = source)
    println("Units of sand that come to rest: $restingUnitsOfSand")
}

private fun solutionPart2(input: List<String>) {
    println("Day 14 Solution - Part 2")

    val cave = createCave(input)
    val source = Point.from("500,0")
    val bottom = cave.maxOf { it.y }

    val restingUnitsOfSand = cave.fillSand(restWhen = { it.y > bottom }, finishWhen = { it == source }, source = source)
    println("Units of sand that come to rest: $restingUnitsOfSand")
}

// solution implemented by andilau (https://github.com/andilau/advent-of-code-2022)
data class Point(val x: Int, val y: Int) {
    fun lineto(to: Point): Sequence<Point> {
        val dx = (to.x - x).sign
        val dy = (to.y - y).sign

        return generateSequence(this) {
            if (it == to) null
            else it + Point(dx, dy)
        }
    }

    private operator fun plus(other: Point) = Point(x + other.x, y + other.y)
    override fun toString(): String {
        return "P($x, $y)"
    }

    companion object {
        fun from(line: String) = line
            .split(",")
            .map(String::toInt)
            .let { (x, y) -> Point(x, y) }
    }
}

private fun createCave(walls: List<String>) = walls.flatMap {
    it.split(" -> ")
        .map { Point.from(it) }
        .zipWithNext()
        .flatMap { (from, to) -> from.lineto(to) }
}.toSet()

private fun Set<Point>.fillSand(
    restWhen: (Point) -> Boolean = { false },
    finishWhen: (Point) -> Boolean = { false },
    source: Point
): Int {
    var sandAtRest = 0
    val occupied = this.toMutableSet()
    while (true) {
        var current = source
        while (true) {
            val next = sequenceOf(current.down(), current.downLeft(), current.downRight())
                .firstOrNull { it !in occupied }

            when {
                next == null || restWhen(current) -> {
                    occupied += current
                    sandAtRest++
                    if (finishWhen(current)) return sandAtRest
                    else break
                }

                finishWhen(next) -> return sandAtRest

                else -> current = next
            }
        }
    }
}

private fun Point.down(): Point = copy(y = y + 1)
private fun Point.downLeft(): Point = copy(x = x - 1, y = y + 1)
private fun Point.downRight(): Point = copy(x = x + 1, y = y + 1)
