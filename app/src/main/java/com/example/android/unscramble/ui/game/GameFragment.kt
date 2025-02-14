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

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.android.unscramble.R
import com.example.android.unscramble.databinding.GameFragmentBinding
import com.example.android.unscramble.ui.ResourceProvider
import com.google.android.material.dialog.MaterialAlertDialogBuilder

/**
 * Fragment where the game is played, contains the game logic.
 */
class GameFragment : Fragment() {


    private val gameViewModel: GameViewModel by viewModels( ) {
        GameViewModel.Factory(ResourceProvider.nextWordUseCase, ResourceProvider.scrambleWordUseCase)
    }


    // Binding object instance with access to the views in the game_fragment.xml layout
    private lateinit var binding: GameFragmentBinding

    // Create a ViewModel the first time the fragment is created.
    // If the fragment is re-created, it receives the same GameViewModel instance created by the
    // first fragment

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        // Inflate the layout XML file and return a binding object instance
        binding = GameFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Setup a click listener for the Submit and Skip buttons.
        binding.submit.setOnClickListener { onSubmitWord() }
        binding.skip.setOnClickListener { onSkipWord() }
        binding.textViewUnscrambledWord.setOnClickListener { onShowWordInfo() }
        // Update the UI
        updateWordOnScreen()
        gameViewModel.currentScrambledWord.observe(viewLifecycleOwner) {
            binding.textViewUnscrambledWord.text = it
        }
    }

    /*
    * Checks the user's word, and updates the score accordingly.
    * Displays the next scrambled word.
    */
    private fun onSubmitWord() {
        gameViewModel.nextWord(binding.textInputEditText.text.toString())
        setErrorTextField(gameViewModel.guessFailed)
        afterUserAnswerSubmit()
    }

    /*
     * Skips the current word without changing the score.
     * Increases the word count.
     */
    private fun onSkipWord() {
        Toast.makeText(requireContext(), getString(R.string.the_word_was, gameViewModel.currentWord.word), Toast.LENGTH_LONG).show()
        gameViewModel.skipWord()
        afterUserAnswerSubmit()
    }

    private fun onShowWordInfo() {
        try {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(gameViewModel.currentWord.url)
            startActivity(intent)
        } catch (_: ActivityNotFoundException) {
            Toast.makeText(requireContext(), gameViewModel.currentWord.description, Toast.LENGTH_LONG).show()
        }
    }

    private fun afterUserAnswerSubmit(){
        if(gameViewModel.isGameOver()) {
            showEndGameDialog()
        } else {
            updateWordOnScreen()
        }
    }

    /*
     * Re-initializes the data in the ViewModel and updates the views with the new data, to
     * restart the game.
     */
    private fun restartGame() {
        gameViewModel.restartGame()
        updateWordOnScreen()
    }

    /*
     * Exits the game.
     */
    private fun exitGame() {
        activity?.finish()
    }

    /*
    * Sets and resets the text field error status.
    */
    private fun setErrorTextField(error: Boolean) {
        if (error) {
            binding.textField.isErrorEnabled = true
            binding.textField.error = getString(R.string.try_again)
        } else {
            binding.textField.isErrorEnabled = false
            binding.textInputEditText.text = null
        }
    }

    /*
     * Displays the next scrambled word on screen.
     */
    private fun updateWordOnScreen() {
        binding.wordCount.text = getString(R.string.word_count, gameViewModel.currentWordCount, MAX_NO_OF_WORDS)
        binding.score.text = getString(R.string.score, gameViewModel.score)
    }

    private fun showEndGameDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.congratulations)
            .setMessage(getString(R.string.you_scored, gameViewModel.score))
            .setCancelable(false)
            .setPositiveButton(R.string.play_again) { _, _ ->
                restartGame()
            }
            .setNegativeButton(R.string.exit) { _, _ ->
                exitGame()
            }
            .show()
    }
}
