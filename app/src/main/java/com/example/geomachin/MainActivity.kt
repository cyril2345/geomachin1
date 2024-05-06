package com.example.geomachin

//import android.R

import android.app.Activity
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.SeekBar


class MainActivity : Activity() {

    private lateinit var gameLoop: GameLoop
    private lateinit var playerView: PlayerView
    private lateinit var player: Player
    private var obstacleViews = mutableListOf<ObstacleView>()
    private lateinit var game: Game
    private var mediaPlayer: MediaPlayer? = null
    private lateinit var audioManager: AudioManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.menu_activity) // Set the content view to menu_activity.xml

        // Initialize AudioManager
        audioManager = getSystemService(AUDIO_SERVICE) as AudioManager

        // Create MediaPlayer instance
        mediaPlayer = MediaPlayer.create(this, R.raw.xstepmusic).apply {
            isLooping = true  // Set looping
            setVolume(1.0f, 1.0f)  // Set initial volume
            start()  // Start playback
        }


        // Initialize the game
        //game = Game(this,2000, 400)

        val startButton = findViewById<ImageButton>(R.id.button1)
        val soundOnButton = findViewById<ImageButton>(R.id.buttonsoundon)
        val soundOffButton = findViewById<ImageButton>(R.id.buttonsoundoff)
        val volumeSeekBar = findViewById<SeekBar>(R.id.volumeSeekBar);


        // Start Game when the button is clicked
        startButton.setOnClickListener {
           startGame()
        }

        // Start Game when the button is clicked
        soundOnButton.setOnClickListener {
            mediaPlayer?.setVolume(1.0f, 1.0f); // Set volume to full
        }


        // Start Game when the button is clicked
        soundOffButton.setOnClickListener {
            mediaPlayer?.setVolume(0f, 0f); // Mute the media player
        }

        // Volume bar to set volume
        volumeSeekBar?.apply {
            max = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
            progress = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
            setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                    if (fromUser) {
                        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0)
                    }
                }

                override fun onStartTrackingTouch(seekBar: SeekBar) {}
                override fun onStopTrackingTouch(seekBar: SeekBar) {}
            })
        }





        }

    public fun startGame() {
        // Set the content view to activity_main.xml when the button is clicked
        setContentView(R.layout.activity_main)

        // Initialize the game
        game = Game(this,2000, 400)

        // Create PlayerView
        playerView = PlayerView(this, game.getPlayer())

        // Cleanup before setting up a new game
        //cleanup()//to test

        // Add PlayerView to layout
        val container = findViewById<FrameLayout>(R.id.container)
        container.addView(playerView)


        // obstacleViews = mutableListOf() // to test Reset the obstacleViews list

        // Create and add obstacle views
        for (obstacle in game.getObstacles()) {
            val obstacleView = ObstacleView(this, obstacle)
            container.addView(obstacleView)
            obstacleViews.add(obstacleView)
        }

        // Create and start GameLoop
        gameLoop = GameLoop(this,game, playerView, obstacleViews)
        gameLoop.setRunning(true)
        gameLoop.start()


        // Initialize stop button and set its click listener
        val stopButton = findViewById<ImageButton>(R.id.stopButton)
        stopButton.setOnClickListener {
            stopGame()
        }
    }


    public fun restartGame() {
        // Ensure the current game loop is stopped properly
        if (::gameLoop.isInitialized) {
            gameLoop.setRunning(false)
            try {
                gameLoop.join()  // Wait for the game loop thread to terminate
            } catch (e: InterruptedException) {
                Thread.currentThread().interrupt() // Re-interrupt if interrupted during join
            }
        }

        // Optionally reset the game state without reinitializing everything
        game.resetGame()  // Assuming `resetGame()` method in `Game` class resets only necessary components

        // Clear existing obstacle views
        val container = findViewById<FrameLayout>(R.id.container)
        container.removeAllViews()

        // Reinitialize views and components
        playerView = PlayerView(this, game.getPlayer())
        container.addView(playerView)

        obstacleViews.clear()
        for (obstacle in game.getObstacles()) {
            val obstacleView = ObstacleView(this, obstacle)
            container.addView(obstacleView)
            obstacleViews.add(obstacleView)
        }



        // Restart the GameLoop
        gameLoop = GameLoop(this, game, playerView, obstacleViews)
        gameLoop.setRunning(true)
        gameLoop.start()
    }


    override fun onPause() {
        super.onPause()
        if (mediaPlayer!!.isPlaying) {
            mediaPlayer!!.pause() // Pause when the activity is not in the foreground
        }
    }

    override fun onResume() {
        super.onResume()
        if (!mediaPlayer!!.isPlaying) {
            mediaPlayer!!.start() // Resume playback when the activity comes into the foreground
        }
    }


    public fun stopGame() {
        // Signal the game loop to stop
        if (::gameLoop.isInitialized && gameLoop.isAlive) {
            gameLoop.setRunning(false)
        }

        // showGameOver()

        // Proceed to clear views and update UI without waiting for the thread to join
        runOnUiThread {
            val container = findViewById<FrameLayout>(R.id.container)
            container.removeAllViews()
            setContentView(R.layout.menu_activity)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer!!.release() // Release MediaPlayer resource on destroy
        mediaPlayer = null
    }


    public fun showGameOver() {
        // Set the content view to activity_main.xml when the button is clicked
        setContentView(R.layout.gameover_activity)
        findViewById<ImageButton>(R.id.button3).setOnClickListener {
            startGame()
            }


        // Additional cleanup logic if necessary (stop threads, nullify objects, etc.)
    }
}