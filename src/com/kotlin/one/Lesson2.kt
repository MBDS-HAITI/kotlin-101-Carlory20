package com.android.com.kotlin.one

fun greet(name: String = "Student"): String {
    //TODO("Write a function that greets someone by name.")
    return name
}

fun printInfo(name: String, age: Int = 18, city: String = "Paris") {
   //TODO("Print user info, with some default values. In the format: $name is $age years old and lives in $city.")
    println("$name is $age years old and lives in $city")
}

fun add(a: Int, b: Int): Int {
    //TODO("Function that adds two numbers and returns the result.")
  return  a + b
}

fun isEven(number: Int): Boolean {
    //TODO("Check if a number is even.")
    return number % 2 ==0
}

fun areaOfCircle(radius: Double): Double {
    //TODO( "Compute area of a circle using π * r².")
    val pi = 3.14
    return pi * radius * radius
}

// TODO 5: Return a letter grade based on score.
fun grade(score: Int): String {
//    TODO(
//        "Return a letter grade based on score. \uD83D\uDD27 Use if or when" +
//                "- Score >= 90: 'A'\n" +
//                "- Score >= 80: 'B'\n" +
//                "- Score >= 70: 'C'\n" +
//                "- Score >= 60: 'D'\n" +
//                "- Below 60: 'F'"
//    )
    return when {
        score >= 90 -> "A"
        score >= 80 -> "B"
        score >= 70 -> "C"
        score >= 60 -> "D"
        else -> "F"
    }
}

fun maxOfThree(a: Int, b: Int, c: Int): Int {
    //TODO("Return the maximum of three numbers.")
    return maxOf(a, maxOf(b, c))
}

fun toFahrenheit(celsius: Double): Double {
    //TODO("Convert Celsius to Fahrenheit.")
    return (celsius * 9.0 / 5.0) + 32.0
}


fun applyDiscount(price: Double, discount: Double = 0.1): Double {
    //TODO("Apply a discount (default 10%) to a price.")
    return price * (1.0 - discount)
}


fun capitalizeWords(sentence: String): String {
    //TODO("Capitalize the first letter of each word in a sentence.")
    return sentence
        .trim()
        .split(Regex("\\s+"))
        .filter { it.isNotBlank() }
        .joinToString(" ") { word ->
            word.lowercase().replaceFirstChar { ch ->
                if (ch.isLowerCase()) ch.titlecase() else ch.toString()
            }
        }
}

fun bmi(weight: Double, height: Double): Double {
    //TODO("Compute BMI using the formula: weight / height²")
    return weight / (height * height)
}

fun passwordStrength(password: String): Boolean {
//    TODO(
//        "Check password strength:\n" +
//                "- At least 8 characters\n" +
//                "- Contains uppercase letter\n" +
//                "- Contains lowercase letter\n" +
//                "- Contains a number"
//    )
    if (password.length < 8) return false
    val hasUpper = password.any { it.isUpperCase() }
    val hasLower = password.any { it.isLowerCase() }
    val hasDigit = password.any { it.isDigit() }
    return hasUpper && hasLower && hasDigit
}

fun filterEvenNumbers(numbers: List<Int>): List<Int> {
    //TODO("Return a list of even numbers from the input list.")
    return numbers.filter { it % 2 == 0 }
}


fun factorial(n: Int): Int {
    //TODO("Compute the factorial of a number n recursively.")
    require(n >= 0) { "n must be >= 0" }
    return if (n <= 1) 1 else n * factorial(n - 1)
}

fun fibonacci(n: Int): Int {
    //TODO("Return the nth Fibonacci number using recursion.")
    require(n >= 0) { "n must be >= 0" }
    return when (n) {
        0 -> 0
        1 -> 1
        else -> fibonacci(n - 1) + fibonacci(n - 2)
    }
}


// TODO 19: Simple calculator using when expression.
fun miniCalculator() {
    //TODO("Create a simple calculator that takes two numbers and an operator (+, -, *, /) from the user and prints the result.")

    /*
    Example
    println("Enter first number:")
    val a = readln().toDouble()
     */
    print("Enter first number: ")
    val a = readln().toDouble()

    print("Enter operator (+, -, *, /): ")
    val op = readln().trim()

    print("Enter second number: ")
    val b = readln().toDouble()

    val result = when (op) {
        "+" -> a + b
        "-" -> a - b
        "*" -> a * b
        "/" -> {
            if (b == 0.0) {
                println("Cannot divide by zero.")
                return
            } else a / b
        }
        else -> {
            println("Invalid operator.")
            return
        }
    }

    println("Result: $result")
}

// TODO 20: Text analyzer.
fun analyzeText(text: String): Map<String, Any> {
//    TODO(
//        "Analyze the text and return statistics:\n" +
//                "- Character count\n" +
//                "- Word count\n" +
//                "- Longest word\n" +
//                "- Average word length"
//    )
    val charCount = text.length

    val words = text
        .trim()
        .split(Regex("\\s+"))
        .filter { it.isNotBlank() }

    val wordCount = words.size
    val longestWord = words.maxByOrNull { it.length } ?: ""
    val averageWordLength = if (wordCount == 0) 0.0 else words.sumOf { it.length }.toDouble() / wordCount

    return mapOf(
        "charCount" to charCount,
        "wordCount" to wordCount,
        "longestWord" to longestWord,
        "averageWordLength" to averageWordLength
    )
}


fun main() {
    println("🔍 Running Kotlin Functions Playground Tests...\n")

    var passed = 0
    var failed = 0

    fun verify(name: String, block: () -> Boolean) {
        try {
            check(block()) { "❌ Test failed: $name" }
            println("✅ $name")
            passed++
        } catch (e: Throwable) {
            println("❌ $name → ${e.message}")
            failed++
        }
    }

    // 🟢 LEVEL 1
    verify(name = "greet() with default") { greet() == "Student" }
    verify(name = "greet(\"Alice\")") { greet("Alice") == "Alice" }
    verify("printInfo with all defaults") {
        printInfo("Bob")
        true // Just checking it runs without error
    }
    verify("add(3,5) == 8") { add(3, 5) == 8 }
    verify("isEven(4) == true") { isEven(4) }
    verify("isEven(7) == false") { !isEven(7) }
    verify("areaOfCircle(2.0) ≈ 12.57") {
        val result = areaOfCircle(2.0)
        result in 12.56..12.58
    }

    // 🟡 LEVEL 2
    verify("grade(95) == 'A'") { grade(95) == "A" }
    verify("grade(82) == 'B'") { grade(82) == "B" }
    verify("maxOfThree(3,9,6) == 9") { maxOfThree(3, 9, 6) == 9 }
    verify("toFahrenheit(20.0) == 68.0") { (toFahrenheit(20.0) - 68.0).absoluteValue < 0.1 }

    // 🟠 LEVEL 3
    verify("applyDiscount(100.0) == 90.0") { (applyDiscount(100.0) - 90.0).absoluteValue < 0.001 }
    verify("applyDiscount(100.0, 0.2) == 80.0") { (applyDiscount(100.0, 0.2) - 80.0).absoluteValue < 0.001 }

    // 🟣 LEVEL 4
    verify("capitalizeWords works") { capitalizeWords("hello kotlin world") == "Hello Kotlin World" }
    verify("bmi(70,1.75) ≈ 22.86") { bmi(70.0, 1.75) in 22.8..22.9 }
    verify("passwordStrength detects strong") { passwordStrength("MyPass123") }
    verify("passwordStrength detects weak") { !passwordStrength("weak") }
    verify("filterEvenNumbers works") {
        filterEvenNumbers(listOf(1, 2, 3, 4, 5, 6)) == listOf(2, 4, 6)
    }

    // ⚫ LEVEL 5
    verify("factorial(5) == 120") { factorial(5) == 120 }
    verify("fibonacci(6) == 8") { fibonacci(6) == 8 }

    // 🧠 LEVEL 7
    verify("analyzeText stats") {
        val result = analyzeText("Kotlin is fun and powerful")
        result["charCount"] == 26 &&
                result["wordCount"] == 5 &&
                result["longestWord"] == "powerful" &&
                (result["averageWordLength"] as Double) in 4.0..5.0
    }

    println("\n🎯 TEST SUMMARY: $passed passed, $failed failed.")
    if (failed == 0) println("🎉 All tests passed! Great job!")
    else println("⚠️  Some tests failed. Keep debugging!")
}

// Simple helper for double comparison
private val Double.absoluteValue get() = if (this < 0) -this else this


