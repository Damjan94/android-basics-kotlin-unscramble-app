/*
 * Copyright (C) 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.unscramble.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.android.unscramble.R
import com.example.android.unscramble.data.source.WordSourceImpl
import com.example.android.unscramble.domain.use_case.NextWordUseCase
import com.example.android.unscramble.domain.use_case.ScrambleWordUseCase
import com.example.android.unscramble.domain.use_case.xml_parser.GoToNextTagUseCase
import com.example.android.unscramble.domain.use_case.xml_parser.ParseNextWordXmlUseCase
import com.example.android.unscramble.domain.use_case.xml_parser.ParseWordXmlUseCase
import com.example.android.unscramble.ui.game.WordListRepositoryImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.random.Random


object ResourceProvider {

    private val nextTagUseCase = GoToNextTagUseCase()
    private val parseNextWordXmlUseCase = ParseNextWordXmlUseCase(nextTagUseCase)
    private val parseWordXmlUseCase = ParseWordXmlUseCase(parseNextWordXmlUseCase, nextTagUseCase)
    val wordSource = WordSourceImpl(parseWordXmlUseCase, Random(System.currentTimeMillis()))
    private val wordListRepository = WordListRepositoryImpl(wordSource)
    val nextWordUseCase = NextWordUseCase(wordListRepository)
    val scrambleWordUseCase = ScrambleWordUseCase()

}
/**
 * Creates an Activity that hosts the Game fragment in the app
 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        val stream = resources.getXml(R.xml.jezicka_laboratorija_recnik)
        lifecycleScope.launch(Dispatchers.IO) {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                ResourceProvider.wordSource.parseDatabase(stream)
            }
        }
    }
}