package com.meetkokomo.www.minesweeper

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import org.jetbrains.anko.*
import org.jetbrains.anko.button

class MainScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_screen)

        alert("New game", null) {
            customView{
                verticalLayout {
                    var radioGroup = radioGroup {
                        radioButton {
                            id = R.id.rb_new_game_easy
                            textResource= R.string.dialog_new_game_easy
                        }
                        radioButton {
                            id = R.id.rb_new_game_intermediate
                            textResource = R.string.dialog_new_game_intermediate
                        }
                        radioButton {
                            id = R.id.rb_new_game_hard
                            textResource = R.string.dialog_new_game_hard
                        }
                        radioButton {
                            id = R.id.rb_new_game_custom
                            textResource = R.string.dialog_new_game_custom
                            //TODO make interface to take custom values
                        }
                    }
                    var button = button {
                        id = R.id.b_new_game
                        textResource = R.string.dialog_button_new_game
                    }
                    button.setOnClickListener{
                        val radioId = radioGroup.checkedRadioButtonId

                        if(radioId == R.id.rb_new_game_easy) {
                            startActivity<GameUIActivity>("col" to 8, "row" to 8, "mines" to 16)
                        }
                        else if(radioId == R.id.rb_new_game_intermediate) {
                            startActivity<GameUIActivity>("col" to 16, "row" to 16, "mines" to 40)
                        }
                        else if(radioId == R.id.rb_new_game_hard) {
                            startActivity<GameUIActivity>("col" to 16, "row" to 30, "mines" to 99)
                        }
                        else if(radioId == R.id.rb_new_game_custom){
                            Toast.makeText(context, "selected Custom", Toast.LENGTH_LONG).show()
                        }
                        else{
                            Toast.makeText(context, "Please Select a Game option", Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }
        }.show()

    }
}
