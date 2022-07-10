package com.example.android.unscramble.ui.game

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.android.unscramble.data.model.Word
import com.example.android.unscramble.domain.use_case.NextWordUseCase
import com.example.android.unscramble.domain.use_case.ScrambleWordUseCase
import kotlinx.coroutines.launch

class GameViewModel(
        private val nextWordUseCase: NextWordUseCase,
        private val scrambleWordUseCase: ScrambleWordUseCase
    ): ViewModel(){
    var score = 0
        private set
    var currentWordCount = 0
        private set


    var currentScrambledWord = MutableLiveData<String>()
        private set
    lateinit var currentWord: Word
        private set
    var guessFailed = false
        private set
    init {
        updateWords()
    }

    fun nextWord(playerWord: String){
        guessFailed = playerWord != currentWord.word
        if(guessFailed) {
            return
        }
        updateWords()
        ++currentWordCount
        score += SCORE_INCREASE
    }

    private fun updateWords() {
        viewModelScope.launch {
            currentWord = nextWordUseCase()
            currentScrambledWord.value = scrambleWordUseCase(currentWord.word)
        }
    }
    fun restartGame() {
        currentWordCount = 0
        score = 0
        guessFailed = false
        updateWords()
    }

    fun skipWord() {
        updateWords()
        ++currentWordCount
    }

    fun isGameOver(): Boolean {
        return currentWordCount >= MAX_NO_OF_WORDS
    }

    class Factory(
            private val nextWordUseCase: NextWordUseCase,
            private val scrambleWordUseCase: ScrambleWordUseCase

        ): ViewModelProvider.Factory{
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if(modelClass.isAssignableFrom(GameViewModel::class.java)){
                return GameViewModel(nextWordUseCase, scrambleWordUseCase) as T
            }
            throw java.lang.IllegalStateException("Couldn't create ${modelClass} with ${GameViewModel::class.java}")
        }
    }
}