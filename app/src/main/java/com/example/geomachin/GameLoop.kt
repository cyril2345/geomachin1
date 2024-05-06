package com.example.geomachin


import android.view.View
import android.widget.FrameLayout
import android.util.Log
import android.widget.TextView
import android.widget.ImageView

class GameLoop(
    //private val activity: Activity,
    private val mainActivity: MainActivity,
    private val game: Game,
    private val playerView: PlayerView,
    private val obstacleViews: List<ObstacleView>) : Thread() {
    private var running = false
    private var paused = false
    private val targetFPS = 80

    fun setRunning(isRunning: Boolean) {
        running = isRunning
    }


    fun pauseGame() {
        synchronized(this) {
            paused = true
        }
    }

    fun resumeGame() {
        synchronized(this) {
            paused = false
            (this as Object).notifyAll()  // Properly notify all waiting threads
        }
    }

    fun isPaused(): Boolean {
        return paused
    }

    private fun updateProgressBar(progress: Float) {
        mainActivity.runOnUiThread {
            val progressBar = mainActivity.findViewById<FrameLayout>(R.id.progress_container)
            val fillView = mainActivity.findViewById<View>(R.id.progress_fill)

            if (progressBar != null && fillView != null) {
                val width = progressBar.width

                if (progress in 0f..100f) {
                    val layoutParams = fillView.layoutParams
                    layoutParams.width = (width * (progress / 100)).toInt()
                    fillView.layoutParams = layoutParams
                } else {
                    Log.e("GameLoop", "Invalid progress value: $progress. Expected a value between 0 and 100.")
                }
            } else {
                Log.e("GameLoop", "Could not find progress bar or fill view.")
            }
        }
    }




    private fun updateLives(lives: Int) {
        mainActivity.runOnUiThread {
            val heartIds = arrayOf(R.id.heart1, R.id.heart2, R.id.heart3, R.id.heart4, R.id.heart5)
            heartIds.forEachIndexed { index, heartId ->
                val heart = mainActivity.findViewById<ImageView>(heartId)
                if (index < lives) {
                    heart.setImageResource(R.drawable.ic_heart_full)
                } else {
                    heart.setImageResource(R.drawable.ic_heart_empty)
                }
            }
        }
    }


    private fun updatePieces() {
        val counter = game.getCoinsResult()
        mainActivity.runOnUiThread {
            val scoreText = mainActivity.findViewById<TextView>(R.id.piece_text)
            scoreText?.text= counter.toString()

        }
    }


    override fun run() {
        var lastUpdateTime = System.nanoTime()
        val updateInterval = 1000000000L / targetFPS  // 1 second / target FPS in nanoseconds
        var flag = 0

        while (running) {
            synchronized(this) {
                while (paused) {
                    try {
                        (this as Object).wait()  // Wait until notified
                    } catch (e: InterruptedException) {
                        Thread.currentThread().interrupt()
                        return
                    }
                }
            }

            val currentTime = System.nanoTime()
            val elapsedTime = currentTime - lastUpdateTime

            if (elapsedTime >= updateInterval) {
                synchronized(playerView) {
                    game.update()


                    updateLives(game.getPlayer().lives) // Example to set lives remaining to 3 // game.PlayerLives()
                    updateProgressBar(game.gameProgress) // SWI Fill the progressbar
                    updatePieces()

                    if (game.collisionCountTriangle >= 3 || game.collisionCountSquare >= 3) {
                        game.getPlayer().loseLife()
                        flag = 10
                        running = false

                        if (!game.getPlayer().isAlive()) { // No life anymore, gameover
                            flag = 13
                            running = false

                        }
                        else {// we still have lives,restart the game

                        }
                    }
                    if (game.gameProgress >= 100) {
                        flag = 12
                        running = false
                    }
                }

                // UI updates
                mainActivity.runOnUiThread {
                    updateLives(game.getPlayer().lives)
                    updateProgressBar(game.gameProgress)

                }

                //rendering player
                playerView.postInvalidate() //remplacer postInvalidate() par update()
                //rendering obstacles
                obstacleViews.forEach { it.postInvalidate() } //remplacer postInvalidate() par update()
                lastUpdateTime = currentTime
            }

            // Sleep a bit if running too fast
            val sleepTime = (lastUpdateTime + updateInterval - System.nanoTime()) / 1000000
            if (sleepTime > 0) {
                try {
                    Thread.sleep(sleepTime)
                } catch (e: InterruptedException) {
                    Thread.currentThread().interrupt()
                    return
                }
            }
        }

        handleGameEnd(flag)
    }




    private fun handleGameEnd(flag: Int) {
        mainActivity.runOnUiThread {
            mainActivity.playSound(flag)  // Trigger appropriate sound playback

            when (flag) {
                10 -> mainActivity.restartGame()  // Lost a life
                12 -> mainActivity.showVictory()  // Victory
                13 -> mainActivity.showGameOver()  // Game over
            }
        }
    }


}
