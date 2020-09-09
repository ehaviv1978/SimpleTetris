package com.eran.simpletetris

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.String
import java.util.*
import kotlin.concurrent.schedule
import kotlin.random.Random.Default.nextInt

class MainActivity : AppCompatActivity() {

    private var timer = Timer()

    enum class Shape {
        Line, Square, Plus, S1, S2, L1, L2
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val board1d = arrayOf(
            View_0_0, View_0_1, View_0_2, View_0_3, View_0_4, View_0_5, View_0_6, View_0_7, View_0_8, View_0_9,
            View_1_0, View_1_1, View_1_2, View_1_3, View_1_4, View_1_5, View_1_6, View_1_7, View_1_8, View_1_9,
            View_2_0, View_2_1, View_2_2, View_2_3, View_2_4, View_2_5, View_2_6, View_2_7, View_2_8, View_2_9,
            View_3_0, View_3_1, View_3_2, View_3_3, View_3_4, View_3_5, View_3_6, View_3_7, View_3_8, View_3_9,
            View_4_0, View_4_1, View_4_2, View_4_3, View_4_4, View_4_5, View_4_6, View_4_7, View_4_8, View_4_9,
            View_5_0, View_5_1, View_5_2, View_5_3, View_5_4, View_5_5, View_5_6, View_5_7, View_5_8, View_5_9,
            View_6_0, View_6_1, View_6_2, View_6_3, View_6_4, View_6_5, View_6_6, View_6_7, View_6_8, View_6_9,
            View_7_0, View_7_1, View_7_2, View_7_3, View_7_4, View_7_5, View_7_6, View_7_7, View_7_8, View_7_9,
            View_8_0, View_8_1, View_8_2, View_8_3, View_8_4, View_8_5, View_8_6, View_8_7, View_8_8, View_8_9,
            View_9_0, View_9_1, View_9_2, View_9_3, View_9_4, View_9_5, View_9_6, View_9_7, View_9_8, View_9_9,
            View_10_0, View_10_1, View_10_2, View_10_3, View_10_4, View_10_5, View_10_6, View_10_7, View_10_8, View_10_9,
            View_11_0, View_11_1, View_11_2, View_11_3, View_11_4, View_11_5, View_11_6, View_11_7, View_11_8, View_11_9,
            View_12_0, View_12_1, View_12_2, View_12_3, View_12_4, View_12_5, View_12_6, View_12_7, View_12_8, View_12_9,
            View_13_0, View_13_1, View_13_2, View_13_3, View_13_4, View_13_5, View_13_6, View_13_7, View_13_8, View_13_9,
            View_14_0, View_14_1, View_14_2, View_14_3, View_14_4, View_14_5, View_14_6, View_14_7, View_14_8, View_14_9,
            View_15_0, View_15_1, View_15_2, View_15_3, View_15_4, View_15_5, View_15_6, View_15_7, View_15_8, View_15_9,
            View_16_0, View_16_1, View_16_2, View_16_3, View_16_4, View_16_5, View_16_6, View_16_7, View_16_8, View_16_9
        )

        val rowsShapeLand = mutableSetOf<Int>()
        var score =0
        var delay: Long = 1000
        var shape = Shape.values()[(nextInt(Shape.values().size))]

        val shapeMap = mapOf(
            Shape.Line to arrayOf(0, 10, 20, 30),
            Shape.Square to arrayOf(0, 1, 10, 11),
            Shape.Plus to arrayOf(0, 1, 2, 11),
            Shape.S1 to arrayOf(0, 1, 11, 12),
            Shape.S2 to arrayOf(1, 2, 10, 11),
            Shape.L1 to arrayOf(0, 1, 2, 10),
            Shape.L2 to arrayOf(0, 1, 2, 12)
        )

        var shapeArray = intArrayOf(4)

        val shapeColorMap = mapOf(
            Shape.Line to Color.CYAN,
            Shape.Square to Color.BLUE,
            Shape.Plus to Color.GREEN,
            Shape.S1 to Color.RED,
            Shape.S2 to Color.YELLOW,
            Shape.L1 to Color.MAGENTA,
            Shape.L2 to Color.LTGRAY
        )

        fun setScore(){
            runOnUiThread { textScoreVal.text = score.toString() }
        }

        fun newBoard() {
            score =0
            setScore()
            runOnUiThread {
                textInfo.visibility = View.INVISIBLE
                btnMoveDown.isEnabled = true
                btnRotate.isEnabled = true
                btnMoveLeft.isEnabled = true
                btnMoveRight.isEnabled = true
            }
            for (item in board1d) {
                item.backgroundTintList = ColorStateList.valueOf(Color.BLACK)
                item.tag = "Empty"
            }
        }

        fun gameOver(){
            runOnUiThread {
                btnMoveDown.isEnabled = false
                btnRotate.isEnabled = false
                btnMoveLeft.isEnabled = false
                btnMoveRight.isEnabled = false
                textInfo.visibility = View.VISIBLE
            }
        }

        fun newShape():Boolean {
            shape = Shape.values()[(nextInt(Shape.values().size))]
            shapeArray = shapeMap[shape]?.toIntArray()!!
            for (n in shapeArray) {
                if (board1d[n].tag == "Full"){
                    return false
                }
                board1d[n].backgroundTintList = ColorStateList.valueOf(shapeColorMap[shape] as Int)
                board1d[n].tag = "Shape"
            }
            return true
        }

        fun moveRight() {
            for (n in shapeArray) {
                if (n % 10 == 9 || board1d[n + 1].tag == "Full") {
                    return
                }
            }
            for (i in 3 downTo 0) {
                board1d[shapeArray[i] + 1].backgroundTintList = board1d[shapeArray[i]].backgroundTintList
                board1d[shapeArray[i] + 1].tag = "Shape"
                board1d[shapeArray[i]].backgroundTintList = ColorStateList.valueOf(Color.BLACK)
                board1d[shapeArray[i]].tag = "Empty"
                shapeArray[i] = shapeArray[i] + 1
            }
        }

        fun moveLeft() {
            for (n in shapeArray) {
                if (n % 10 == 0 || board1d[n - 1].tag == "Full") {
                    return
                }
            }
            for (i in 0..3) {
                board1d[shapeArray[i] - 1].backgroundTintList = board1d[shapeArray[i]].backgroundTintList
                board1d[shapeArray[i] - 1].tag = "Shape"
                board1d[shapeArray[i]].backgroundTintList = ColorStateList.valueOf(Color.BLACK)
                board1d[shapeArray[i]].tag = "Empty"
                shapeArray[i] = shapeArray[i] - 1
            }
        }

        fun rowsToDelete() {
            val rowsNotFull = mutableListOf<Int>()
            for (n in rowsShapeLand) {
                for (i in 0 until 10) {
                    if (board1d[(n * 10) + i].tag == "Empty") {
                        rowsNotFull.add(n)
                    }
                }
            }
            rowsShapeLand.removeAll(rowsNotFull)
        }

        fun deleteRows() {
            for (row in rowsShapeLand) {
                for (i in 0 until 10) {
                    board1d[(row * 10) + i].tag = "Empty"
                    board1d[(row * 10) + i].backgroundTintList = ColorStateList.valueOf(Color.BLACK)
                }
            }
        }

        fun moveRowsDown() {
            for (row in rowsShapeLand) {
                for (n in (row * 10 + 9) downTo 10) {
                    board1d[n].tag = board1d[n - 10].tag
                    board1d[n].backgroundTintList = board1d[n - 10].backgroundTintList
                }
                for (i in 0 until 10) {
                    board1d[i].tag = "Empty"
                    board1d[i].backgroundTintList = ColorStateList.valueOf(Color.BLACK)
                }
            }
        }

        fun moveDown() {
            score++
            setScore()
            delay--
            for (n in shapeArray) {
                if (n / 10 == 16 || board1d[n + 10].tag == "Full") {
                    for (i in shapeArray) {
                        board1d[i].tag = "Full"
                        rowsShapeLand.add(i / 10)
                    }
                    rowsToDelete()
                    if (rowsShapeLand.size > 0) {
                        delay += rowsShapeLand.size * rowsShapeLand.size
                        score += rowsShapeLand.size * rowsShapeLand.size * 10
                        setScore()
                        deleteRows()
                        moveRowsDown()
                    }
                    rowsShapeLand.clear()
                    if (newShape()) {
                        timer.schedule(delay) {
                            moveDown()
                        }
                    }else {
                        gameOver()
                    }
                    return
                }
            }
            for (i in 3 downTo 0) {
                board1d[shapeArray[i] + 10].backgroundTintList = board1d[shapeArray[i]].backgroundTintList
                board1d[shapeArray[i] + 10].tag = "Shape"
                board1d[shapeArray[i]].backgroundTintList = ColorStateList.valueOf(Color.BLACK)
                board1d[shapeArray[i]].tag = "Empty"
                shapeArray[i] = shapeArray[i] + 10
            }
            timer.schedule(delay) {
                moveDown()
            }
        }

        fun removeShape() {
            for (n in shapeArray) {
                board1d[n].backgroundTintList = ColorStateList.valueOf(Color.BLACK)
                board1d[n].tag = "Empty"
            }
        }

        fun createShape(color: Int) {
            for (n in shapeArray) {
                board1d[n].backgroundTintList = ColorStateList.valueOf(color)
                board1d[n].tag = "Shape"
            }
        }

        fun rotate() {
            if (shape == Shape.Line) {
                if (shapeArray[0] + 1 == shapeArray[1]) {
                    if (shapeArray[0] / 10 > 14 || board1d[shapeArray[2] + 10].tag != "Empty" ||
                        board1d[shapeArray[2] + 20].tag != "Empty"
                    ) {
                        return
                    }
                    removeShape()
                    shapeArray[0] = shapeArray[1] - 10
                    shapeArray[2] = shapeArray[1] + 10
                    shapeArray[3] = shapeArray[1] + 20
                    createShape(Color.CYAN)
                } else {
                    if (shapeArray[0] % 10 == 0 || shapeArray[0] % 10 > 7 || board1d[shapeArray[2] - 1].tag != "Empty" ||
                        board1d[shapeArray[2] + 1].tag != "Empty" || board1d[shapeArray[2] + 2].tag != "Empty"
                    ) {
                        return
                    }
                    removeShape()
                    shapeArray[0] = shapeArray[1] - 1
                    shapeArray[2] = shapeArray[1] + 1
                    shapeArray[3] = shapeArray[1] + 2
                    createShape(Color.CYAN)
                }
            } else if (shape == Shape.Plus) {
                if (shapeArray[3] - shapeArray[1] == 10 && shapeArray[1] - shapeArray[0] == 1) {
                    if (shapeArray[1] - 10 < 0) {
                        return
                    }
                    removeShape()
                    shapeArray[0] = shapeArray[1] - 10
                    shapeArray[1] = shapeArray[0] + 9
                    shapeArray[2] = shapeArray[1] + 1
                    shapeArray[3] = shapeArray[2] + 10
                    createShape(Color.GREEN)
                } else if (shapeArray[3] - shapeArray[1] == 11) {
                    if (board1d[shapeArray[2] + 1].tag != "Empty" || shapeArray[2] % 10 == 9) {
                        return
                    }
                    removeShape()
                    shapeArray[3] = shapeArray[2] + 1
                    createShape(Color.GREEN)
                } else if (shapeArray[3] - shapeArray[1] == 2) {
                    if (shapeArray[2] > 160) {
                        return
                    }
                    if (board1d[shapeArray[2] + 10].tag != "Empty") {
                        return
                    }
                    removeShape()
                    shapeArray[1]++
                    shapeArray[2]++
                    shapeArray[3] += 9
                    createShape(Color.GREEN)
                } else {
                    if (board1d[shapeArray[1] - 1].tag != "Empty" || shapeArray[1] % 10 == 0) {
                        return
                    }
                    removeShape()
                    shapeArray[0] = shapeArray[1] - 1
                    createShape(Color.GREEN)
                }
            } else if (shape == Shape.S1) {
                if (shapeArray[1] - shapeArray[0] == 1) {
                    if (shapeArray[2] / 10 == 16) {
                        return
                    }
                    if (board1d[shapeArray[2] + 9].tag == "Full") {
                        return
                    }
                    removeShape()
                    shapeArray[0] = shapeArray[1]
                    shapeArray[1] = shapeArray[2] - 1
                    shapeArray[3] = shapeArray[1] + 10
                    createShape(Color.RED)
                } else {
                    if (shapeArray[0] % 10 == 9 || board1d[shapeArray[2] + 1].tag == "Full") {
                        return
                    }
                    removeShape()
                    shapeArray[1] = shapeArray[0]
                    shapeArray[0]--
                    shapeArray[3] = shapeArray[2] + 1
                    createShape(Color.RED)
                }
            } else if (shape == Shape.S2) {
                if (shapeArray[1] - shapeArray[0] == 1) {
                    if (shapeArray[3] + 10 > 169) {
                        return
                    }
                    if (board1d[shapeArray[3] + 10].tag == "Full") {
                        return
                    }
                    removeShape()
                    shapeArray[2] = shapeArray[3]
                    shapeArray[3] += 10
                    shapeArray[1] = shapeArray[2] - 1
                    shapeArray[0]--
                    createShape(Color.YELLOW)
                } else {
                    if (shapeArray[2] % 10 == 9 || board1d[shapeArray[0] + 2].tag == "Full") {
                        return
                    }
                    removeShape()
                    shapeArray[2] = shapeArray[1]
                    shapeArray[0]++
                    shapeArray[1] = shapeArray[0] + 1
                    shapeArray[3] = shapeArray[2] + 1
                    createShape(Color.YELLOW)
                }
            } else if (shape == Shape.L1) {
                if (shapeArray[1] - shapeArray[0] == 1 && shapeArray[2] - shapeArray[1] == 1) {
                    if (shapeArray[0] / 10 == 0) {
                        return
                    }
                    if (board1d[shapeArray[0] - 10].tag != "Empty" || board1d[shapeArray[1] - 10].tag != "Empty"
                        || board1d[shapeArray[1] + 10].tag != "Empty"
                    ) {
                        return
                    }
                    removeShape()
                    shapeArray[2] = shapeArray[1]
                    shapeArray[1] = shapeArray[2] - 10
                    shapeArray[3] = shapeArray[2] + 10
                    shapeArray[0] = shapeArray[1] - 1
                    createShape(Color.MAGENTA)
                } else if (shapeArray[1] - shapeArray[0] == 1) {
                    if (shapeArray[1] % 10 == 9 || board1d[shapeArray[2] - 1].tag != "Empty" ||
                        board1d[shapeArray[2] + 11].tag != "Empty" || board1d[shapeArray[2] - 9].tag != "Empty"
                    ) {
                        return
                    }
                    removeShape()
                    shapeArray[0] = shapeArray[2] - 9
                    shapeArray[1] = shapeArray[2] - 1
                    shapeArray[3] = shapeArray[2] + 1
                    createShape(Color.MAGENTA)
                } else if (shapeArray[1] - shapeArray[0] == 8) {
                    if (shapeArray[1] / 10 == 16 || board1d[shapeArray[2] + 10].tag != "Empty" ||
                        board1d[shapeArray[2] + 11].tag != "Empty" || board1d[shapeArray[2] - 10].tag != "Empty"
                    ) {
                        return
                    }
                    removeShape()
                    shapeArray[1] = shapeArray[2]
                    shapeArray[0] = shapeArray[1] - 10
                    shapeArray[2] = shapeArray[1] + 10
                    shapeArray[3] = shapeArray[2] + 1
                    createShape(Color.MAGENTA)
                } else {
                    if (shapeArray[1] % 10 == 0 || board1d[shapeArray[1] - 1].tag != "Empty" ||
                        board1d[shapeArray[1] + 1].tag != "Empty" || board1d[shapeArray[1] + 9].tag != "Empty"
                    ) {
                        return
                    }
                    removeShape()
                    shapeArray[0] = shapeArray[1] - 1
                    shapeArray[2] = shapeArray[1] + 1
                    shapeArray[3] = shapeArray[0] + 10
                    createShape(Color.MAGENTA)
                }
            } else if (shape == Shape.L2) {
                if (shapeArray[3] - shapeArray[1] == 11) {
                    if (shapeArray[0] / 10 == 0) {
                        return
                    }
                    if (board1d[shapeArray[1] - 10].tag != "Empty" || board1d[shapeArray[1] + 10].tag != "Empty"
                        || board1d[shapeArray[1] + 9].tag != "Empty"
                    ) {
                        return
                    }
                    removeShape()
                    shapeArray[0] = shapeArray[1] - 10
                    shapeArray[3] = shapeArray[1] + 10
                    shapeArray[2] = shapeArray[3] - 1
                    createShape(Color.LTGRAY)
                } else if (shapeArray[3] - shapeArray[1] == 10) {
                    if (shapeArray[1] % 10 == 9 || board1d[shapeArray[1] + 1].tag != "Empty" ||
                        board1d[shapeArray[1] - 1].tag != "Empty" || board1d[shapeArray[1] - 11].tag != "Empty"
                    ) {
                        return
                    }
                    removeShape()
                    shapeArray[2] = shapeArray[1]
                    shapeArray[1] = shapeArray[2] - 1
                    shapeArray[3] = shapeArray[2] + 1
                    shapeArray[0] = shapeArray[1] - 10
                    createShape(Color.LTGRAY)
                } else if (shapeArray[3] - shapeArray[1] == 2) {
                    if (shapeArray[1] / 10 == 16 || board1d[shapeArray[2] + 10].tag != "Empty" ||
                        board1d[shapeArray[2] - 9].tag != "Empty" || board1d[shapeArray[2] - 10].tag != "Empty"
                    ) {
                        return
                    }
                    removeShape()
                    shapeArray[0] = shapeArray[2] - 10
                    shapeArray[1] = shapeArray[0] + 1
                    shapeArray[3] = shapeArray[2] + 10
                    createShape(Color.LTGRAY)
                } else {
                    if (shapeArray[0] % 10 == 0 || board1d[shapeArray[2] - 1].tag != "Empty" ||
                        board1d[shapeArray[2] + 1].tag != "Empty" || board1d[shapeArray[2] + 11].tag != "Empty"
                    ) {
                        return
                    }
                    removeShape()
                    shapeArray[1] = shapeArray[2]
                    shapeArray[0] = shapeArray[1] - 1
                    shapeArray[2] = shapeArray[1] + 1
                    shapeArray[3] = shapeArray[2] + 10
                    createShape(Color.LTGRAY)
                }
            }
        }

        btnNewGame.setOnClickListener {
            timer.cancel()
            timer = Timer()
            newBoard()
            newShape()
            timer.schedule(delay) {
                moveDown()
            }
        }

        btnMoveRight.setOnClickListener() {
            moveRight()
        }

        btnMoveLeft.setOnClickListener() {
            moveLeft()
        }

        btnMoveDown.setOnClickListener() {
            timer.cancel()
            timer = Timer()
            moveDown()
        }

        btnRotate.setOnClickListener() {
            rotate()
        }

    }
}