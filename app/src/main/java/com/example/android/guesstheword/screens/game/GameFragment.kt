/*
 * Copyright 2018, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.guesstheword.screens.game

import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.getSystemService
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment.findNavController
import com.example.android.guesstheword.R
import com.example.android.guesstheword.databinding.GameFragmentBinding
import com.google.firebase.analytics.FirebaseAnalytics

/**
 * Fragment where the game is played
 */
class GameFragment : Fragment() {

    // binding contains references to views
    private lateinit var binding: GameFragmentBinding

    private lateinit var viewModel: GameViewModel

    private lateinit var firebaseAnalytics: FirebaseAnalytics

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        // Inflate view and obtain an instance of the binding class
        binding = DataBindingUtil.inflate(
                inflater,
                R.layout.game_fragment,
                container,
                false
        )
        // To make your data binding lifecycle aware and to have it play nicely with LiveData,
        // you need to call binding.setLifecycleOwner
        binding.lifecycleOwner = this

        firebaseAnalytics = FirebaseAnalytics.getInstance(requireContext())

        // 10-4 : 4.Create and initialize a GameViewModel, using ViewModelProvider(). Add a log statement
        Log.i("GameViewModel", "ViewModelProvider created!!")
        viewModel = ViewModelProvider(this).get(GameViewModel::class.java)
        binding.viewModel = viewModel

        viewModel.eventGameFinish.observe(viewLifecycleOwner, Observer { hasFinished ->
            if (hasFinished) {
                gameFinished()
                viewModel.onGameFinishComplete()
            }
        })

        viewModel.eventBuzz.observe(viewLifecycleOwner, Observer { newBuzzType ->
            if (newBuzzType != BuzzType.NO_BUZZ) {
                buzz(newBuzzType.pattern)
                viewModel.onBuzzComplete()
            }
        })

        return binding.root
    }

    private fun buzz(pattern: LongArray) {
        val buzzer = activity?.getSystemService<Vibrator>()
        buzzer?.let {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                buzzer.vibrate(VibrationEffect.createWaveform(pattern, -1))
            } else {
                //deprecated in API 26
                buzzer.vibrate(pattern, -1)
            }
        }
    }


    /**
     * Called when the game is finished
     */
    private fun gameFinished() {
        //tracking
        val bundle = Bundle().apply {
            putString("event_game_fragment", "game finished")
        }
        firebaseAnalytics.logEvent("game_fragment", bundle)

        val action = GameFragmentDirections.actionGameToScore(viewModel.score.value ?: 0)
        findNavController(this).navigate(action)
    }

}
