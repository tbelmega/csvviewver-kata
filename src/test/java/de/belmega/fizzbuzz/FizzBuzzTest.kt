package de.belmega.fizzbuzz

import org.junit.Test

class FizzBuzzTest {
    @Test
    fun testThat1Returns1() {
        assert(fizzBuzz(1) == "1")
    }

    @Test
    fun testThat3ReturnsFizz() {
        assert(fizzBuzz(3) == "fizz")
    }

    @Test
    fun testThat5ReturnsBuzz() {
        assert(fizzBuzz(5) == "buzz")
    }

    @Test
    fun testThat6ReturnsFizz() {
        assert(fizzBuzz(6) == "fizz")
    }

    @Test
    fun testThat10ReturnsBuzz() {
        assert(fizzBuzz(10) == "buzz")
    }

    @Test
    fun testThat15ReturnsFizzBuzz() {
        assert(fizzBuzz(15) == "fizzbuzz")
    }
}



