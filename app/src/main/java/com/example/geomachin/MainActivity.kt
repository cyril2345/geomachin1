package com.example.geomachin

import android.app.Activity
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.SeekBar


class MainActivity : Activity() {

    //new approach with GameBuilder
    private lateinit var gameBuilder: GameBuilder
    private lateinit var container: FrameLayout
    private var introMediaPlayer: MediaPlayer? = null
    private var crashMediaPlayer: MediaPlayer? = null
    private var endGameMediaPlayer: MediaPlayer? = null
    private var winMediaPlayer: MediaPlayer? = null
    protected lateinit var audioManager: AudioManager
    private lateinit var gameLoop: GameLoop

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.menu_activity) // Set the content view to menu_activity.xml

        audioManager = getSystemService(AUDIO_SERVICE) as AudioManager // Initialize AudioManager
        setupMediaPlayers() // Initialize media players
        setupUI()           // Setup UI elements and listeners
        introMediaPlayer?.start()
    }


    private fun setupUI() {

        val startButton = findViewById<ImageButton>(R.id.button1)


        // Start Game when the button is clicked
        startButton.setOnClickListener {
            startGame()
        }
    }

     fun startGame() {
        setContentView(R.layout.activity_main)
        container = findViewById(R.id.container)

        gameBuilder = GameBuilder(this)
            .setupContainer(container)
            .build()
            .addPlayer()
            .addObstacles()
            .initializeGameLoop()
            .startGame()

        // Initialize stop button and set its click listener
        val stopButton = findViewById<ImageButton>(R.id.stopButton)
        stopButton.setOnClickListener {
            onStopGame()
        }

        val pauseButton = findViewById<ImageButton>(R.id.pauseButton)
        pauseButton.setOnClickListener {
            onTogglePauseGame()
        }

        // Volume bar to set volume
        val volumeSeekBar2 = findViewById<SeekBar>(R.id.volumeSeekBar2)
        volumeSeekBar2?.apply {
            max = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
            progress = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
            setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                    if (fromUser) {
                        val normalizedVolume = progress.toFloat() / max.toFloat() // Normalize to 0..1
                        gameBuilder.setVolume(normalizedVolume)
                    }
                }

                override fun onStartTrackingTouch(seekBar: SeekBar) {}
                override fun onStopTrackingTouch(seekBar: SeekBar) {}
            })
        }
    }


     fun restartGame() {
        gameBuilder.restartGame()
    }


    private fun onStopGame() { // In case Stop Button is pressed
        showGameOver()
        gameBuilder.stopGame()  // Stop the game loop
        gameBuilder.cleanup()  // Clean up the game view
        setContentView(R.layout.menu_activity)  // Optionally return to the main menu
        val startButton = findViewById<ImageButton>(R.id.button1)
        startButton.setOnClickListener {
           //startGame()
        }
    }


    private fun onTogglePauseGame() { // in case Pause button is pressed
        if (gameBuilder.isPaused()) {
            gameBuilder.resumeGame()  // Clean up the game view
        } else {
            gameBuilder.pauseGame()  // Stop the game loop

        }

    }


    override fun onDestroy() {
        super.onDestroy()
        introMediaPlayer!!.release() // Release MediaPlayer resource on destroy
        crashMediaPlayer?.release()
        //endGameMediaPlayer?.release()
        //winMediaPlayer?.release()
        introMediaPlayer = null
        crashMediaPlayer = null
        //endGameMediaPlayer = null
        //winMediaPlayer = null

    }


    protected fun setupMediaPlayers() {
        introMediaPlayer = MediaPlayer.create(this, R.raw.intro)
        crashMediaPlayer = MediaPlayer.create(this, R.raw.crash)
        endGameMediaPlayer = MediaPlayer.create(this, R.raw.gameover)  // Assume you have an end_game_sound file
        winMediaPlayer = MediaPlayer.create(this, R.raw.win)
    }


    fun playSound(soundType: Int) { // play specific sound based on results
        when (soundType) {
            10 -> crashMediaPlayer?.start()  // Play crash sound
            12 -> winMediaPlayer?.start()  // Play win sound
            13 -> endGameMediaPlayer?.start() // Play end game music

        }
    }

     fun showVictory() { // go to victory screen
        gameBuilder.stopGame()
        gameBuilder.cleanup()
        setContentView(R.layout.victory_activity)
        findViewById<ImageButton>(R.id.button2).setOnClickListener {
            startGame()
        }
    }
     fun showGameOver() { // go to gameover screen
        gameBuilder.stopGame()
        gameBuilder.cleanup()
        // Set the content view to activity_main.xml when the button is clicked
        setContentView(R.layout.gameover_activity)
        findViewById<ImageButton>(R.id.button3).setOnClickListener {
            startGame()
            }


        // Additional cleanup logic if necessary (stop threads, nullify objects, etc.)
    }
}