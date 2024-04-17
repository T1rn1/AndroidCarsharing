package com.example.myapp

import android.os.Bundle
import android.os.CountDownTimer
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Chronometer
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomSheetFragment : BottomSheetDialogFragment(){
    private var rideTime: Long = 0
    private var waitTime: Long = 0
    private var isTripStarted = false
    private var isWaiting = false


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottomsheet_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showCarInf()

        val myButton: Button = view.findViewById(R.id.startDrive)
        val chronometer:Chronometer = view.findViewById(R.id.chronometer)
        val pauseButton: Button = view.findViewById(R.id.pauseDriveBtn)
        val waitChronometer:Chronometer = view.findViewById(R.id.waitChronometer)

        //Обработчик кнопки
        myButton.setOnClickListener{
            if(ActivitySecond.DataHolder.haveCard){
                if (isTripStarted) {
                    if(!isWaiting) {
                        //Остановка таймера поездки
                        chronometer.stop()
                        //Вычисление времени езды
                        rideTime = SystemClock.elapsedRealtime() - chronometer.base
                        //Вычитание денег
                        ActivitySecond.DataHolder.cardBalance -= "%.2f".format(rideTime * MainActivity.DataHolder.data.costRoad / 60000).toDouble()
                        Toast.makeText(activity,"Баланс карты: " + String.format("%.2f", ActivitySecond.DataHolder.cardBalance), Toast.LENGTH_SHORT).show()
                        //Скрытие таймера
                        chronometer.visibility = if (!isTripStarted) View.VISIBLE else View.GONE
                    }else {
                        //Завершение из паузы
                        pauseButton.text = "остановить поездку"
                        waitChronometer.stop()
                        waitTime = SystemClock.elapsedRealtime() - waitChronometer.base
                        waitChronometer.base = SystemClock.elapsedRealtime()
                        waitChronometer.visibility = View.INVISIBLE
                        ActivitySecond.DataHolder.cardBalance -= "%.2f".format(waitTime * MainActivity.DataHolder.data.costWaiting / 60000).toDouble()
                        Toast.makeText(activity,"Баланс карты: " + String.format("%.2f", ActivitySecond.DataHolder.cardBalance), Toast.LENGTH_SHORT).show()
                    }
                    pauseButton.visibility = View.INVISIBLE
                    //Текст "Начать поездку"
                    myButton.text = "Начать поездку"
                    isWaiting = false
                    //Изменяемый bottomshettfragment
                    setCancelable(true)
                } else {
                    //Начало таймера поездки
                    chronometer.base = SystemClock.elapsedRealtime()
                    chronometer.start()
                    //Таймер виден
                    chronometer.visibility = if (!isTripStarted) View.VISIBLE else View.GONE
                    //Текст "Завершить поездку"
                    myButton.text = "Завершить поездку"
                    //неизменяемый bottomshettfragment
                    setCancelable(false)
                    //Появление кнопки пауза
                    pauseButton.visibility = View.VISIBLE
                }
                isTripStarted = !isTripStarted
            }
            else {
                Toast.makeText(activity, "Нужно добавть карту", Toast.LENGTH_SHORT).show()
            }
        }

        pauseButton.setOnClickListener {
            if (isWaiting) {
                // Ожидание заканчивается
                waitChronometer.stop()
                waitTime = SystemClock.elapsedRealtime() - waitChronometer.base
                waitChronometer.base = SystemClock.elapsedRealtime()
                ActivitySecond.DataHolder.cardBalance -= "%.2f".format(waitTime * MainActivity.DataHolder.data.costWaiting / 60000).toDouble()
                Toast.makeText(activity, "Баланс карты: " + String.format("%.2f",ActivitySecond.DataHolder.cardBalance), Toast.LENGTH_SHORT).show()

                waitChronometer.visibility = View.INVISIBLE

                pauseButton.text = "Остановить поездку"
                // Начинается отсчет времени в режиме езды
                chronometer.start()
                chronometer.base = SystemClock.elapsedRealtime()
                chronometer.visibility = View.VISIBLE
            } else {
                // Ожидание начинается
                chronometer.stop()
                chronometer.visibility = View.INVISIBLE
                rideTime = SystemClock.elapsedRealtime() - chronometer.base
                chronometer.base = SystemClock.elapsedRealtime()
                //Вычитание денег
                ActivitySecond.DataHolder.cardBalance -= "%.2f".format(rideTime * MainActivity.DataHolder.data.costRoad / 60000).toDouble()
                Toast.makeText(activity, "Баланс карты: " + String.format("%.2f",ActivitySecond.DataHolder.cardBalance), Toast.LENGTH_SHORT).show()

                waitChronometer.base = SystemClock.elapsedRealtime()
                waitChronometer.start()
                waitChronometer.visibility = View.VISIBLE
                pauseButton.text = "Возобновить поездку"
            }
            isWaiting = !isWaiting
        }
    }

    //Отображение данных на экране
    private fun showCarInf(){
        val car: Car = MainActivity.DataHolder.data
        val ModelText = view?.findViewById<TextView>(R.id.ModelView)
        val NumberText = view?.findViewById<TextView>(R.id.NumberView)
        val CostRoad = view?.findViewById<TextView>(R.id.CostOnRoad)
        val CostWaiting = view?.findViewById<TextView>(R.id.CostOnWaiting)
        ModelText?.text = car?.model
        NumberText?.text = car?.number.toString()
        CostRoad?.text = car?.costRoad.toString()
        CostWaiting?.text = car?.costWaiting.toString()
    }
}