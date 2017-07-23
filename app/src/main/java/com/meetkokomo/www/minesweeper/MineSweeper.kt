package com.meetkokomo.www.minesweeper

import android.util.Log
import java.util.Random
import java.util.concurrent.ConcurrentLinkedDeque

/**
 * Created by morningstar on 7/22/17.
 */

class MineSweeper {

    val MAX_MINES : Int = 10
    val ROW : Int = 8
    val COL : Int = 8

    var placedMines : Int  = 0
    var mineGenerator = MineGenerator (ROW * COL)
    var board = Board(ROW, COL)

    init {
        while (placedMines < MAX_MINES) {
            var mine = mineGenerator.getMine()
            if(board.placeMine(mine)){
                placedMines++
            }
        }
    }

}

class Board (numRow : Int, numCol : Int) {

    val ROW : Int
    val COL : Int
    val MINE_CELL = -1

    var data : IntArray

    init {
        ROW = numRow
        COL = numCol
        data = IntArray ( numRow*numCol , { _ -> 0})
    }

    fun placeMine ( mine : Int ) : Boolean {

        var minePlaced = false

        if (data[mine] != MINE_CELL){
            data[mine] = MINE_CELL
            fillNeighbors(mine)
            printBoard()
            minePlaced = true
        }

        return minePlaced
    }

    fun fillNeighbors (mine : Int) {
        var col : Int = mine % COL
        val row : Int = mine / COL

        updateNeighbor(col-1, row-1)
        updateNeighbor(col, row-1)
        updateNeighbor(col+1,row-1)
        updateNeighbor(col-1, row)
        updateNeighbor(col+1, row)
        updateNeighbor(col-1, row+1)
        updateNeighbor(col, row+1)
        updateNeighbor(col+1, row+1)

    }

    fun valid (col : Int, row : Int) : Boolean {
        var isValid : Boolean = false

        if(col < COL && col >= 0 && row < ROW && row >= 0)
            isValid = true

        return isValid
    }

    fun updateNeighbor (col : Int, row : Int) {

        val valid = valid(col, row)
        val position : Int = (row * COL) + col

        if(valid == true && data[position] != MINE_CELL) {
            data[position] ++
        }

    }

    fun printBoard() {
        var nRow: String = ""

        for ((index, value) in data.withIndex()) {

            if (index >= COL) {
               if (index % COL == 0)
                    nRow += "\n"
            }

            if (value == -1)
                nRow += "" + value + " "
            else
                nRow += " " + value + " "

        }

        Log.d("Board", nRow)

    }

}

class MineGenerator (length : Int) {

    var limit : Int = 0

    init {
        limit = length
    }

    fun getMine () : Int {
        var random = Random()
        return random.nextInt(limit)
    }

}