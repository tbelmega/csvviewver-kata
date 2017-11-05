package de.belmega.fizzbuzz

fun fizzBuzz(i: Int): String {
    if (isBuzz(i) && isFizz(i)) return "fizzbuzz"
    if (isBuzz(i)) return "buzz"
    if (isFizz(i)) return "fizz"
    return i.toString()
}

private fun isFizz(i: Int) = i % 3 == 0

private fun isBuzz(i: Int) = i % 5 == 0
