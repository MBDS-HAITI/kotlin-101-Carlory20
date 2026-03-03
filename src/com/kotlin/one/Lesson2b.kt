package com.android.com.kotlin.one


// Exercise 0: Test setup
fun runtest(name: String, block: () -> Boolean) {
    try {
        check(block()) { " Test failed: $name" }
        println(" $name")
    } catch (e: Throwable) {
        println(" $name → ${e.message}")
    }
}

// Exercise 1 — Immutable List
fun ex1(): List<Int> {
    // immutable list of 5 integers
    return listOf(1, 2, 3, 4, 5)
}

// Exercise 2 — Mutable List
fun ex2(): MutableList<String> {
    val items = mutableListOf("Kotlin", "Java", "Swift")
    items.add("Python")
    return items
}

// Exercise 3 — Filter Even
fun ex3(): List<Int> {
    return (1..10).toList().filter { it % 2 == 0 }
}

// Exercise 4 — Filter and Map
fun ex4(ages: List<Int>): List<String> {
    return ages
        .filter { it >= 18 }
        .map { "Adult: $it" }
}

// Exercise 5 — Flatten Nested Lists
fun ex5(): List<Int> {
    val nested = listOf(listOf(1, 2), listOf(3, 4), listOf(5))
    return nested.flatten()
}

// Exercise 6 — FlatMap
fun ex6(): List<String> {
    val phrases = listOf("Kotlin is fun", "I love lists")
    return phrases.flatMap { it.split(" ") }
}

// Exercise 7 — Eager Processing
fun ex7(): List<Long> {
    val start = System.currentTimeMillis()

    val result = (1..1_000_000)
        .toList()
        .filter { it % 3 == 0 }
        .map { it.toLong() * it.toLong() }
        .take(5)

    val end = System.currentTimeMillis()
    println("ex7 Time: ${end - start} ms")

    return result
}

// Exercise 8 — Lazy Processing
fun ex8(): List<Long> {
    val start = System.currentTimeMillis()

    val result = (1..1_000_000)
        .asSequence() // lazy
        .filter { it % 3 == 0 }
        .map { it.toLong() * it.toLong() }
        .take(5)
        .toList()

    val end = System.currentTimeMillis()
    println("ex8 Time: ${end - start} ms")

    return result
}

// Exercise 9 — Chain multiple operations
fun ex9(names: List<String>): List<String> {
    return names
        .filter { it.startsWith("A") }
        .map { it.uppercase() }
        .sorted()
}

// Quick test runner
fun main() {
    println(" Running Lesson2 List Exercises...\n")

    runtest("ex1 returns 5 ints") { ex1().size == 5 && ex1() == listOf(1,2,3,4,5) }
    runtest("ex2 adds 4th element") { ex2().size == 4 && ex2().contains("Python") }
    runtest("ex3 even 1..10") { ex3() == listOf(2,4,6,8,10) }

    val ages = listOf(10, 18, 25, 17, 40)
    runtest("ex4 filter+map ages") { ex4(ages) == listOf("Adult: 18", "Adult: 25", "Adult: 40") }

    runtest("ex5 flatten") { ex5() == listOf(1,2,3,4,5) }
    runtest("ex6 flatMap words") { ex6() == listOf("Kotlin","is","fun","I","love","lists") }

    runtest("ex7 eager first 5 squares of multiples of 3") {
        ex7() == listOf(9L, 36L, 81L, 144L, 225L)
    }

    runtest("ex8 lazy first 5 squares of multiples of 3") {
        ex8() == listOf(9L, 36L, 81L, 144L, 225L)
    }

    val names = listOf("Alice", "Bob", "Andrew", "anna", "Alex", "Charles")
    runtest("ex9 filter A + uppercase + sort") {
        ex9(names) == listOf("ALEX", "ALICE", "ANDREW")
    }

    println("\nDone.")
}