package com.capital7software.aoc.lib.crypt

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings
import kotlin.math.max
import kotlin.math.min

/**
 * The computer system you're breaking into uses a weird scrambling function to store its
 * passwords. It shouldn't be much trouble to create your own scrambled password, so you can
 * add it to the system; you just have to implement the scrambler.
 *
 * The scrambling function is a series of operations (the exact list is provided in your
 * puzzle input). Starting with the password to be scrambled, apply each operation in
 * succession to the string. The individual operations behave as follows:
 *
 * - swap position X with position Y means that the letters at indexes X and Y
 * (counting from 0) should be **swapped.**
 * - swap letter X with letter Y means that the letters X and Y should be **swapped**
 * (regardless of where they appear in the string).
 * - rotate left/right X steps means that the whole string should be **rotated**;
 * for example, one right rotation would turn abcd into dabc.
 * - rotate based on position of letter X means that the whole string should
 * be **rotated to the right** based on the **index** of letter X (counting from 0)
 * as determined **before** this instruction does any rotations. Once the index is
 * determined, rotate the string to the right one time, plus a number of times equal
 * to that index, plus one additional time if the index was at least 4.
 * - reverse positions X through Y means that the span of letters at indexes X through
 * Y (including the letters at X and Y) should be **reversed in order.**
 * - move position X to position Y means that the letter which is at index X should
 * be **removed** from the string, then **inserted** such that it ends up at index Y.
 *
 * For example, suppose you start with abcde and perform the following operations:
 *
 * - swap position 4 with position 0 swaps the first and last letters, producing the
 * input for the next step, ebcda.
 * - swap letter d with letter b swaps the positions of d and b: edcba.
 * - reverse positions 0 through 4 causes the entire string to be reversed, producing abcde.
 * - rotate left 1-step shifts all letters left one position, causing the first letter
 * to wrap to the end of the string: bcdea.
 * - move position 1 to position 4 removes the letter at position 1 (c), then inserts
 * it at position 4 (the end of the string): bdeac.
 * - move position 3 to position 0 removes the letter at position 3 (a), then inserts
 * it at position 0 (the front of the string): abdec.
 * - rotate based on position of letter b finds the index of letter b (1), then rotates
 * the string right once plus a number of times equal to that index (2): ecabd.
 * - rotate based on position of letter d finds the index of letter d (4), then rotates
 * the string right once, plus a number of times equal to that index, plus an additional
 * time because the index was at least 4, for a total of 6 right rotations: decab.
 *
 * After these steps, the resulting scrambled password is decab.
 *
 */
class PasswordScrambler() {
  private val instructions = mutableListOf<Instruction>()

  /**
   * Instantiates a new [PasswordScrambler] with the specified salt and list of
   * scrambling instructions.
   *
   * @param instructions The [List] of scrambling instructions to execute.
   */
  @SuppressFBWarnings
  constructor(instructions: List<String>) : this() {
    this.instructions.addAll(instructions.map { parseInstruction(it) })
  }

  companion object {
    private val SWAP_POSITIONS = """swap position (\d+) with position (\d+)""".toRegex()
    private val SWAP_LETTERS = """swap letter (\w) with letter (\w)""".toRegex()
    private val ROTATE_LEFT = """rotate left (\d+) step""".toRegex()
    private val ROTATE_RIGHT = """rotate right (\d+) step""".toRegex()
    private val ROTATE_BASED_ON_POSITION =
        """rotate based on position of letter (\w)""".toRegex()
    private val REVERSE_POSITIONS = """reverse positions (\d+) through (\d+)""".toRegex()
    private val MOVE_POSITIONS = """move position (\d+) to position (\d+)""".toRegex()
  }

  private interface Instruction {
    fun execute(letters: CharArray)
    fun inverse(letters: CharArray) = execute(letters)
  }

  private data class SwapPositions(val first: Int, val second: Int) : Instruction {
    override fun execute(letters: CharArray) {
      val t = letters[first]
      letters[first] = letters[second]
      letters[second] = t
    }
  }

  private data class SwapLetters(val first: Char, val second: Char) : Instruction {
    override fun execute(letters: CharArray) {
      var i = -1
      var j = -1

      for (k in letters.indices) {
        if (letters[k] == first) {
          i = k
        }
        if (letters[k] == second) {
          j = k
        }
        if (i >= 0 && j >= 0) {
          break
        }
      }
      check(i >= 0 && j >= 0) { "Unable to swap letters" }

      val t = letters[i]
      letters[i] = letters[j]
      letters[j] = t
    }
  }

  private data class RotateLeft(val steps: Int) : Instruction {
    override fun execute(letters: CharArray) {
      val left = steps % letters.size

      if (left == 0 || left == letters.size) {
        return
      }

      val t = CharArray(left)
      letters.copyInto(t, 0, 0, left)

      for (i in left..<letters.size) {
        letters[i - left] = letters[i]
      }

      t.copyInto(letters, letters.size - left)
    }

    override fun inverse(letters: CharArray) {
      RotateRight(steps).execute(letters)
    }
  }

  private data class RotateRight(val steps: Int) : Instruction {
    override fun execute(letters: CharArray) {
      val right = steps % letters.size

      if (right == 0 || right == letters.size) {
        return
      }

      val t = CharArray(right)
      letters.copyInto(t, 0, letters.size - right)

      for (i in letters.size - 1 - right downTo 0) {
        letters[i + right] = letters[i]
      }

      t.copyInto(letters)
    }

    override fun inverse(letters: CharArray) {
      RotateLeft(steps).execute(letters)
    }
  }

  private data class RotateBasedOnPosition(val letter: Char) : Instruction {
    companion object {
      private val INV_MAP = mutableMapOf<Int, Int>().apply {
        put(0, 7)
        put(1, 7)
        put(2, 2)
        put(3, 6)
        put(4, 1)
        put(5, 5)
        put(6, 0)
        put(7, 4)
      }
    }

    override fun execute(letters: CharArray) {
      var index = -1

      for (i in letters.indices) {
        if (letters[i] == letter) {
          index = i
          break
        }
      }
      check(index >= 0) { "Unable to find index of $letter" }
      var steps = 1 + index
      if (index >= 4) {
        steps++
      }
      RotateRight(steps).execute(letters)
    }

    override fun inverse(letters: CharArray) {
      var index = -1

      for (i in letters.indices) {
        if (letters[i] == letter) {
          index = i
          break
        }
      }
      check(index >= 0) { "Unable to find index of $letter" }
      val steps = INV_MAP[index] ?: error("Unexpected password length!")
      RotateRight(steps).execute(letters)
    }
  }

  private data class ReversePositions(val start: Int, val end: Int) : Instruction {
    override fun execute(letters: CharArray) {
      var min = min(start, end)
      var max = max(start, end)

      while (min < max) {
        val t = letters[max]
        letters[max] = letters[min]
        letters[min] = t
        min++
        max--
      }
    }
  }

  private data class MovePositions(val source: Int, val destination: Int) : Instruction {
    override fun execute(letters: CharArray) {
      val t = letters[source]

      if (destination > source) {
        // move everything from source + 1 through destination to the left
        for (i in source + 1..destination) {
          letters[i - 1] = letters[i]
        }
      } else {
        // move everything from destination through source - 1 to the right
        for (i in source - 1 downTo destination) {
          letters[i + 1] = letters[i]
        }
      }

      letters[destination] = t
    }

    override fun inverse(letters: CharArray) {
      MovePositions(destination, source).execute(letters)
    }
  }

  private fun parseInstruction(instruction: String): Instruction {
    return when (instruction.substring(0, 10)) {
      "swap posit" -> {
        val match = SWAP_POSITIONS.find(instruction)!!
        SwapPositions(match.groups[1]!!.value.toInt(), match.groups[2]!!.value.toInt())
      }

      "swap lette" -> {
        val match = SWAP_LETTERS.find(instruction)!!
        SwapLetters(match.groups[1]!!.value[0], match.groups[2]!!.value[0])
      }

      "rotate lef" -> {
        val match = ROTATE_LEFT.find(instruction)!!
        RotateLeft(match.groups[1]!!.value.toInt())
      }

      "rotate rig" -> {
        val match = ROTATE_RIGHT.find(instruction)!!
        RotateRight(match.groups[1]!!.value.toInt())
      }

      "rotate bas" -> {
        val match = ROTATE_BASED_ON_POSITION.find(instruction)!!
        RotateBasedOnPosition(match.groups[1]!!.value[0])
      }

      "reverse po" -> {
        val match = REVERSE_POSITIONS.find(instruction)!!
        ReversePositions(match.groups[1]!!.value.toInt(), match.groups[2]!!.value.toInt())
      }

      "move posit" -> {
        val match = MOVE_POSITIONS.find(instruction)!!
        MovePositions(match.groups[1]!!.value.toInt(), match.groups[2]!!.value.toInt())
      }

      else -> error("Unknown instruction: $instruction")
    }
  }

  /**
   * For example, suppose you start with abcde and perform the following operations:
   *
   * - swap position 4 with position 0 swaps the first and last letters, producing the
   * input for the next step, ebcda.
   * - swap letter d with letter b swaps the positions of d and b: edcba.
   * - reverse positions 0 through 4 causes the entire string to be reversed, producing abcde.
   * - rotate left 1-step shifts all letters left one position, causing the first letter
   * to wrap to the end of the string: bcdea.
   * - move position 1 to position 4 removes the letter at position 1 (c), then inserts
   * it at position 4 (the end of the string): bdeac.
   * - move position 3 to position 0 removes the letter at position 3 (a), then inserts
   * it at position 0 (the front of the string): abdec.
   * - rotate based on position of letter b finds the index of letter b (1), then rotates
   * the string right once plus a number of times equal to that index (2): ecabd.
   * - rotate based on position of letter d finds the index of letter d (4), then rotates
   * the string right once, plus a number of times equal to that index, plus an additional
   * time because the index was at least 4, for a total of 6 right rotations: decab.
   *
   * After these steps, the resulting scrambled password is decab.
   *
   * @return The specified password scrambled using the scrambling instructions.
   */
  fun scramble(password: String): String {
    val chars = password.toCharArray()

    instructions.forEach {
      it.execute(chars)
    }

    return String(chars)
  }

  /**
   * Unscrambles the specified scrambled password.
   *
   * @return The password unscrambled using the inverse of the scrambling instructions.
   */
  fun unscramble(password: String): String {
    val chars = password.toCharArray()

    instructions.reversed().forEach {
      it.inverse(chars)
    }

    return String(chars)
  }
}
