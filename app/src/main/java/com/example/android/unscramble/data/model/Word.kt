package com.example.android.unscramble.data.model

data class Word(
    val id: Int,
    val word: String,
    val description: String,
    val url: String
): Comparable<Word> {
    override fun compareTo(other: Word): Int {
        return this.id.compareTo(other.id)
    }

}