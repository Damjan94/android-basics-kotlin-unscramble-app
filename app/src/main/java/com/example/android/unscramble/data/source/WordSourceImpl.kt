package com.example.android.unscramble.data.source

import android.content.res.XmlResourceParser
import android.util.Log
import com.example.android.unscramble.data.model.Word
import com.example.android.unscramble.domain.use_case.xml_parser.ParseXmlUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ClosedReceiveChannelException
import java.util.concurrent.ConcurrentSkipListSet
import kotlin.random.Random

class WordSourceImpl(
    private val parseWordXml: ParseXmlUseCase,
    private val random: Random
): WordSource {
    private var wordSet = ConcurrentSkipListSet<Word>()//mutableSetOf<Word>()
    override val wordCount: Int
        get() = wordSet.size

    private val channel = Channel<Word>(Channel.CONFLATED)

    override suspend fun parseDatabase(document: XmlResourceParser) {
        // this is created in a static context. Make sure we haven't already parsed the database
        if(wordSet.isNotEmpty()) {
            return
        }
        parseWordXml(document).collect {
            wordSet.addAll(it)
            channel.send(it.random(random))
        }
        channel.close()
    }
    override suspend fun getRandomWord(): Word {
        Log.d("hello", "word set size = ${wordSet.size}")
        return try {
            channel.receive()
        } catch (_: ClosedReceiveChannelException) {
            wordSet.random(random)
        }
    }
}