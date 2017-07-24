package com.meetkokomo.www.minesweeper

import android.util.Log
import java.util.Random
import java.util.Stack



/**
 * Created by morningstar on 7/22/17.
 */

class MineSweeper {

    val MAX_MINES : Int = 2
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

    fun runTest(){
        board.printBoard()
        board.click(0,0)
        board.printBoard()
        board.click(0,1)
        board.printBoard()
        board.click(1,0)
        board.printBoard()
        board.click(1,1)
        board.printBoard()
    }

}

class Board (numRow : Int, numCol : Int) {

    val ROW : Int
    val COL : Int
    val MINE_CELL = 9
    val EMPTY_CELL = 10
    val MINE_CELL_PRESSED = -1
    val EMPTY_CELL_PRESSED = 0

    var data : IntArray

    init {
        ROW = numRow
        COL = numCol
        data = IntArray ( numRow*numCol , { _ -> 10})
    }

    fun placeMine ( mine : Int ) : Boolean {

        var minePlaced = false

        if (data[mine] != MINE_CELL){
            data[mine] = MINE_CELL
            fillNeighbors(mine)
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

    fun isValid(col : Int, row : Int) : Boolean {
        return (col < COL && col >= 0 && row < ROW && row >= 0)
    }

    fun updateNeighbor (col : Int, row : Int) {

        if(isValid(col, row)) {
            val element: Int = getElementAt(col, row)

            if (element != MINE_CELL) {
                setElement(element + 1, col, row)
            }
        }

    }

    fun click (col : Int, row : Int){
        val element = getElementAt(col, row)

        if(isMine(element)){
            Log.d("Board", "GAME OVER")
        }
        else if (element >= MINE_CELL){
            expansion(col, row)
        }
    }

    fun expansion (col : Int, row: Int){

        var stack = Stack<Int> ()

        stack.push((row * COL) +  col)

        while (!stack.empty()){
            var temp = stack.pop()

            val col = temp % COL
            var row = temp / COL

            if(data[temp] == 10){

                data[temp] = 0

                if (isValid(col-1, row-1)) {
                    stack.push(((row - 1) * COL) + (col - 1))
                }

                if (isValid(col, row-1)) {
                    stack.push(((row - 1) * COL) + col)
                }
                if (isValid(col+1,row-1)) {
                    stack.push(((row - 1) * COL) + (col + 1))
                }

                if (isValid (col-1, row)) {
                    stack.push((row * COL) + (col - 1))
                }

                if (isValid(col+1, row)) {
                    stack.push((row * COL) + (col + 1))
                }

                if (isValid(col - 1, row + 1)){
                    stack.push(((row + 1) * COL) + (col - 1))
                }

                if (isValid(col, row + 1)) {
                    stack.push(((row + 1) * COL) + col)
                }

                if (isValid(col + 1, row + 1)) {
                    stack.push(((row + 1) * COL) + (col + 1))
                }

            }
            else if (data[temp] > 10){
                data[temp] -= 10
            }
        }

    }

    fun isMine(element : Int) : Boolean {
        return (element == MINE_CELL)
    }

    fun getElementAt (col : Int, row: Int) : Int {
        return data [(row * COL) +  col]
    }

    fun setElement (value: Int, col: Int, row: Int){
        if( isValid(col, row)){
            data [(row * COL) +  col] = value;
        }
    }

    fun printBoard() {
        var nRow: String = ""
        for ((index, value) in data.withIndex()) {
            if (index >= COL) {
                if (index % COL == 0)
                    nRow += "\n"
            }
            if(value >= MINE_CELL) {
                nRow += "  0 " //TODO change here to 0
            }
            else {
                if (value == MINE_CELL_PRESSED)
                    nRow += "  X "
                else if (value == EMPTY_CELL_PRESSED)
                    nRow += "  - "
                else
                    nRow += "  " + value + " "

            }
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