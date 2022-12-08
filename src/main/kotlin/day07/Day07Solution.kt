package day07

import java.io.File
import java.util.ArrayDeque

val dirSizes = mutableMapOf<String, Int>()
val children = mutableMapOf<String, MutableList<String>>()
const val MAX_DIR_SIZE = 100_000

fun main() {
    val input = parseInput()
    solutionPart1(input)
    solutionPart2(input)
}

private fun parseInput(): String {
    val filePath = "${System.getProperty("user.dir")}/src/main/kotlin/day07/Day07Input.txt"
    val terminalOutput = File(filePath).readText(Charsets.UTF_8)
        .trim()

    return terminalOutput
}

private fun solutionPart1(terminalOutput: String) {
    println("Day 07 Solution")

    val paths = ArrayDeque<String>()
    val blocks = terminalOutput.drop(2).split("\n$ ")
    blocks.forEach {
        parseLine(it, paths)
    }

    var dirSizeSum = 0
    for (dir in dirSizes.keys) {
        val dirSize = dfs(dir)
        if (dirSize <= MAX_DIR_SIZE) dirSizeSum += dirSize
    }

    println("Sum of directory sizes: $dirSizeSum")
}

private fun solutionPart2(terminalOutput: String) {
    println("Day 07 Solution - Part 2")
    dirSizes.clear()
    children.clear()

    val paths = ArrayDeque<String>()
    val blocks = terminalOutput.drop(2).split("\n$ ")
    blocks.forEach {
        parseLine(it, paths)
    }

    val totalDiskSpace = 70_000_000
    val spaceNeededForUpdate = 30_000_000
    val totalUsedSpace = dfs("/")
    val freeSpace = totalDiskSpace - totalUsedSpace

    val neededSpace = spaceNeededForUpdate - freeSpace

    var smallestSuitableDirSize = Int.MAX_VALUE
    for (dir in dirSizes.keys) {
        val dirSize = dfs(dir)
        if (dirSize >= neededSpace) {
            smallestSuitableDirSize = smallestSuitableDirSize.coerceAtMost(dirSize)
        }
    }

    println("Smallest dir to be deleted has size: $smallestSuitableDirSize")
}

private fun parseLine(line: String, paths: ArrayDeque<String>) {
    val split = line.split("\n")
    if (split.size == 1) {
        // cd command
        changeDirectory(split[0], paths)
    } else {
        // ls command
        assert(split[0] == "ls")
        parseListFileCommand(split.drop(1), paths)
    }
}

private fun changeDirectory(changeDirectoryCommand: String, paths: ArrayDeque<String>) {
    val (_, destinationDir) = changeDirectoryCommand.split(" ")
    if (destinationDir == "..") {
        paths.pop()
    } else {
        paths.push(destinationDir)
    }
}

private fun parseListFileCommand(listContents: List<String>, paths: ArrayDeque<String>) {
    val absPath = paths.reversed().joinToString("/")
    var fileSizeSum = 0

    listContents.forEach { result ->
        if (result.startsWith("dir")) {
            val dirName = result.split(" ")[1]
            children.getOrPut(absPath) { mutableListOf() }.add("$absPath/$dirName")
        } else {
            fileSizeSum += result.split(" ")[0].toInt()
        }
    }

    dirSizes[absPath] = fileSizeSum
}

private fun dfs(absolutePath: String): Int {
    var totalSize = dirSizes[absolutePath] ?: 0
    children[absolutePath]?.forEach { child -> totalSize += dfs(child) }

    return totalSize
}
