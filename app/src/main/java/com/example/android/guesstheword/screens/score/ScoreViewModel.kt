package com.example.android.guesstheword.screens.score

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ScoreViewModel(finalScore: Int) : ViewModel() {

    private val _score = MutableLiveData<Int>()
    val score: LiveData<Int> = _score

    private val _isPlayAgain = MutableLiveData<Boolean>()
    val isPlayAgain: LiveData<Boolean> = _isPlayAgain

    init {
        Log.i("ScoreViewModel", "init ScoreViewModel")
        _score.value = finalScore
        _isPlayAgain.value = false
    }

    fun onClickPlayAgain() {
        _isPlayAgain.value = true
    }
}