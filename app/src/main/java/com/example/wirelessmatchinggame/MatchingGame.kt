package com.example.wirelessmatchinggame

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.wirelessmatchinggame.R.drawable.*
import kotlinx.android.synthetic.main.activity_matching_game.*

class MatchingGame : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_matching_game)

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
}
