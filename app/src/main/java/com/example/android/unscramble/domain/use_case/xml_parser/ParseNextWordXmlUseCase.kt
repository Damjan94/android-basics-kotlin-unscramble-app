package com.example.android.unscramble.domain.use_case.xml_parser

import android.content.res.XmlResourceParser
import android.util.Log
import com.example.android.unscramble.data.model.Word

class ParseNextWordXmlUseCase(
    private val nextTag: GoToNextTagUseCase
) {

    operator fun invoke(document: XmlResourceParser): Word {
        nextTag(document, "entry")
        val id = document.getAttributeIntValue(0, -1)
        nextTag(document, "word")
        document.next()
        val wordString = document.text
        nextTag(document, "description")
        document.next()
        val description = document.text
        nextTag(document, "entry"){
            it.attributeCount == 3 && it.getAttributeValue(0) == "url"
        } // go to the ending tag
        var url = ""
        if(document.getAttributeValue(0) == "url") {
            document.next()
            url = document.text
        }else {
            document.next() // go one step further to avoid waiting forever on the ending tag
        }
        Log.d("Parsing Xml", "Currently parsing $id\t$wordString")
        return Word(
            id,
            wordString,
            description,
            url
        )
    }
}