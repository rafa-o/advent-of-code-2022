package day02

import java.io.File

fun main() {
    val input = parseInput()
    solutionPart1(strategyGuide = input)
    solutionPart2(strategyGuide = input)
}

private fun parseInput(): String {
    val filePath = "${System.getProperty("user.dir")}/src/main/kotlin/day02/Day02Input.txt"
    val strategyGuide = File(filePath).readText(Charsets.UTF_8)

    return strategyGuide.trim()
}

private enum class RockPaperScissors(val score: Int) {
    ROCK(1),
    PAPER(2),
    SCISSORS(3);

    companion object {
        // given a shape, maps what shape wins against it (left) and what shape loses against it (right)
        val winningAndLosingPair = mapOf(
            ROCK to Pair(PAPER, SCISSORS),
            PAPER to Pair(SCISSORS, ROCK),
            SCISSORS to Pair(ROCK, PAPER)
        )
    }
}

private fun Char.toGameHand(): RockPaperScissors {
    return when (this) {
        'A', 'X' -> RockPaperScissors.ROCK
        'B', 'Y' -> RockPaperScissors.PAPER
        'C', 'Z' -> RockPaperScissors.SCISSORS
        else -> throw IllegalArgumentException("char $this cannot be parsed into Rock Paper Scissors game hand")
    }
}

private fun playGame(mainHand: RockPaperScissors, opponentHand: RockPaperScissors): Int {
    val (winningShape, losingShape) = RockPaperScissors.winningAndLosingPair[mainHand]!!

    return when (opponentHand) {
        winningShape -> 0 + mainHand.score
        losingShape -> 6 + mainHand.score
        else -> 3 + mainHand.score
    }
}

private fun solutionPart1(strategyGuide: String) {
    println("Day 02 Solution")
    val games = strategyGuide.split("\n")
    var myTotalScore = 0
    games.forEach { game ->
        val (opponentHand, myHand) = game.split(" ").map { it[0].toGameHand() }
        val score = playGame(myHand, opponentHand)
        myTotalScore += score
    }

    println("My total score: $myTotalScore")
}

private fun solutionPart2(strategyGuide: String) {
    println("Day 02 Solution - Part 2")

    val games = strategyGuide.split("\n")
    var myTotalScore = 0
    games.forEach { game ->
        val (opponentHand, strategy) = game.split(" ").map { it[0] }
        println("opponent hand: $opponentHand, strategy: $strategy")

        val score = playWithStrategyGuide(opponentHand, strategy)
        myTotalScore += score
    }

    println("My total score: $myTotalScore")
}

private fun playWithStrategyGuide(opponentGame: Char, strategy: Char): Int {
    val opponentHand = opponentGame.toGameHand()
    val (winningHand, losingHand) = RockPaperScissors.winningAndLosingPair[opponentHand]!!

    val myHand = when (strategy) {
        'X' -> losingHand
        'Y' -> opponentGame.toGameHand()
        'Z' -> winningHand
        else -> throw IllegalArgumentException("Strategy $strategy not recognized.")
    }

    return playGame(myHand, opponentHand)
}
