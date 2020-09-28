package com.eran.simpletetris

import android.R.attr.*
import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.DisplayMetrics
import android.view.MotionEvent
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.random.Random.Default.nextInt


class MainActivity : AppCompatActivity() {

    private val sharedPrefFile = "com.eran.simpletetris.highScore"

    enum class Shape {
        Line, Square, Plus, S1, S2, L1, L2
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (supportActionBar != null)
            supportActionBar?.hide()

        val sharedPreferences: SharedPreferences = this.getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)
        var highScoreValue = sharedPreferences.getInt("highScore_key", 0)
        textHighScoreVal.text = highScoreValue.toString()

        val handler = Handler()

//        val mp_click = MediaPlayer.create(this,R.raw.soft_click)

        btnLeft.isEnabled = false
        btnRight.isEnabled = false
        btnDown.isEnabled = false
        btnPause.isEnabled = false
        btnPlay.isEnabled = false
        GameBoard.isEnabled = false

//        val lp = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)

//        val rows = arrayOf(
//            Row_0, Row_1, Row_2, Row_3, Row_4, Row_5, Row_6, Row_7, Row_8, Row_9, Row_10, Row_11, Row_12, Row_13, Row_14, Row_15, Row_16
//        )

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
            View_16_0, View_16_1, View_16_2, View_16_3, View_16_4, View_16_5, View_16_6, View_16_7, View_16_8, View_16_9,
            View_17_0, View_17_1, View_17_2, View_17_3, View_17_4, View_17_5, View_17_6, View_17_7, View_17_8, View_17_9,
            View_18_0, View_18_1, View_18_2, View_18_3, View_18_4, View_18_5, View_18_6, View_18_7, View_18_8, View_18_9
        )

        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)

        var height = displayMetrics.heightPixels
        var squareSize = (height - 200) / 25
        var startPosition = 4
        var gameOver = true
        var btnDownIsPressed = false

        for (item in board1d) {
            item.layoutParams.height = squareSize
            item.layoutParams.width = squareSize
        }

        val rowsShapeLand = mutableSetOf<Int>()
        var score = 0
        var delay = 800F
        var shape = Shape.values()[nextInt(7)]  //get a random shape from Shape Enum

        val shapeMap = mapOf(
            Shape.Line to arrayOf(0 + startPosition, 10 + startPosition, 20 + startPosition, 30 + startPosition),
            Shape.Square to arrayOf(0 + startPosition, 1 + startPosition, 10 + startPosition, 11 + startPosition),
            Shape.Plus to arrayOf(0 + startPosition, 10 + startPosition, 11 + startPosition, 20 + startPosition),
            Shape.S1 to arrayOf(1 + startPosition, 10 + startPosition, 11 + startPosition, 20 + startPosition),
            Shape.S2 to arrayOf(0 + startPosition, 10 + startPosition, 11 + startPosition, 21 + startPosition),
            Shape.L1 to arrayOf(0 + startPosition, 1 + startPosition, 11 + startPosition, 21 + startPosition),
            Shape.L2 to arrayOf(0 + startPosition, 1 + startPosition, 10 + startPosition, 20 + startPosition)
        )

        var shapeArray = IntArray(4)
        var shapeArrayLand = IntArray(4)
        var shapeColor: Int = Color.WHITE

        val shapeColorMap = mapOf(
            Shape.Line to Color.CYAN,
            Shape.Square to Color.BLUE,
            Shape.Plus to Color.GREEN,
            Shape.S1 to Color.RED,
            Shape.S2 to Color.YELLOW,
            Shape.L1 to Color.MAGENTA,
            Shape.L2 to Color.WHITE
        )

        fun setScore() {
            textScoreVal.text = score.toString()
        }

        fun newBoard() {
            highScoreValue = sharedPreferences.getInt("highScore_key", 0)
            textHighScoreVal.text = highScoreValue.toString()
            score = 0
            delay = 800F
            setScore()
            gameOver = false
            textInfo.visibility = View.INVISIBLE
            for (item in board1d) {
                item.backgroundTintList = ColorStateList.valueOf(Color.BLACK)
                item.tag = "Empty"

            }

            btnLeft.isEnabled = true
            btnRight.isEnabled = true
            btnDown.isEnabled = true
            GameBoard.isEnabled = true
            btnPause.isEnabled = true

        }

        fun gameOver() {
            gameOver = true
            btnLeft.isEnabled = false
            btnRight.isEnabled = false
            btnDown.isEnabled = false
            GameBoard.isEnabled = false
            btnPlay.isEnabled = false
            btnPause.isEnabled = false
            textInfo.visibility = View.VISIBLE
            if (score > highScoreValue) {
                val editor: SharedPreferences.Editor = sharedPreferences.edit()
                editor.putInt("highScore_key", score)
                editor.apply()
                editor.commit()
            }
        }

        fun shapeLand(){
            var land = false
            var i = 10
            fun shapeLandColor(){
                for (item in shapeArray){
                    if ((item +i -10)<190 && board1d[item + i - 10].tag == "Empty") {
                        board1d[item + i-10].backgroundTintList = ColorStateList.valueOf(Color.DKGRAY)
//                        runOnUiThread {
////                            board1d[item + i - 10].layoutParams.width -=2
////                            board1d[item + i - 10].layoutParams.height -= 2
//                            lp.setMargins(2, 2, 2, 2)
//                            board1d[item + i - 10].layoutParams = lp
//                        }
                    }
                }
                land = true
                for ((n, item) in shapeArray.withIndex()){
                    if ((item +i-10)<190) {
                        shapeArrayLand[n] = item + i -10
                    }
                }
            }
            while (!land){
                for (item in shapeArray){
                    if ((item + i) > 189){
                        shapeLandColor()
                        break
                    }
                    if (board1d[item + i].tag == "Full"){
                        shapeLandColor()
                        break
                    }
                }
                i+=10
            }
        }

        fun shapeLandClear (){
            for (item in shapeArrayLand) {
                if (board1d[item].tag == "Empty") {
                    board1d[item].backgroundTintList = ColorStateList.valueOf(Color.BLACK)
//                    runOnUiThread {
////                        board1d[item].layoutParams.width = squareSize
////                        board1d[item].layoutParams.height = squareSize
//                        lp.setMargins(1, 1, 1, 1)
//                        board1d[item].layoutParams = lp
//                    }
                }
            }
        }

        fun newShape(): Boolean {
//            mp_click.stop()
//            mp_click.prepare()
//            mp_click.start()
            handler.removeCallbacksAndMessages(null);
            btnDownIsPressed = false
            shape = Shape.values()[nextInt(7)]
            shapeArray = shapeMap[shape]?.toIntArray()!!
            shapeColor = shapeColorMap[shape]!!
            for (n in shapeArray) {
                if (board1d[n].tag == "Full") {
                    return false
                }
                board1d[n].backgroundTintList = ColorStateList.valueOf(shapeColor as Int)
                board1d[n].tag = "Shape"
            }
            shapeLandClear()
            shapeLand()
            return true
        }

        fun moveRight() {
//            mp_click.stop()
//            mp_click.prepare()
//            mp_click.start()
            for (n in shapeArray) {
                if (n % 10 == 9 || board1d[n + 1].tag == "Full") {
                    return
                }
            }
            for (i in 3 downTo 0) {
                board1d[shapeArray[i] + 1].backgroundTintList = ColorStateList.valueOf(shapeColor)
                board1d[shapeArray[i] + 1].tag = "Shape"
                board1d[shapeArray[i]].backgroundTintList = ColorStateList.valueOf(Color.BLACK)
                board1d[shapeArray[i]].tag = "Empty"
                shapeArray[i] = shapeArray[i] + 1
            }
            shapeLandClear()
            shapeLand()
        }

        fun moveLeft() {
//            mp_click.stop()
//            mp_click.prepare()
//            mp_click.start()
            for (n in shapeArray) {
                if (n % 10 == 0 || board1d[n - 1].tag == "Full") {
                    return
                }
            }
            for (i in 0..3) {
                board1d[shapeArray[i] - 1].backgroundTintList = ColorStateList.valueOf(shapeColor)
                board1d[shapeArray[i] - 1].tag = "Shape"
                board1d[shapeArray[i]].backgroundTintList = ColorStateList.valueOf(Color.BLACK)
                board1d[shapeArray[i]].tag = "Empty"
                shapeArray[i] = shapeArray[i] - 1
            }
            shapeLandClear()
            shapeLand()
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
//            mp_click.stop()
//            mp_click.prepare()
//            mp_click.start()
            score++
            setScore()
            delay = (delay * 0.998).toFloat()
            val moveDownRunnable: Runnable = object : Runnable {
                override fun run() {
                    moveDown()
                }
            }
            for (n in shapeArray) {
                if (n / 10 == 18 || board1d[n + 10].tag == "Full") {
                    for (i in shapeArray) {
                        board1d[i].tag = "Full"
                        rowsShapeLand.add(i / 10)
                    }
                    rowsToDelete()
                    if (rowsShapeLand.size > 0) {
                        delay += rowsShapeLand.size * rowsShapeLand.size * rowsShapeLand.size
                        score += rowsShapeLand.size * rowsShapeLand.size * 10
                        setScore()
                        deleteRows()
                        moveRowsDown()
                    }
                    rowsShapeLand.clear()
                    if (newShape()) {
                        handler.removeCallbacks(moveDownRunnable)
                        handler.postDelayed(moveDownRunnable, delay.toLong());
                    } else {
                        gameOver()
                    }
                    return
                }
            }
            for (i in 3 downTo 0) {
                board1d[shapeArray[i] + 10].backgroundTintList = ColorStateList.valueOf(shapeColor)
                board1d[shapeArray[i] + 10].tag = "Shape"
                board1d[shapeArray[i]].backgroundTintList = ColorStateList.valueOf(Color.BLACK)
                board1d[shapeArray[i]].tag = "Empty"
                shapeArray[i] = shapeArray[i] + 10
            }
            if (!btnDownIsPressed) {
                handler.removeCallbacks(moveDownRunnable)
                handler.postDelayed(moveDownRunnable, delay.toLong());
            }
        }

        fun removeShape() {
            for (n in shapeArray) {
                board1d[n].backgroundTintList = ColorStateList.valueOf(Color.BLACK)
                board1d[n].tag = "Empty"
            }
        }

        fun createShape() {
            for (n in shapeArray) {
                board1d[n].backgroundTintList = ColorStateList.valueOf(shapeColor)
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
                    createShape()
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
                    createShape()
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
                    createShape()
                } else if (shapeArray[3] - shapeArray[1] == 11) {
                    if (board1d[shapeArray[2] + 1].tag != "Empty" || shapeArray[2] % 10 == 9) {
                        return
                    }
                    removeShape()
                    shapeArray[3] = shapeArray[2] + 1
                    createShape()
                } else if (shapeArray[3] - shapeArray[1] == 2) {
                    if (shapeArray[2] > 180) {
                        return
                    }
                    if (board1d[shapeArray[2] + 10].tag != "Empty") {
                        return
                    }
                    removeShape()
                    shapeArray[1]++
                    shapeArray[2]++
                    shapeArray[3] += 9
                    createShape()
                } else {
                    if (board1d[shapeArray[1] - 1].tag != "Empty" || shapeArray[1] % 10 == 0) {
                        return
                    }
                    removeShape()
                    shapeArray[0] = shapeArray[1] - 1
                    createShape()
                }
            } else if (shape == Shape.S1) {
                if (shapeArray[1] - shapeArray[0] == 1) {
                    if (shapeArray[2] / 10 == 18) {
                        return
                    }
                    if (board1d[shapeArray[2] + 9].tag == "Full") {
                        return
                    }
                    removeShape()
                    shapeArray[0] = shapeArray[1]
                    shapeArray[1] = shapeArray[2] - 1
                    shapeArray[3] = shapeArray[1] + 10
                    createShape()
                } else {
                    if (shapeArray[0] % 10 == 9 || board1d[shapeArray[2] + 1].tag == "Full") {
                        return
                    }
                    removeShape()
                    shapeArray[1] = shapeArray[0]
                    shapeArray[0]--
                    shapeArray[3] = shapeArray[2] + 1
                    createShape()
                }
            } else if (shape == Shape.S2) {
                if (shapeArray[1] - shapeArray[0] == 1) {
                    if (shapeArray[3] + 10 > 189) {
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
                    createShape()
                } else {
                    if (shapeArray[2] % 10 == 9 || board1d[shapeArray[0] + 2].tag == "Full") {
                        return
                    }
                    removeShape()
                    shapeArray[2] = shapeArray[1]
                    shapeArray[0]++
                    shapeArray[1] = shapeArray[0] + 1
                    shapeArray[3] = shapeArray[2] + 1
                    createShape()
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
                    createShape()
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
                    createShape()
                } else if (shapeArray[1] - shapeArray[0] == 8) {
                    if (shapeArray[1] / 10 == 18 || board1d[shapeArray[2] + 10].tag != "Empty" ||
                        board1d[shapeArray[2] + 11].tag != "Empty" || board1d[shapeArray[2] - 10].tag != "Empty"
                    ) {
                        return
                    }
                    removeShape()
                    shapeArray[1] = shapeArray[2]
                    shapeArray[0] = shapeArray[1] - 10
                    shapeArray[2] = shapeArray[1] + 10
                    shapeArray[3] = shapeArray[2] + 1
                    createShape()
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
                    createShape()
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
                    createShape()
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
                    createShape()
                } else if (shapeArray[3] - shapeArray[1] == 2) {
                    if (shapeArray[1] / 10 == 18 || board1d[shapeArray[2] + 10].tag != "Empty" ||
                        board1d[shapeArray[2] - 9].tag != "Empty" || board1d[shapeArray[2] - 10].tag != "Empty"
                    ) {
                        return
                    }
                    removeShape()
                    shapeArray[0] = shapeArray[2] - 10
                    shapeArray[1] = shapeArray[0] + 1
                    shapeArray[3] = shapeArray[2] + 10
                    createShape()
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
                    createShape()
                }
            }
            shapeLandClear()
            shapeLand()
        }

        btnPlay.setOnClickListener {
            btnLeft.isEnabled = true
            btnRight.isEnabled = true
            btnDown.isEnabled = true
            GameBoard.isEnabled = true
            btnPause.isEnabled = true
            btnPlay.isEnabled = false
            moveDown()
        }

        btnReset.setOnClickListener() {
            handler.removeCallbacksAndMessages(null);
            newBoard()
            newShape()
            Handler(Looper.getMainLooper()).postDelayed({
                moveDown()
            }, delay.toLong())
        }

        btnPause.setOnClickListener() {
            handler.removeCallbacksAndMessages(null);
            btnLeft.isEnabled = false
            btnRight.isEnabled = false
            btnDown.isEnabled = false
            GameBoard.isEnabled = false
            btnPlay.isEnabled = true
            btnPause.isEnabled = false
        }

        val moveDownRunnable: Runnable = object : Runnable {
            override fun run() {
                if (gameOver) {
                    handler.removeCallbacksAndMessages(null);
                } else {
                    moveDown()
                    handler.postDelayed(this, 40)
                }
            }
        }

        val moveRightRunnable: Runnable = object : Runnable {
            override fun run() {
                moveRight()
                handler.postDelayed(this, 20)
            }
        }

        val moveLeftRunnable: Runnable = object : Runnable {
            override fun run() {
                moveLeft()
                handler.postDelayed(this, 20)
            }
        }

        btnDown.setOnTouchListener { v, event ->
            when (event?.action) {
                MotionEvent.ACTION_DOWN -> {
                    btnDownIsPressed = true
                    handler.removeCallbacksAndMessages(null);
                    handler.postDelayed(moveDownRunnable, 200);
                }
                MotionEvent.ACTION_UP -> {
                    if (btnDownIsPressed) {
                        btnDownIsPressed = false
                        if (!gameOver) {
                            handler.removeCallbacksAndMessages(null);
                            moveDown()
                        } else {
                            handler.removeCallbacksAndMessages(null);
                        }
                    } else {
                        handler.removeCallbacksAndMessages(null);
                        Handler(Looper.getMainLooper()).postDelayed({
                            moveDown()
                        }, delay.toLong())
                    }
                }
            }
            v?.onTouchEvent(event) ?: true
        }

        btnRight.setOnTouchListener { v, event ->
            when (event?.action) {
                MotionEvent.ACTION_DOWN -> {
                    moveRight()
                    handler.postDelayed(moveRightRunnable, 200);
                }
                MotionEvent.ACTION_UP -> {
                    handler.removeCallbacks(moveRightRunnable)
                }
            }
            v?.onTouchEvent(event) ?: true
        }

        btnLeft.setOnTouchListener { v, event ->
            when (event?.action) {
                MotionEvent.ACTION_DOWN -> {
                    moveLeft()
                    handler.postDelayed(moveLeftRunnable, 200);
                }
                MotionEvent.ACTION_UP -> {
                    handler.removeCallbacks(moveLeftRunnable)
                }
            }
            v?.onTouchEvent(event) ?: true
        }

        GameBoard.setOnClickListener() {
            rotate()
        }
    }
}