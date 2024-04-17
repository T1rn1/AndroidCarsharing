package com.example.myapp

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.telephony.PhoneNumberFormattingTextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast

class ActivitySecond : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        setCardInf()

        setUserInf()
    }


    object DataHolder{
        var haveCard: Boolean = false
        var cardNumber:String = ""
        var cardBalance:Double = 100.0
        var cardStarus = "Нет карты"
    }

    //Проверка правильности введенного номера карты
    fun isValidCard(input: String): Boolean {
        return input.length == 16 && input.all { it.isDigit() }
    }

    private fun setUserInf(){
        val user: User = ActivityAuthorization.DataHolder.data
        val loginText: TextView = findViewById(R.id.user_login_prof)
        val emailText: TextView = findViewById(R.id.user_email_prof)
        loginText.text = "Login: " + user.login
        emailText.text = "Email: " + user.email
    }

    //Ввод номера карты
    fun showDialog(view: View) {
        if(DataHolder.haveCard == false) {
            //Вызов диалогового окна
            val builder = AlertDialog.Builder(this)
            val input = EditText(this)
            builder.setView(input)
            builder.setTitle("Введите номер карты")

            //Кнопка добавления карты
            builder.setPositiveButton("Добавить", DialogInterface.OnClickListener { dialog, id ->
                val cardNumberText = input.text.toString()
                if (isValidCard(cardNumberText)) {
                    DataHolder.cardNumber = cardNumberText
                    DataHolder.haveCard = true
                    setCardInf()
                } else Toast.makeText(this, "Не верно задан номер карты", Toast.LENGTH_SHORT).show()
            })

            //Кнопка отмены ввода
            builder.setNegativeButton("Отмена", DialogInterface.OnClickListener { dialog, id ->

            })
            builder.create().show()
        }
    }

    //Вывод информации о карте
    private fun setCardInf(){
        if(DataHolder.haveCard)DataHolder.cardStarus = "${DataHolder.cardNumber.substring(0, 4)}...     Баланс: ${String.format("%.2f", DataHolder.cardBalance)}"
        val cardNumber: TextView = findViewById(R.id.cardNumberText)
        cardNumber.text = DataHolder.cardStarus

        isAuth(DataHolder.haveCard)
    }

    //Включение и выключение галочки
    private fun isAuth(Auth: Boolean){
        var isAuth: ImageView = findViewById(R.id.isAuth)
        if(Auth) {
            isAuth.setImageResource(R.drawable.check_circle_yes)
        }
        else {
            isAuth.setImageResource(R.drawable.check_circle_no)
        }
    }

    //Изменение надписи
    fun IsAuthOff(view: View){
        DataHolder.haveCard = false
        DataHolder.cardStarus = "Нет карты"
        DataHolder.cardBalance = 100.0
        isAuth(DataHolder.haveCard)
        setCardInf()
    }

    fun onClickToMap(view : View){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}