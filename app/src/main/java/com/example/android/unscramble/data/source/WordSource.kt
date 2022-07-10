package com.example.android.unscramble.data.source

import android.content.res.XmlResourceParser
import com.example.android.unscramble.data.model.Word

interface WordSource {

    val wordCount: Int

    suspend fun parseDatabase(document: XmlResourceParser)

    suspend fun getRandomWord(): Word
}