package com.example.android.unscramble.domain.use_case.xml_parser

import android.content.res.XmlResourceParser
import com.example.android.unscramble.data.model.Word
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ParseXmlUseCase(
    private val nextElement: ParseAllElements
) {
    private var maxItemsBeforeEmit = 100

    suspend operator fun invoke(document: XmlResourceParser): Flow<MutableSet<Word>> = flow {
        var wordSet = mutableSetOf<Word>()
        var addedItemCount = 0

        val wordBuilder = Word.Builder()

        nextElement(document, TextHandlers(
            idTextHandler = {id ->
                assert(id != -1)
                wordBuilder.setId(id)
            },
            wordTextHandler = {word ->
                wordBuilder.setWord(word)
            },
            descriptionTextHandler = { description ->
                wordBuilder.setDescription(description)
            },
            urlTextHandler = {url ->
                wordBuilder.setUrl(url)
            },
            doneWithElement = {
                val word = wordBuilder.build()
                if(addedItemCount > maxItemsBeforeEmit) {
                    emit(wordSet)
                    wordSet = mutableSetOf()
                    addedItemCount = 0
                    maxItemsBeforeEmit = (maxItemsBeforeEmit * 1.5).toInt()
                }
                wordSet.add(word)
                ++addedItemCount
            }
        ))

        emit(wordSet)
    }
}
