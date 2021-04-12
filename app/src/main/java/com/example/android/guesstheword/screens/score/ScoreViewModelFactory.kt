package com.example.android.guesstheword.screens.score

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

//Note : the constructor of your view model factory should take any parameters you want to pass into your ScoreViewModel. In this case, it takes in the final score.
class ScoreViewModelFactory(private val finalScore: Int) :
        ViewModelProvider.Factory {

    // to create and return ViewModel = should construct a new ScoreViewModel and return it
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ScoreViewModel::class.java)) {
            return ScoreViewModel(finalScore) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}