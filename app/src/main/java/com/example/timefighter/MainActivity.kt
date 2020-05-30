package com.example.timefighter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog

class MainActivity : AppCompatActivity() {

    internal var score:Int = 0
    internal var  gameStarted = false

    internal lateinit var tapMeButton:Button
    internal lateinit var gameScoreTextView:TextView
    internal lateinit var timeLeftTextView:TextView

    internal lateinit var countDownTimer:CountDownTimer
    internal val initialCountDown:Long = 10000
    internal val countDownInterval:Long = 1000
    internal var timeLeftonTimer:Long = 60000

    companion object{
        val TAG = MainActivity::class.java.simpleName
        private const val SCORE_KEY = "SCORE_KEY"
        private const val TIMER_KEY = "TIMER_KEY"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        tapMeButton = findViewById(R.id.tapMeButton)
        gameScoreTextView = findViewById(R.id.gameScoreText)
        timeLeftTextView = findViewById(R.id.timeLeftTextView)


        tapMeButton.setOnClickListener { view ->
            val bounceAnimation = AnimationUtils.loadAnimation(this, R.anim.bounce);
            view.startAnimation(bounceAnimation)
            incrementScore()
        }
       if(savedInstanceState != null){
           score = savedInstanceState.getInt(SCORE_KEY)
           timeLeftonTimer = savedInstanceState.getLong(TIMER_KEY)
           restoreGame()
       }else{
           resetGame()
       }
        Log.d(TAG, "onCreate Score is $score")

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
         super.onCreateOptionsMenu(menu)
            menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)
        if(item.itemId == R.id.actionAbout){
            showMessage()
        }
        return true
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(SCORE_KEY,score)
        outState.putLong(TIMER_KEY,timeLeftonTimer)
        countDownTimer.cancel()

        Log.d(TAG, "onSavedInstanceState: saving Score: $score  and timeleft : $timeLeftonTimer")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy Called")
    }
    private fun incrementScore() {
        if(!gameStarted){
           startGmae()
        }
        score+=1
        val newScore = getString(R.string.yourScore, score)
        gameScoreTextView.text = newScore
    }

    private fun resetGame(){
        score = 0
        gameScoreTextView.text = getString(R.string.yourScore, score)
        val initialTimeLeft = initialCountDown/countDownInterval
        timeLeftTextView.text = getString(R.string.timeLeft, initialTimeLeft)

        countDownTimer = object:CountDownTimer(initialCountDown, countDownInterval){
            override fun onTick(millisUntilFinished: Long) {
                timeLeftonTimer = millisUntilFinished
                val timeLeft = millisUntilFinished/countDownInterval
                timeLeftTextView.text = getString(R.string.timeLeft, timeLeft)
            }

            override fun onFinish() {
                endGame()
            }
        }
        gameStarted = false
    }
    private fun restoreGame(){
        gameScoreTextView.text = getString(R.string.yourScore, score)
        val restoredTime = timeLeftonTimer/countDownInterval
        timeLeftTextView.text = getString(R.string.timeLeft, restoredTime)
        countDownTimer = object : CountDownTimer(timeLeftonTimer, countDownInterval){
            override fun onTick(millisUntilFinished: Long) {
                timeLeftonTimer = millisUntilFinished
                val timeLeft = millisUntilFinished/countDownInterval
                timeLeftTextView.text = getString(R.string.timeLeft, timeLeft)
            }

            override fun onFinish() {
                endGame()
            }

        }
        gameStarted = true
        countDownTimer.start()

    }

    private fun startGmae(){
        countDownTimer.start()
        gameStarted = true
    }
    private fun endGame(){
        Toast.makeText(this, getString(R.string.gameOverMessage, score), Toast.LENGTH_LONG).show()
        resetGame()
    }
    private fun showMessage(){
        val dialogtitle = getString(R.string.aboutTitle, BuildConfig.VERSION_NAME)
        val dialogMessage = getString(R.string.aboutMessage)

        val builder = AlertDialog.Builder(this)
        builder.setTitle(dialogtitle)
        builder.setMessage(dialogMessage)

        builder.create().show()
    }
}
