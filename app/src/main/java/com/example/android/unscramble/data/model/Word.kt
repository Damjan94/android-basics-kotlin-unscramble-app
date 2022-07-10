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

    class Builder {
        private var id = -1
        private var description = ""
        private var word = ""
        private var url = ""

        fun setId(id: Int):Builder {
            this.id = id
            return this
        }
        fun setDescription(description: String): Builder {
            this.description = description
            return this
        }

        fun setWord(word: String): Builder {
            this.word = word
            return this
        }
        fun setUrl(url: String): Builder {
            this.url = url
            return this
        }

        fun build(): Word {
            val builtWord = Word(id, word, description, url)
            id = -1
            word = ""
            description = ""
            url = ""
            return builtWord
        }
    }
}