package com.example.android.unscramble.domain.use_case

class ScrambleWordUseCase {
    operator fun invoke(word: String):String {
        val shuffled = word.toCharArray()
        shuffled.shuffle()
        while(String(shuffled) == word) {
            shuffled.shuffle()
        }
        return String(shuffled)
    }
}