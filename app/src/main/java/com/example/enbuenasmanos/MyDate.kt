package com.example.enbuenasmanos

import android.app.Application
import android.text.format.DateFormat
import java.util.*

class MyDate: Application() {
    override fun onCreate() {
        super.onCreate()
    }

    companion object{
        //metodo estatico para convertir una marca de tiempo en formato de fehca para que podamos usarlo en todas partes del proyecto sin volver a escribir
        fun formatTimeStamp(timestamp: Long) : String{
            val cal = Calendar.getInstance(Locale.ENGLISH)
            cal.timeInMillis = timestamp
            //formato dd/MM/yyy

            return  DateFormat.format("dd/MM/yyyy", cal).toString()

         }

    }
}