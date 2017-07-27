package com.meetkokomo.www.minesweeper

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.ViewGroup
import android.widget.Button
import android.widget.GridLayout
import android.widget.LinearLayout
import org.jetbrains.anko.*

class GameUIActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_ui)

        val col: Int = this.intent.getIntExtra("col", 0)
        val row: Int = this.intent.getIntExtra("row", 0)
        val mines: Int = this.intent.getIntExtra("mines", 0)

        var game = MineSweeper(col, row, mines)

        var gLayout : GridLayout = GridLayout(this)
        gLayout.columnCount = col
        gLayout.rowCount = row

        val param = GridLayout.LayoutParams()

        param.height = GridLayout.LayoutParams.WRAP_CONTENT
        param.width = GridLayout.LayoutParams.MATCH_PARENT

        gLayout.layoutParams = param

        var r : Int = 0

        while (r < row){
            var c : Int = 0
            while(c < col){
                gLayout.addView(createButton(c, r, game, this))
                c++
            }
            r++
        }

        setContentView(gLayout)
    }

    fun createButton(c: Int, r: Int, game: MineSweeper, context: Context?) : GridButton{
        var button : GridButton = GridButton(c, r, context)

        button.text = " "

        val param = GridLayout.LayoutParams()
        param.height = 55 //GridLayout.LayoutParams.WRAP_CONTENT
        param.width = 55 //GridLayout.LayoutParams.WRAP_CONTENT TODO make dimentions calculation
        param.columnSpec = GridLayout.spec(c)
        param.rowSpec = GridLayout.spec(r)
        button.layoutParams = param

        button.setOnClickListener {
            button.text = game.board.click(button.c, button.r).toString()
            game.board.printBoard()
            if(game.hasGameEnded())
                Log.d("UI", "You Win")
            else if(game.hasMineBeenPressed()) {
                alertEndGame()
            }
            button.isClickable = false
            button.setBackgroundColor(Color.GRAY)
        }

        return button
    }

    fun alertEndGame(){
        alert ("You lost"){
            positiveButton("Ok"){
                finish()
            }
        }.show()
    }
}

class GridButton(col: Int, row : Int, context: Context?) : Button(context) {

    var c : Int = 0
    var r : Int = 0
    var value : Int = 10

    init {
        c = col
        r = row
    }

}

        /*
        var r : Int = 0

        verticalLayout {
            while (r < row) {
                var c: Int = 0
                linearLayout {
                    while (c < col) {
                        val b = button{
                            text = ""
                        }.lparams(width = wrapContent, height = wrapContent)
                        b.setOnClickListener {
                            //game.board.click(c, r)
                            Log.d("UI", "c "+c+" r "+r);

                        }
                        c++
                    }
                }
                r++
            }
        }*/
