package com.example.android.unscramble.domain.use_case

import com.example.android.unscramble.data.model.Word
import com.example.android.unscramble.domain.repository.WordListRepository

class NextWordUseCase(private val wordRepository: WordListRepository) {

    private val usedWords = mutableListOf<Int>()

    suspend operator fun invoke(): Word {
        if(usedWords.size != 0 && usedWords.size == wordRepository.getRepoSize()) {
            assert(false) { "we have exhausted all unique words" }
            usedWords.clear() // avoid infinite loop by allowing the words to repeat
        }
        var word = wordRepository.getRandomWord()
        while(usedWords.contains(word.id)) {
            word = wordRepository.getRandomWord()
        }
        usedWords.add(word.id)
        return word
    }
}