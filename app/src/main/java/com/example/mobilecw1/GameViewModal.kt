package com.example.mobilecw1

import android.os.Bundle
import android.util.Log
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.delay
import kotlin.random.Random
import kotlinx.coroutines.*


class GameViewModal:ViewModel() {
    private var _diceNoHuman = mutableStateOf(List(5) { 1 })
    private var _diceNoComputer = mutableStateOf(List(5) { 1 })
    private var _turns = mutableStateOf(0)
    private var _humanScore = mutableStateOf(0)
    private var _computerScore = mutableStateOf(0)
    private var _humanSelectedDice = mutableStateOf(List(5) { 0 })
    private var _showWonPopup = mutableStateOf(false)
    private var _showLosePopup = mutableStateOf(false)
    private var _showTiePopup = mutableStateOf(false)
    private var _isCalculationCompleted = mutableStateOf(true)
    private var _targetScore = mutableStateOf(101)
    private var _humanWins = mutableStateOf(0)
    private var _computerWins = mutableStateOf(0)
    private var _computerTurns =  mutableStateOf(3)
    private var _diceAnimationKey = mutableStateOf(0)


    val diceNoHuman: List<Int> get() = _diceNoHuman.value
    val diceNoComputer: List<Int> get() = _diceNoComputer.value
    val turns: Int get() = _turns.value
    val humanScore: Int get() = _humanScore.value
    val computerScore: Int get() = _computerScore.value
    val humanSelectedDice: List<Int> get() = _humanSelectedDice.value
    val showWonPopup: Boolean get() = _showWonPopup.value
    val showLosePopup: Boolean get() = _showLosePopup.value
    val showTiePopup: Boolean get() = _showTiePopup.value
    val isCalculationCompleted: Boolean get() = _isCalculationCompleted.value
    val targetScore: Int get() = _targetScore.value
    val humanWins: Int get() = _humanWins.value
    val computerWins: Int get() = _computerWins.value
    val diceAnimationKey: Int get() = _diceAnimationKey.value

    fun CalculateScoreHumen() {
        _humanScore.value += _diceNoHuman.value.sum()
        _isCalculationCompleted.value = true
        if (CheckResult(_humanScore.value, _computerScore.value)) {
            Result()
        }
        _turns.value = 0
    }
    fun CalculateScoreComputer() {
        _computerScore.value += _diceNoComputer.value.sum()
        if (CheckResult(_humanScore.value, _computerScore.value)) {
            Result()
        }
        _computerTurns.value = 0
    }


    fun TrowOnclickaction() {

        _diceNoHuman.value = DiceListGen()
        _diceNoComputer.value = DiceListGen()
        CoroutineScope(Dispatchers.Main).launch {
            repeat(_computerTurns.value) {
                delay(1000)
                _diceNoComputer.value = ComputerDiceReroll()
            }
            CalculateScoreComputer()
        }
        _isCalculationCompleted.value = false
        if (CheckResult(_humanScore.value, _computerScore.value)) {
            Result()
        }
        _turns.value = 3

    }

    fun ResetGame() {
        _diceNoHuman.value = List(5) { 1 }
        _diceNoComputer.value = List(5) { 1 }
        _turns.value = 3
        _humanScore.value = 0
        _computerScore.value = 0
    }

    fun ComputerDiceReroll(): List<Int> {
        val originalDice = _diceNoComputer.value.toList()
        var n = 0
        var isUpdated = false

        val newDice = _diceNoComputer.value.toMutableList()

        for (i in originalDice) {
            if (i == 5 || i == 6) {
                n++
                continue
            } else if (i == 4) {
                if (_computerScore.value - _humanScore.value >= 10) {
                    n++
                    continue
                } else {
                    newDice[n] = SingelNoGen()
                    isUpdated = true
                    n++
                    continue
                }
            }
            newDice[n] = SingelNoGen()
            isUpdated = true
            n++
        }

        _diceNoComputer.value = newDice

        if (originalDice == _diceNoComputer.value) {

            _computerTurns.value = 0

        }

        return _diceNoComputer.value
    }


    fun SingelNoGen(): Int {
        return Random.nextInt(1, 7)
    }

    fun DiceListGen(): List<Int> {
        return List(5) { Random.nextInt(1, 7) }
    }

    fun HumanDiceOnclick(diceNo: Int, index: Int) {
        _humanSelectedDice.value = _humanSelectedDice.value.toMutableList().also {
            it[index] = diceNo
        }
    }

    fun isSelectCheck(diceNo: Int, index: Int): Boolean {
        if (_humanSelectedDice.value[index] == diceNo) {
            return true
        } else {
            return false
        }
    }

    fun CheckResult(humanscore: Int, computescore: Int): Boolean {
        if (humanscore < _targetScore.value && computescore < _targetScore.value) {
            _turns.value += 1;
            return false
        } else {
            _turns.value = 0;
            return true
        }
    }

    fun Result() {
        if (_humanScore.value > _computerScore.value) {
            _showWonPopup.value = true
            _humanWins.value += 1

        } else if (_humanScore.value < _computerScore.value) {
            _showLosePopup.value = true
            _computerWins.value += 1
        } else if (_humanScore.value == _computerScore.value) {
            _showTiePopup.value = true
            _turns.value += 1
        }
    }

    fun ShowPopupDismiss() {
        _showWonPopup.value = false
        _showLosePopup.value = false
    }

    fun updateTargetScore(newScore: Int) {
        _targetScore.value = newScore
    }

    fun RerollOnclick(){
        if (!_humanSelectedDice.value.all { it == 0 }) {
            var n: Int = 0
            for (i in _humanSelectedDice.value) {
                if (i != 0) {
                    _diceNoHuman.value = _diceNoHuman.value.toMutableList().also { it[n] = i }
                    n++
                } else if (i == 0) {
                    _diceNoHuman.value =
                        _diceNoHuman.value.toMutableList().also { it[n] = SingelNoGen() }
                    n++
                }
            }
            _humanSelectedDice.value = List(_humanSelectedDice.value.size) { 0 }
            _turns.value -= 1
            if(_turns.value == 0){
                CalculateScoreHumen()
            }

        }else {
            _diceNoHuman.value = DiceListGen()
            _turns.value -= 1
        }
    }

    fun dicekey(){
        _diceAnimationKey.value++
    }
    fun isGameDataReset(): Boolean {
        return _diceNoHuman.value.all { it == 1 } &&
                _diceNoComputer.value.all { it == 1 } &&
                _turns.value == 0 &&
                _humanScore.value == 0 &&
                _computerScore.value == 0 &&
                _humanSelectedDice.value.all { it == 0 } &&
                !_showWonPopup.value &&
                !_showLosePopup.value &&
                !_showTiePopup.value &&
                _isCalculationCompleted.value &&
                _humanWins.value == 0 &&
                _computerWins.value == 0 &&
                _computerTurns.value == 3 &&
                _diceAnimationKey.value == 0
    }
    fun resetGameData() {
        _diceNoHuman.value = List(5) { 1 }
        _diceNoComputer.value = List(5) { 1 }
        _turns.value = 0
        _humanScore.value = 0
        _computerScore.value = 0
        _humanSelectedDice.value = List(5) { 0 }
        _showWonPopup.value = false
        _showLosePopup.value = false
        _showTiePopup.value = false
        _isCalculationCompleted.value = true
        _humanWins.value = 0
        _computerWins.value = 0
        _computerTurns.value = 3
        _diceAnimationKey.value = 0
    }

}

