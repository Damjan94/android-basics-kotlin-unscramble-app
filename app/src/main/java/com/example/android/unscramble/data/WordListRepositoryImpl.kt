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

package com.example.android.unscramble.ui.game

import com.example.android.unscramble.data.model.Word
import com.example.android.unscramble.data.source.WordSource
import com.example.android.unscramble.domain.repository.WordListRepository


const val MAX_NO_OF_WORDS = 10
const val SCORE_INCREASE = 20

class WordListRepositoryImpl(private val source: WordSource): WordListRepository {
    override suspend fun getRandomWord(): Word {
        return source.getRandomWord()
    }

    override fun getRepoSize(): Int {
        return source.wordCount
    }
}