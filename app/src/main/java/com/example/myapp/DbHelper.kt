package com.example.myapp

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.util.Currency

class DbHelper(val context: Context, val factory: SQLiteDatabase.CursorFactory?) :
    SQLiteOpenHelper(context, "users2", factory, 1) {
    override fun onCreate(db: SQLiteDatabase?) {
        val query = "CREATE TABLE users (id INT PRIMARY KEY, login TEXT, email TEXT, pass TEXT)"
        db!!.execSQL(query)
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS users")
        onCreate(db)
    }

    fun addUser(user: User){
        val values = ContentValues()
        values.put("login", user.login)
        values.put("email", user.email)
        values.put("pass", user.pass)

        val db = this.writableDatabase
        db.insert("users", null, values)
        db.close()
    }

    fun isUserAuth(login: String, pass: String): Boolean {
        val db = this.readableDatabase

        val result = db.rawQuery("SELECT * FROM users WHERE login = '$login' AND pass = '$pass'", null)
        return result.moveToFirst()
    }

    @SuppressLint("Range")
    fun getUser(login: String):User{

        val selectQuery = "SELECT * FROM users WHERE login = '$login'"
        val db = this.readableDatabase

        val cursor: Cursor = db.rawQuery(selectQuery, null)

        val login:String
        val email: String
        val pass: String

        if(cursor.moveToFirst()){
            login = cursor.getString(cursor.getColumnIndex("login"))
            email = cursor.getString(cursor.getColumnIndex("email"))
            pass = cursor.getString(cursor.getColumnIndex("pass"))
            return User(login, email, pass)
        }
        return TODO("Provide the return value")

        db.close()
    }
}