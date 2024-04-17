package com.example.myapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class ActivityRegistration : AppCompatActivity() {
    fun isValidEmail(input: String): Boolean {
        val pattern = Regex("""^[A-Za-z0-9]+@[a-z]+\.[A-Z|a-z].{1,}$""")
        return pattern.matches(input)
    }
    fun isValidLogin(input: String): Boolean {
        val pattern = Regex("^[a-zA-Z0-9]*$")
        return pattern.matches(input)
    }
    fun isValidPass(input: String): Boolean {
        val pattern = Regex("^(?=.*[a-zA-Z])(?=.*[0-9]).{8,}$")
        return pattern.matches(input)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        val button: Button = findViewById(R.id.btnReg)
        button.setOnClickListener(){
            val userLogin: EditText = findViewById(R.id.user_login)
            val userEmail: EditText = findViewById(R.id.user_email)
            val userPass: EditText = findViewById(R.id.user_pass)

            val login = userLogin.text.toString().trim()
            val email = userEmail.text.toString().trim()
            val pass = userPass.text.toString().trim()

            if(login == "" || email == "" || pass == ""){
                Toast.makeText(this, "Не все поля заполнены", Toast.LENGTH_LONG).show()
            }
            else if(!isValidLogin(login)) {
                Toast.makeText(this, "Логин задан неверно", Toast.LENGTH_LONG).show()
            }
            else if(!isValidEmail(email)){
                Toast.makeText(this, "Электронная почта задана неверно", Toast.LENGTH_LONG).show()
            }
            else if(!isValidPass(pass)){
                Toast.makeText(this, "Пароль задан некорректно", Toast.LENGTH_LONG).show()
            }
            else{
                val user = User(login, email, pass)

                val db = DbHelper(this,null)
                db.addUser(user)
                Toast.makeText(this, "Пользоватеь $login добавлен", Toast.LENGTH_LONG).show()

                userLogin.text.clear()
                userEmail.text.clear()
                userPass.text.clear()
            }
        }
    }

    fun onClickToAuth(view: View){
        val intent = Intent(this, ActivityAuthorization::class.java)
        startActivity(intent)
    }
}