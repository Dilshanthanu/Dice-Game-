package com.example.mobilecw1

import androidx.compose.runtime.*
import kotlin.random.Random

class GameViewModal {
    private var _diceNoHuman = mutableStateOf(List(5) { 1 })
    private var _diceNoComputer = mutableStateOf(List(5) { 1 })
    private var _turns = mutableStateOf(3)
    private var _humanScore = mutableStateOf(0)
    private var _computerScore = mutableStateOf(0)
    private var _humanSelectedDice = mutableStateOf(List(5) { 0 })
    private var _showWonPopup = mutableStateOf(false)
    private var _showLosePopup = mutableStateOf(false)
    private var _showTiePopup = mutableStateOf(false)
    private var _isCalculationCompleted = mutableStateOf(false)
    private var _targetScore = mutableStateOf(101)


    val diceNoHuman: List<Int> get() = _diceNoHuman.value
    val diceNoComputer: List<Int> get() = _diceNoComputer.value
    val turns: Int get() = _turns.value
    val humanScore: Int get() = _humanScore.value
    val computerScore: Int get() = _computerScore.value
    val humanSelectedDice: List<Int> get() = _humanSelectedDice.value
    val showWonPopup: Boolean get() = _showWonPopup.value
    val showLosePopup:Boolean get() = _showLosePopup.value
    val showTiePopup: Boolean get() = _showTiePopup.value
    val isCalculationCompleted: Boolean get() = _isCalculationCompleted.value
    val targetScore: Int get() = _targetScore.value

        fun CalculateScore() {
            _humanScore.value += _diceNoHuman.value.sum()
            _computerScore.value += _diceNoComputer.value.sum()
            _isCalculationCompleted.value = true
            Result()
        }

    fun TrowOnclickaction() {
        _diceNoHuman.value = HumNDiceThrow()
        _diceNoComputer.value = ComputerDiceThrow()
        _turns.value--
        _isCalculationCompleted.value = false
        if ( CheckResult(_humanScore.value , _computerScore.value)){
            Result()
        }


    }

    fun ResetGame() {
        _diceNoHuman.value = List(5) { 1 }
        _diceNoComputer.value = List(5) { 1 }
        _turns.value = 3
        _humanScore.value = 0
        _computerScore.value = 0
    }

    fun ComputerDiceThrow(): List<Int> {
        var n : Int = 0
        for (i in _diceNoComputer.value){
            if (i == 4 || i == 5 || i == 6){
                n++
                continue
            }
            _diceNoComputer.value = _diceNoComputer.value.toMutableList().also { it[n] = SingelNoGen() }
            n++

        }
        return _diceNoComputer.value
    }

    fun SingelNoGen(): Int{
        return Random.nextInt(1,7)
    }

    fun HumNDiceThrow():List<Int> {
        if (!_humanSelectedDice.value.all { it == 0 }){
            var n: Int = 0
            for (i in _humanSelectedDice.value){


                if (i != 0 ){
                    _diceNoHuman.value = _diceNoHuman.value.toMutableList().also { it[n] = i }
                    n++
                }else if (i == 0){
                    _diceNoHuman.value = _diceNoHuman.value.toMutableList().also { it[n] = SingelNoGen() }
                    n++
                }
            }
            _humanSelectedDice.value = List(_humanSelectedDice.value.size) { 0 }
            return _diceNoHuman.value
        }
        return List(5) { Random.nextInt(1, 7) }
    }

    fun HumanDiceOnclick(diceNo:Int , index: Int) {
        _humanSelectedDice.value = _humanSelectedDice.value.toMutableList().also {
            it[index] = diceNo
        }
    }

    fun isSelectCheck(diceNo: Int , index: Int):Boolean{
        if (_humanSelectedDice.value[index] == diceNo){
            return true
        }else{
            return false
        }
    }

    fun CheckResult (humanscore : Int , computescore : Int):Boolean{
        if (humanscore < _targetScore.value && computescore < _targetScore.value){
            _turns.value += 1;
            return false
        }else{
            _turns.value = 0;
            return true
        }
    }

    fun Result(){
        if (_humanScore.value > _computerScore.value ){
            _showWonPopup.value = true

        }else if (_humanScore.value < _computerScore.value ){
            _showLosePopup.value = true
        }else if (_humanScore.value == _computerScore.value ){
            _showTiePopup.value = true
            _turns.value +=1
        }
    }

    fun ShowPopupDismiss(){
        _showWonPopup.value = false
        _showLosePopup.value = false
    }

    fun updateTargetScore(newScore: Int) {
        _targetScore.value = newScore
    }
}
