package org.hyperskill.stopwatch

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat

// notification channel
const val CHANNEL_ID = "org.hyperskill"

class MainActivity : AppCompatActivity() {
    // in order to call them from notificate()
    private lateinit var notificationBuilder: NotificationCompat.Builder


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // creamos variables para las views
        val textView = findViewById<TextView>(R.id.textView)
        val start = findViewById<Button>(R.id.startButton)
        val reset = findViewById<Button>(R.id.resetButton)
        val progress = findViewById<ProgressBar>(R.id.progressBar)
        progress.visibility = View.INVISIBLE
        val settings = this.findViewById<Button>(R.id.settingsButton)
        var upperLimit: Int? = null


        // creamos el handler
        val handler = Handler(Looper.getMainLooper())

        fun secondsToFormattedString(seconds: Int): String {
            val min = seconds / 60
            val sec = seconds - min * 60
            return String.format("%02d:%02d", min, sec)
        }

        // declaramos un objeto Runnable con nuestra lógica en una función llamada run
        var seconds = 0
        var isRunning = false
        val updateWatch: Runnable = object : Runnable {
            @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
            override fun run() {
                textView.text = secondsToFormattedString(seconds)
                seconds++

                // after the upper limit is reached, the stopwatch color is changed to red
                // if the number is not set, then there is no upper limit
                if (upperLimit != null && seconds > upperLimit!! && upperLimit!! > 0) {
                    textView.setTextColor(Color.RED)
                    notificate()
                }

                // every second the progress bar changes color
                if (seconds % 2 == 0) {
                    progress.indeterminateTintList = ColorStateList.valueOf(0XFFAC8AFF.toInt())
                } else {
                    progress.indeterminateTintList = ColorStateList.valueOf(0xFF8C5AFF.toInt())
                }
                if (isRunning) {
                    handler.postDelayed(this, 1000)
                    settings.isEnabled = false

                }
            }
        }

        // cuando hacemos click en el botón start, empieza el contador
        start.setOnClickListener {
            if (!isRunning) {
                isRunning = true
                handler.post(updateWatch)
                progress.visibility = View.VISIBLE
                // the settingsButton is enabled only when the stopwatch is not running
                settings.isEnabled = false
            }

        }

        reset.setOnClickListener {
            isRunning = false
            seconds = 0
            textView.text = "00:00"
            textView.setTextColor(Color.BLACK)
            progress.visibility = View.INVISIBLE
            settings.isEnabled = true
        }

        settings.setOnClickListener {
            val contentView = LayoutInflater.from(this).inflate(R.layout.edittext_para_alert, null, false)
            AlertDialog.Builder(this)
                .setTitle("Alert Dialog with custom View!")
                .setView(contentView)
                .setPositiveButton(android.R.string.ok) { _, _ ->
                    upperLimit = contentView.findViewById<EditText>(R.id.upperLimitEditText).text.toString().toInt()
                }
                .setNegativeButton(android.R.string.cancel, null)
                .show()
        }

        // Initialize notification components
        // create NOTIFICATION CHANNEL
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Time reached"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, name, importance)
            // Register the channel with the system
            val notificationManager: NotificationManager =
                this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

        // create notification
        notificationBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_notification_overlay)
            .setContentTitle("Time reached")
            .setContentText("Stop now!")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setOnlyAlertOnce(true)

    }

    fun notificate() {
        val notificationManager = this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notification = notificationBuilder.build()
        notification.flags = notification.flags or Notification.FLAG_INSISTENT
        notificationManager.notify(393939, notification)
    }

    /*
        Tests for android can not guarantee the correctness of solutions that make use of
        mutation on "static" variables to keep state. You should avoid using those.
        Consider "static" as being anything on kotlin that is transpiled to java
        into a static variable. That includes global variables and variables inside
        singletons declared with keyword object, including companion object.
        This limitation is related to the use of JUnit on tests. JUnit re-instantiate all
        instance variable for each test method, but it does not re-instantiate static variables.
        The use of static variable to hold state can lead to state from one test to spill over
        to another test and cause unexpected results.
        Using mutation on static variables to keep state
        is considered a bad practice anyway and no measure
        attempting to give support to that pattern will be made.
     */
}