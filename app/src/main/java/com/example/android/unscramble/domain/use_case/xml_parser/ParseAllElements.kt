package com.example.android.unscramble.domain.use_case.xml_parser

import android.content.res.XmlResourceParser

class ParseAllElements {

    private val elementName = "entry"

    suspend operator fun invoke(document: XmlResourceParser, textHandlers: TextHandlers) {
        var nextTextHandler: (String) -> Unit = {}
        var eventType = document.eventType
        while (eventType != XmlResourceParser.END_DOCUMENT) {
            if(eventType == XmlResourceParser.START_TAG) {
                if(document.name == elementName) {
                    textHandlers.idTextHandler(document.getAttributeIntValue(0, -1))
                } else if(document.name == "word") {
                    nextTextHandler = textHandlers.wordTextHandler
                } else if(document.name == "description") {
                    nextTextHandler = textHandlers.descriptionTextHandler
                } else if(document.name == "statement" && document.getAttributeValue(0) == "url") {
                    nextTextHandler = textHandlers.urlTextHandler
                } else {
                    nextTextHandler = {}
                }
            }
            if(eventType == XmlResourceParser.END_TAG) {
                if (document.name == elementName) {
                    textHandlers.doneWithElement()
                }
            }
            if(eventType == XmlResourceParser.TEXT) {
                nextTextHandler(document.text)
            }
            eventType = document.next()
        }
    }
}