package com.example.myapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class ActivityAuthorization : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authorization)
    }

    object DataHolder{
        var data: User = User("","","")
    }

    fun onClickToMap(view: View){
        val userLogin: EditText = findViewById(R.id.user_login_auth)
        val userPass: EditText = findViewById(R.id.user_pass_auth)

        val login = userLogin.text.toString().trim()
        val pass = userPass.text.toString().trim()

        if(login == "" || pass == "")
            Toast.makeText(this, "Не все поля заполнены", Toast.LENGTH_LONG).show()
        else{
            val db = DbHelper(this, null)
            val isAuth = db.isUserAuth(login, pass)
            if (isAuth){

                DataHolder.data = db.getUser(login)

                Toast.makeText(this, "Пользователь $login авторизован", Toast.LENGTH_LONG).show()
                userLogin.text.clear()
                userPass.text.clear()

                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
            else Toast.makeText(this, "Пользователь $login НЕ авторизован", Toast.LENGTH_LONG).show()
        }
    }

    fun onClickToReg(view : View){
        val intent = Intent(this, ActivityRegistration::class.java)
        startActivity(intent)
    }
}