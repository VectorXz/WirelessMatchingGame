package com.example.wirelessmatchinggame

import android.os.Bundle
import android.widget.TextView
import android.os.CountDownTimer
import androidx.appcompat.app.AppCompatActivity
import com.example.wirelessmatchinggame.R.drawable.*
import kotlinx.android.synthetic.main.activity_matching_game.*


class MatchingGame : AppCompatActivity() {

    enum class TimerState{
        Stopped, Running
    }

    private lateinit var timer: CountDownTimer
    private var timerLengthSeconds = 0L
    private var timerState = TimerState.Stopped
    private var secondsRemaining = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_matching_game)

        startButton.setOnClickListener{ v ->
            startTimer()
            timerState = TimerState.Running
            updateButtons()
            updateCountdownUI()
        }

        val images: MutableList<Int> =
            mutableListOf(test1, test2, test3, test1, test2, test3)

        val buttons = arrayOf(button1, button2, button3, button4, button5, button6)

        val cardBack = defaultpic
        var clicked = 0
        var turnOver = false
        var lastClicked = -1
        var count = 0

        images.shuffle()
        for(i in 0..5){
            buttons[i].setBackgroundResource(cardBack)
            buttons[i].text = "cardBack"
            buttons[i].textSize = 0.0F
            buttons[i].setOnClickListener {
                if (buttons[i].text == "cardBack" && !turnOver) {
                    buttons[i].setBackgroundResource(images[i])
                    buttons[i].setText(images[i])
                    if (clicked == 0) {
                        lastClicked = i
                    }
                    clicked++
                } else if (buttons[i].text !in "cardBack") {
                    buttons[i].setBackgroundResource(cardBack)
                    buttons[i].text = "cardBack"
                    clicked--
                }

                if (clicked == 2) {
                    turnOver = true
                    if (buttons[i].text == buttons[lastClicked].text) {
                        count++
                        buttons[i].isClickable = false
                        buttons[lastClicked].isClickable = false
                        turnOver = false
                        clicked = 0
                    }
                } else if (clicked == 0) {
                    turnOver = false
                }
            }
        }
    }

    private fun onTimerFinished(){
        timerState = TimerState.Stopped
        secondsRemaining = timerLengthSeconds
    }

    private fun startTimer(){
        timerState = TimerState.Running
        timer = object : CountDownTimer(secondsRemaining*1000, 1000){
            override fun onFinish() = onTimerFinished()

            override fun onTick(millisUntilFinished: Long) {
                secondsRemaining = millisUntilFinished/1000
                updateCountdownUI()
            }
        }.start()
    }

    private fun updateCountdownUI(){
        val minutesUntilFinished = secondsRemaining/60
        val secondsInMinuteUntilFinished = secondsRemaining - minutesUntilFinished * 60
        val secondsStr = secondsInMinuteUntilFinished.toString()
        textViewCountdown.text = "$minutesUntilFinished:${
        if(secondsStr.length == 2) secondsStr
        else "0"+secondsStr}"
        progress_countdown.progress = (timerLengthSeconds - secondsRemaining).toInt()
    }

    private fun updateButtons(){
        when (timerState){
            TimerState.Running->{
                startButton.isEnabled = false
            }
            TimerState.Stopped->{
                startButton.isEnabled = true
            }
        }
    }
}
