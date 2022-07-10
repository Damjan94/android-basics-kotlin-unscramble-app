package com.example.android.unscramble.domain.use_case.xml_parser

data class TextHandlers(
    val idTextHandler: (Int) -> Unit,
    val wordTextHandler: (String) -> Unit,
    val descriptionTextHandler: (String) -> Unit,
    val urlTextHandler: (String) -> Unit,
    val doneWithElement: suspend () -> Unit
)