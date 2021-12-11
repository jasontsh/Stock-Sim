package com.jasontsh.interviewkickstart.stocksim

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import java.util.concurrent.Executors
import java.util.concurrent.Semaphore
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    var currentVal = StockUtil.STATIC_START_VAL
    val semaphore = Semaphore(1)
    var loggedIn = false
    var enabled = false
    val executor = Executors.newScheduledThreadPool(4)
    var currentIndex = 0
    val maxIndex = 5

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        executor.submit {
            semaphore.acquire()
            while (!loggedIn) {}
            enabled = true
            while (loggedIn) {}
            enabled = false
            semaphore.release()
        }
        val logInButton = findViewById<Button>(R.id.loggedIn)
        logInButton.setOnClickListener {
            semaphore.acquire()
            loggedIn = true
            semaphore.release()
        }
        val logOutButton = findViewById<Button>(R.id.loggedOut)
        logOutButton.setOnClickListener {
            semaphore.acquire()
            loggedIn = false
            semaphore.release()
        }
        val staticStocks = findViewById<TextView>(R.id.static_stocks)
        val staticButton = findViewById<Button>(R.id.static_stocks_button)
        staticButton.setOnClickListener {
            if (!enabled) {
                return@setOnClickListener
            }
            var s = "%.2f".format(currentVal)
            for (i in 0..5) {
                val nextPrice = StockUtil.generateNextStockPrice(currentVal)
                s += ", %.2f".format(nextPrice)
                currentVal = nextPrice
            }
            staticStocks.text = s
        }
        val increaseEd = findViewById<EditText>(R.id.increase_ed)
        val variantEd = findViewById<EditText>(R.id.variant_ed)
        val inflateEd = findViewById<EditText>(R.id.inflate_ed)
        val dynamicStocks = findViewById<TextView>(R.id.dynamic_stocks)
        dynamicStocks.text = "100.0,100.0,100.0,100.0,100.0"
    }
}