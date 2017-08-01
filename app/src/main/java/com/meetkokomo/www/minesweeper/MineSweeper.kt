package com.meetkokomo.www.minesweeper

import android.util.Log
import java.util.*


/**
 * Created by morningstar on 7/22/17.
 */

interface BoardListener{
    fun minePressed()
    fun gameEnded()
}


class MineSweeper (col: Int, row: Int, mines: Int) : BoardListener { //TODO remove activity parameter

    var col : Int = 0
    var row : Int = 0
    var mines : Int = 0

    var win : Boolean = false
    var lose : Boolean = false

    var board : Board

    init {
        this.col = col
        this.row = row
        this.mines = mines
        board  = Board(row, col, mines, this)
        board.fillBoard()
        board.printBoard()
    }

    override fun minePressed(){
        lose = true
    }

    override fun gameEnded() {
        win = true
    }

    fun hasGameEnded() : Boolean{
        return win
    }
    fun hasMineBeenPressed() : Boolean{
        return lose
    }

}

class Board (numRow : Int, numCol : Int, maxMines : Int, bListener : BoardListener) {

    val ROW : Int
    val COL : Int
    val MAX_MINES : Int

    val MINE_CELL = 9
    val MINE_CELL_PRESSED = -1
    val EMPTY_CELL_PRESSED = 0
    val listener : BoardListener

    var expansionElements: Stack<Int>

    var data : IntArray

    init {
        ROW = numRow
        COL = numCol
        MAX_MINES = maxMines
        listener = bListener
        data = IntArray ( numRow*numCol , { _ -> 10})
        expansionElements = Stack <Int>()
    }

    fun fillBoard(){

        var placedMines : Int  = 0
        var mineGenerator = MineGenerator (ROW * COL)

        while (placedMines < MAX_MINES) {
            var mine = mineGenerator.getMine()
            if(placeMine(mine)){
                placedMines++
            }
        }
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

    fun click (col : Int, row : Int) : Int{
        val element = getElementAt(col, row)

        if(isMine(element)){
            listener.minePressed()
        }
        else if (element >= MINE_CELL){
            expansion(col, row)
        }

        if(checkForEndGame())
            listener.gameEnded()

        return getElementAt(col, row)
    }

    fun checkForEndGame() : Boolean{
        for(element in data){
            if(element > MINE_CELL)
                return false
        }
        return true
    }

    fun expansion (col : Int, row: Int){

        var stack = Stack<Int> ()

        expansionElements.clear()
        stack.push((row * COL) +  col)

        while (!stack.empty()){
            var temp = stack.pop()

            val col = temp % COL
            var row = temp / COL

            if(data[temp] == 10){

                data[temp] = 0
                expansionElements.push(temp)

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
                expansionElements.push(temp)
            }
        }

    }

    fun getExpansion (): Stack<Int>{
        return expansionElements
    }


    fun isMine(element : Int) : Boolean {
        return (element == MINE_CELL)
    }

    fun getElementAt(pos : Int) : Int{
        return data[pos]
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
                nRow += "  " + value + " "
                //nRow += "  0 " //TODO change here to 0
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