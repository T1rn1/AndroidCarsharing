package com.example.myapp

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.graphics.BitmapFactory
import java.lang.Exception

class DbCar(val context: Context, val factory: SQLiteDatabase.CursorFactory?) :
    SQLiteOpenHelper(context, "carrs", factory, 1) {
    override fun onCreate(db: SQLiteDatabase?) {
        val query = "CREATE TABLE cars (id TEXT, model TEXT, number INT, costRoad DOUBLE, costWaiting Double, fuel INT)"
        db!!.execSQL(query)
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS cars")
        onCreate(db)
    }

    fun addCar(car: Car){
        val values = ContentValues()
        values.put("id", car.id)
        values.put("model", car.model)
        values.put("number", car.number)
        values.put("costRoad", car.costRoad)
        values.put("costWaiting",car.costWaiting)
        values.put("fuel", car.fuel)

        val db = this.writableDatabase
        db.insert("cars", null, values)
        db.close()
    }

    @SuppressLint("Range")
    fun getCar(id: String):Car{
        val selectQuery = "SELECT * FROM cars WHERE id = '$id'"
        val db = this.readableDatabase

        val cursor: Cursor = db.rawQuery(selectQuery,null)

        val model: String
        val number: Int
        val costRoad: Double
        val costWaiting: Double
        val fuel:Int

        if(cursor!!.moveToFirst()){
            model = cursor!!.getString(cursor!!.getColumnIndex("model"))
            number = cursor!!.getInt(cursor!!.getColumnIndex("number"))
            costRoad = cursor!!.getDouble(cursor!!.getColumnIndex("costRoad"))
            costWaiting = cursor!!.getDouble(cursor!!.getColumnIndex("costWaiting"))
            fuel = cursor!!.getInt(cursor!!.getColumnIndex("fuel"))
            return Car(id,model,number,costRoad,costWaiting,fuel)
        }
        return TODO("Provide the return value")

        db.close()
    }
}
