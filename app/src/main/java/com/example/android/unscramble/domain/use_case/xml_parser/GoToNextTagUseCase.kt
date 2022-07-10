package com.example.android.unscramble.domain.use_case.xml_parser

import android.content.res.XmlResourceParser

class GoToNextTagUseCase {
    operator fun invoke(document: XmlResourceParser, tag: String, breakEarly: (document: XmlResourceParser) -> Boolean = {false}) {
        while (document.name != tag) {
            if(breakEarly(document)) {
                break
            }
            document.next()
        }
    }
}