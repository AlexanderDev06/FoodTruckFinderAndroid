package com.example.youfood

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.coroutines.awaitStringResponseResult
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.runBlocking

class SignupScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup_screen)

        val signupButton = findViewById<Button>(R.id.signupButton)
        val emailEdit = findViewById<EditText>(R.id.signupEmailEdit)
        val passwordEdit = findViewById<EditText>(R.id.signupPasswordEdit)
        val backButton = findViewById<Button>(R.id.signupBackButton)


        signupButton.setOnClickListener {
            runBlocking {
                val (_, _, result) = Fuel.post("http://foodtruckfindermi.com/signup", listOf("email" to emailEdit.text, "password" to passwordEdit.text))
                    .awaitStringResponseResult()

                result.fold( {data ->

                    if (data == "true") {
                        val db = DBHelper(this@SignupScreen, null)

                        db.addUser(emailEdit.text.toString(), passwordEdit.text.toString())
                        startIntent()
                    } else if (data == "false") {
                        val snackbar = Snackbar.make(
                            it, "Email Already Used",
                            Snackbar.LENGTH_SHORT
                        ).setAction("Action", null)

                        snackbar.show()

                    }
                             },
                    {error -> Log.e("http", "$error")})
            }
        }



        backButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun startIntent(){
        val intent = Intent(this, UserScreen::class.java)
        startActivity(intent)
    }
}
