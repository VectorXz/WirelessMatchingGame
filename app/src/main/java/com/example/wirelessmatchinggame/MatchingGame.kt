package com.example.wirelessmatchinggame

import android.R.attr.bitmap
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.provider.MediaStore
import android.util.Log
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import com.example.wirelessmatchinggame.R.drawable.defaultpic
import kotlinx.android.synthetic.main.activity_matching_game.*
import java.util.Collections.addAll


class MatchingGame : AppCompatActivity() {

    enum class TimerState{
        Stopped, Running
    }

    enum class GameState{
        Stopped, Running
    }

    private lateinit var timer: CountDownTimer
    private var timerLengthSeconds = 120L
    private var timerState = TimerState.Stopped
    private var secondsRemaining = 120L
    private var gameStatus = GameState.Stopped;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_matching_game)

        startButton.setOnClickListener{ v ->
            startTimer()
            timerState = TimerState.Running
            updateButtons()
            updateCountdownUI()
            gameStatus = GameState.Running
        }

        val image1 = intent.getStringExtra("imgThumb1")
        val image2 = intent.getStringExtra("imgThumb2")
        val image3 = intent.getStringExtra("imgThumb3")

        Log.d("[IMAGE 1]", ">> "+image1)
        Log.d("[IMAGE 2]", ">> "+image2)
        Log.d("[IMAGE 3]", ">> "+image3)

        val bitmap1 = MediaStore.Images.Media.getBitmap(this.contentResolver, image1.toUri())
        val bitmap2 = MediaStore.Images.Media.getBitmap(this.contentResolver, image2.toUri())
        val bitmap3 = MediaStore.Images.Media.getBitmap(this.contentResolver, image3.toUri())

        val images: MutableList<Bitmap> = mutableListOf(bitmap1, bitmap2, bitmap3, bitmap1, bitmap2, bitmap3)

        val buttons = arrayOf(button1, button2, button3, button4, button5, button6)

        val cardBack = defaultpic
        var clicked = 0
        var turnOver = false
        var lastClicked = -1
        var count = 0
        var flipNo = 0

        images.shuffle()

        val Texts: MutableList<String> = mutableListOf()
        for(i in 0..5) {
            when(images[i]) {
                bitmap1-> Texts.add(i, intent.getStringExtra("txtImg1"))
                bitmap2-> Texts.add(i, intent.getStringExtra("txtImg2"))
                bitmap3-> Texts.add(i, intent.getStringExtra("txtImg3"))
            }
        }

        Log.d("Texts", ">> "+Texts)
        Log.d("Img", ">> "+images)

        for(i in 0..5){
            buttons[i].setBackgroundResource(cardBack)
            buttons[i].text = "cardBack"
            buttons[i].textSize = 0F
            buttons[i].setOnClickListener {
                if (buttons[i].text == "cardBack" && !turnOver && timerState == TimerState.Running) {
                    buttons[i].setBackground(BitmapDrawable(getResources(), images[i]))
                    buttons[i].setText(images[i].toString())
                    if (clicked == 0) {
                        lastClicked = i
                    }
                    clicked++
                    flipNo++
                    Log.d("Flip count", "COUNT : "+flipNo)
                    val countNoTxt = findViewById<TextView>(R.id.countNoTxt)
                    countNoTxt.text = flipNo.toString()
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
                        buttons[i].setBackground(BitmapDrawable(getResources(), images[i]))
                        buttons[i].setText(images[i].toString())
                        buttons[lastClicked].isClickable = false
                        buttons[lastClicked].setBackground(BitmapDrawable(getResources(), images[lastClicked]))
                        buttons[lastClicked].setText(images[lastClicked].toString())
                        turnOver = false
                        clicked = 0
                        if(count == 3) {
                            Log.d("GAME STATUS", "ENDED")
                            timer.cancel()
                            gameStatus = GameState.Stopped
                            //TODO Alert dialog for user to input vocab (get from intent stated in Texts variable)
                            val builder = AlertDialog.Builder(this@MatchingGame)
                            builder.setTitle("You won!")
                            builder.setMessage("You won this game!")
                            builder.setPositiveButton("OK") { dialogInterface, which ->
                                //TODO Redirect to input vocab page.
                            }
                            val alertDialog: AlertDialog = builder.create()
                            alertDialog.show()
                        }
                    } else {
                        //auto close card
                        Handler().postDelayed(Runnable {
                            buttons[i].setBackgroundResource(cardBack)
                            buttons[i].text = "cardBack"
                            buttons[lastClicked].setBackgroundResource(cardBack)
                            buttons[lastClicked].text = "cardBack"
                            clicked = 0
                            turnOver = false
                        }, 800)
                    }
                } else if (clicked == 0) {
                    turnOver = false
                }
            }
        }
    }

    private fun onTimerFinished(){
        val TimerTxt = findViewById<TextView>(R.id.textViewCountdown)
        TimerTxt.setText("0:00")
        val progressBar = findViewById<ProgressBar>(R.id.progress_countdown)
        progressBar.progress = 0
        timerState = TimerState.Stopped
        secondsRemaining = timerLengthSeconds
        Log.d("Game Status", ">> "+gameStatus)
        if(gameStatus == GameState.Running) {
            val builder = AlertDialog.Builder(this@MatchingGame)
            builder.setTitle("You Lose!")
            builder.setMessage("You lose this game!")
            builder.setPositiveButton("OK") { dialogInterface, which ->
                //TODO Redirect to result page.
            }
            val alertDialog: AlertDialog = builder.create()
            alertDialog.show()
        }
    }

    private fun startTimer(){
        timerState = TimerState.Running
        timer = object : CountDownTimer(secondsRemaining*1000, 100){
            override fun onFinish() = onTimerFinished()

            override fun onTick(millisUntilFinished: Long) {
                //Log.d("Milli Until Finish", ":" + millisUntilFinished)

                secondsRemaining = millisUntilFinished/1000
                updateCountdownUI()
            }

        }.start()
    }

    private fun updateCountdownUI(){
        val minutesUntilFinished = secondsRemaining/60
        val secondsInMinuteUntilFinished = secondsRemaining - minutesUntilFinished * 60
        val secondsStr = secondsInMinuteUntilFinished.toString()
        textViewCountdown.text = "$minutesUntilFinished:${if(secondsStr.length == 2) secondsStr else "0"+secondsStr}"
        val progressBar = findViewById<ProgressBar>(R.id.progress_countdown)
        progressBar.progress = (120 - (timerLengthSeconds - secondsRemaining)).toInt()
        //Log.d("TIME", (120 - (timerLengthSeconds - secondsRemaining)).toString())
    }

    private fun updateButtons(){
        when (timerState){
            TimerState.Running->{
                startButton.isEnabled = false
                startButton.setBackgroundResource(R.color.lightBlack)
            }
            TimerState.Stopped->{
                startButton.isEnabled = true
            }
        }
    }
}
