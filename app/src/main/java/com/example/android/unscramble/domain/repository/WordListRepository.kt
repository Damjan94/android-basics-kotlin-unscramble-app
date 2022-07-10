package com.example.android.unscramble.domain.repository

import com.example.android.unscramble.data.model.Word

interface WordListRepository {
    suspend fun getRandomWord(): Word
    fun getRepoSize(): Int
}