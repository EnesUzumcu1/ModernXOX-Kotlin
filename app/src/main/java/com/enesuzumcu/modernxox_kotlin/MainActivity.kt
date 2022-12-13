package com.enesuzumcu.modernxox_kotlin

import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ImageButton
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.enesuzumcu.modernxox_kotlin.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var player1Buttons: MutableList<ImageButton> = mutableListOf()
    private var player2Buttons: MutableList<ImageButton> = mutableListOf()
    private var gameAreaButtons: MutableList<ImageButton> = mutableListOf()
    private var selectedDrawable: Drawable? = null
    private lateinit var selectedButton: ImageButton
    private var selectedTag: String = ""
    private var siraNo = 1
    private var winner = 0
    private var movesPossible = true
    //movesPossible = true -> drawable güncelle. movesPossible = false hamle mümkün mü kontrol et

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        atamalar()

        for (player1Button in player1Buttons) {
            player1Button.setOnClickListener {
                focusButton(player1Button, 1)
            }
        }

        for (player2Button in player2Buttons) {
            player2Button.setOnClickListener {
                focusButton(player2Button, 2)
            }
        }

        for (gameAreaButton in gameAreaButtons) {
            gameAreaButton.setOnClickListener {
                checkDrawable(gameAreaButton)
            }
        }
    }

    private fun atamalar() {
        player1Buttons.add(binding.player1Btn1)
        player1Buttons.add(binding.player1Btn2)
        player1Buttons.add(binding.player1Btn3)
        player1Buttons.add(binding.player1Btn4)
        player1Buttons.add(binding.player1Btn5)
        player1Buttons.add(binding.player1Btn6)
        player2Buttons.add(binding.player2Btn1)
        player2Buttons.add(binding.player2Btn2)
        player2Buttons.add(binding.player2Btn3)
        player2Buttons.add(binding.player2Btn4)
        player2Buttons.add(binding.player2Btn5)
        player2Buttons.add(binding.player2Btn6)
        gameAreaButtons.add(binding.btn11)
        gameAreaButtons.add(binding.btn12)
        gameAreaButtons.add(binding.btn13)
        gameAreaButtons.add(binding.btn21)
        gameAreaButtons.add(binding.btn22)
        gameAreaButtons.add(binding.btn23)
        gameAreaButtons.add(binding.btn31)
        gameAreaButtons.add(binding.btn32)
        gameAreaButtons.add(binding.btn33)
        setTagBtn()
        if (siraNo == 1) {
            binding.llPlayer1.setBackgroundColor(Color.parseColor("#FFFAEF94"))
        } else if (siraNo == 2) {
            binding.llPlayer2.setBackgroundColor(Color.parseColor("#FFFAEF94"))
        }
    }

    private fun setTagBtn() {
        binding.player1Btn1.tag = Constants.player1Btn1
        binding.player1Btn2.tag = Constants.player1Btn2
        binding.player1Btn3.tag = Constants.player1Btn3
        binding.player1Btn4.tag = Constants.player1Btn4
        binding.player1Btn5.tag = Constants.player1Btn5
        binding.player1Btn6.tag = Constants.player1Btn6
        binding.player2Btn1.tag = Constants.player2Btn1
        binding.player2Btn2.tag = Constants.player2Btn2
        binding.player2Btn3.tag = Constants.player2Btn3
        binding.player2Btn4.tag = Constants.player2Btn4
        binding.player2Btn5.tag = Constants.player2Btn5
        binding.player2Btn6.tag = Constants.player2Btn6
    }

    private fun focusButton(buton: ImageButton, oyuncuNo: Int) {
        if (siraNo == 1 && oyuncuNo == siraNo) {
            if (buton.tag.toString() == selectedTag) {
                buton.setBackgroundResource(R.drawable.oyuncu1_button)
                selectedDrawable = null
                selectedTag = ""
            } else {
                buton.setBackgroundResource(R.drawable.focus_button)
                for (button in player1Buttons) {
                    if (buton.id != button.id) {
                        button.setBackgroundResource(R.drawable.oyuncu1_button)
                    }
                }
                selectedDrawable = buton.drawable
                selectedButton = buton
                selectedTag = buton.tag.toString()
            }
        } else if (siraNo == 2 && oyuncuNo == siraNo) {
            if (buton.tag.toString() == selectedTag) {
                buton.setBackgroundResource(R.drawable.oyuncu2_button)
                selectedDrawable = null
                selectedTag = ""
            } else {
                buton.setBackgroundResource(R.drawable.focus_button)
                for (button in player2Buttons) {
                    if (buton.id != button.id) {
                        button.setBackgroundResource(R.drawable.oyuncu2_button)
                    }
                }
                selectedDrawable = buton.drawable
                selectedButton = buton
                selectedTag = buton.tag.toString()
            }
        }
    }

    private fun updateDrawable(buton: ImageButton) {
        if (selectedTag != "") {
            buton.setImageDrawable(selectedDrawable)
            buton.tag = selectedTag
            selectedTag = ""
            if (siraNo == 1) {
                player1Buttons.remove(selectedButton)
                for (button in player1Buttons) {
                    if (buton.id != button.id) {
                        button.isClickable = true
                    }
                }
                siraNo = 2
            } else if (siraNo == 2) {
                player2Buttons.remove(selectedButton)
                for (button in player2Buttons) {
                    if (buton.id != button.id) {
                        button.isClickable = true
                    }
                }
                siraNo = 1
            }
            selectedButton.isClickable = false
            selectedButton.visibility = View.INVISIBLE
            checkWinner()
            if (player1Buttons.size == 0 && player2Buttons.size == 0) {
                disableOtherClickableButtons()
                alertDialogResult()
            } else if (!(winner == 1 || winner == 2)) {
                if (!isMovesPossible()) {
                    disableOtherClickableButtons()
                    alertDialogResult()
                }
            }
            changePlayer()
        }
    }

    private fun checkDrawable(button: ImageButton): Boolean {
        return if (selectedDrawable != null) {
            if (button.tag == null) {
                if (movesPossible) {
                    updateDrawable(button)
                }
                true
            } else {
                when (button.tag.toString()) {
                    Constants.player1Btn1 -> {
                        if (selectedTag == Constants.player2Btn2
                            || selectedTag == Constants.player2Btn3
                            || selectedTag == Constants.player2Btn4
                            || selectedTag == Constants.player2Btn5
                            || selectedTag == Constants.player2Btn6
                        ) {
                            if (movesPossible) {
                                updateDrawable(button)
                            }
                            true
                        } else {
                            false
                        }
                    }
                    Constants.player1Btn2 -> {
                        if (selectedTag == Constants.player2Btn3
                            || selectedTag == Constants.player2Btn4
                            || selectedTag == Constants.player2Btn5
                            || selectedTag == Constants.player2Btn6
                        ) {
                            if (movesPossible) {
                                updateDrawable(button)
                            }
                            true
                        } else {
                            false
                        }
                    }
                    Constants.player1Btn3 -> {
                        if (selectedTag == Constants.player2Btn4
                            || selectedTag == Constants.player2Btn5
                            || selectedTag == Constants.player2Btn6
                        ) {
                            if (movesPossible) {
                                updateDrawable(button)
                            }
                            true
                        } else {
                            false
                        }
                    }
                    Constants.player1Btn4 -> {
                        if (selectedTag == Constants.player2Btn5
                            || selectedTag == Constants.player2Btn6
                        ) {
                            if (movesPossible) {
                                updateDrawable(button)
                            }
                            true
                        } else {
                            false
                        }
                    }
                    Constants.player1Btn5 -> {
                        if (selectedTag == Constants.player2Btn6
                        ) {
                            if (movesPossible) {
                                updateDrawable(button)
                            }
                            true
                        } else {
                            false
                        }
                    }
                    Constants.player1Btn6 -> {
                        //en büyük kartın üzerina başka bir kart konamaz
                        false
                    }
                    Constants.player2Btn1 -> {
                        if (selectedTag == Constants.player1Btn2
                            || selectedTag == Constants.player1Btn3
                            || selectedTag == Constants.player1Btn4
                            || selectedTag == Constants.player1Btn5
                            || selectedTag == Constants.player1Btn6
                        ) {
                            if (movesPossible) {
                                updateDrawable(button)
                            }
                            true
                        } else {
                            false
                        }
                    }
                    Constants.player2Btn2 -> {
                        if (selectedTag == Constants.player1Btn3
                            || selectedTag == Constants.player1Btn4
                            || selectedTag == Constants.player1Btn5
                            || selectedTag == Constants.player1Btn6
                        ) {
                            if (movesPossible) {
                                updateDrawable(button)
                            }
                            true
                        } else {
                            false
                        }
                    }
                    Constants.player2Btn3 -> {
                        if (selectedTag == Constants.player1Btn4
                            || selectedTag == Constants.player1Btn5
                            || selectedTag == Constants.player1Btn6
                        ) {
                            if (movesPossible) {
                                updateDrawable(button)
                            }
                            true
                        } else {
                            false
                        }
                    }
                    Constants.player2Btn4 -> {
                        if (selectedTag == Constants.player1Btn5
                            || selectedTag == Constants.player1Btn6
                        ) {
                            if (movesPossible) {
                                updateDrawable(button)
                            }
                            true
                        } else {
                            false
                        }
                    }
                    Constants.player2Btn5 -> {
                        if (selectedTag == Constants.player1Btn6
                        ) {
                            if (movesPossible) {
                                updateDrawable(button)
                            }
                            true
                        } else {
                            false
                        }
                    }
                    Constants.player2Btn6 -> {
                        //en büyük kartın üzerina başka bir kart konamaz
                        false
                    }
                    else -> {
                        false
                    }
                }
            }
        } else {
            false
        }
    }

    private fun changePlayer() {
        if (siraNo == 1) {
            binding.llPlayer1.setBackgroundColor(Color.parseColor("#FFFAEF94"))
            binding.llPlayer2.setBackgroundColor(Color.TRANSPARENT)
        } else if (siraNo == 2) {
            binding.llPlayer1.setBackgroundColor(Color.TRANSPARENT)
            binding.llPlayer2.setBackgroundColor(Color.parseColor("#FFFAEF94"))
        }
    }

    private fun compareTags(tag1: Any?, tag2: Any?, tag3: Any?) {
        if (tag1 != null && tag2 != null && tag3 != null) {
            if (splitTag(tag1.toString()) == splitTag(tag2.toString())
                && splitTag(tag1.toString()) == splitTag(tag3.toString())
            ) {
                winner = splitTag(tag1.toString()).takeLast(1).toInt()
                disableOtherClickableButtons()
                alertDialogResult()
            }
        }
    }

    private fun splitTag(tag: String): String {
        val x = tag.split("-").toTypedArray()
        return x.first()
    }

    private fun checkWinner() {
        compareTags(binding.btn11.tag, binding.btn12.tag, binding.btn13.tag)
        compareTags(binding.btn21.tag, binding.btn22.tag, binding.btn23.tag)
        compareTags(binding.btn31.tag, binding.btn32.tag, binding.btn33.tag)
        compareTags(binding.btn11.tag, binding.btn21.tag, binding.btn31.tag)
        compareTags(binding.btn12.tag, binding.btn22.tag, binding.btn32.tag)
        compareTags(binding.btn13.tag, binding.btn23.tag, binding.btn33.tag)
        compareTags(binding.btn11.tag, binding.btn22.tag, binding.btn33.tag)
        compareTags(binding.btn13.tag, binding.btn22.tag, binding.btn31.tag)
    }

    private fun alertDialogResult() {
        val builder = AlertDialog.Builder(this@MainActivity)
        builder.setTitle("Tebrikler!")
        if (winner == 1) {
            builder.setMessage("Yeşil renk kazandı.")
        } else if (winner == 2) {
            builder.setMessage("Kırmızı renk kazandı.")
        } else if (player1Buttons.size == 0 && player2Buttons.size == 0) {
            builder.setTitle("Beraberlik!")
        } else {
            builder.setTitle("Daha fazla hamle mümkün değil!")
        }
        builder.setCancelable(false)
        builder.setNeutralButton("Oyunu Göster") { _: DialogInterface?, _: Int ->
            Handler(Looper.getMainLooper()).postDelayed({
                alertDialogResult()
            }, 3000)
        }
        builder.setNegativeButton("Tekrar Oyna") { _: DialogInterface?, _: Int ->
            finish()
            startActivity(intent)
        }
        builder.show()
    }

    private fun isMovesPossible(): Boolean {
        //true -> hamle mümkün. false -> hamle mümkün değil
        movesPossible = false
        return when (siraNo) {
            1 -> {
                checkPossibleMoves(player1Buttons)
            }
            2 -> {
                checkPossibleMoves(player2Buttons)
            }
            else -> {
                movesPossible = true
                false
            }
        }
    }

    private fun checkPossibleMoves(list: MutableList<ImageButton>): Boolean {
        var movesCount = 0
        for (button in list) {
            selectedDrawable = button.drawable
            selectedTag = button.tag.toString()
            for (btn in gameAreaButtons) {
                val result = checkDrawable(btn)
                if (result) {
                    movesCount++
                }
            }
        }
        selectedDrawable = null
        movesPossible = true
        selectedTag = ""
        return movesCount != 0
    }

    private fun disableOtherClickableButtons() {
        for (btn in player1Buttons) {
            btn.isClickable = false
        }
        for (btn in player2Buttons) {
            btn.isClickable = false
        }
        for (btn in gameAreaButtons) {
            btn.isClickable = false
        }
    }
}