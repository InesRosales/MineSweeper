package com.meetkokomo.www.minesweeper

import android.content.Context
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.GridLayout
import org.jetbrains.anko.*
import android.graphics.Point
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.activity_game_ui.*



class GameUIActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_ui)

        val col: Int = this.intent.getIntExtra("col", 0)
        val row: Int = this.intent.getIntExtra("row", 0)
        val mines: Int = this.intent.getIntExtra("mines", 0)

        var game = MineSweeper(col, row, mines)

        ui_grid_layout.columnCount = col
        ui_grid_layout.rowCount = row
        val screenWidth = getScreenWidth()
        val cellWidth = screenWidth / col
        val margin = cellWidth / col

        var r : Int = 0
        while (r < row){
            var c : Int = 0
            while(c < col){
                ui_grid_layout.addView(createButton(c, r, game, this))
                c++
            }
            r++
        }

        var p : ViewGroup.LayoutParams
        for (i in 0..ui_grid_layout.getChildCount() - 1) {
            p = ui_grid_layout.getChildAt(i).layoutParams
            p.width = cellWidth
            p.height = cellWidth
        }

    }

    fun getScreenWidth(): Int {
        val display = windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        return size.x
    }

    fun createButton(c: Int, r: Int, game: MineSweeper, context: Context?) : GridButton{
        var button : GridButton = GridButton(c, r, context)

        button.text = " "

        button.setOnClickListener {

            val value = game.board.click(button.c, button.r)
            game.board.printBoard()
            if(game.hasGameEnded())
                alertWinGame()
            else if(game.hasMineBeenPressed()) {
                alertEndGame()
            }

            if(value == 0){

                var elements = game.board.getExpansion()
                var ePosition : Int
                var eText : Int

                while(!elements.empty()){

                    ePosition = elements.pop()
                    eText = game.board.getElementAt(ePosition)

                    val v = ui_grid_layout.getChildAt(ePosition) as View
                    if (v is Button) {
                        deshabilitateButton(v)
                        if(eText != 0)
                            v.text = eText.toString()
                    }


                }

            }
            else {
                button.text = value.toString()
            }

            deshabilitateButton(button)
        }

        return button
    }

    fun deshabilitateButton(button : View){
        button.isClickable = false
        button.setBackgroundColor(Color.LTGRAY)
    }

    fun alertWinGame(){
        alert ("You Won!"){
            positiveButton("Ok"){
                finish()
            }
        }
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
