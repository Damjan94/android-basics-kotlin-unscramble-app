package com.example.android.unscramble.domain.use_case.xml_parser

import android.content.res.XmlResourceParser
import com.example.android.unscramble.data.model.Word
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.yield

class ParseWordXmlUseCase(
    private val parseNextWord: ParseNextWordXmlUseCase,
    private val nextTag: GoToNextTagUseCase
) {
    val MAX_ITEM_COUNT_BEFORE_EMIT = 100

    suspend operator fun invoke(document: XmlResourceParser): Flow<MutableSet<Word>> = flow {
        var wordSet = mutableSetOf<Word>()
        nextTag(document, "entry")
        var addedItemCount = 0
        while (document.depth >= 3) {
            val word = parseNextWord(document)
            wordSet.add(word)
            ++addedItemCount
            if(addedItemCount > MAX_ITEM_COUNT_BEFORE_EMIT) {
                emit(wordSet)
                wordSet = mutableSetOf()
            }
            yield()
        }
        emit(wordSet)
    }
}
