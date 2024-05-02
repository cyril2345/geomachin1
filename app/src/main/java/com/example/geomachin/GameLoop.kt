package com.example.geomachin

class GameLoop(
    private val game: Game,
    private val playerView: PlayerView,
    private val obstacleViews: List<ObstacleView>) : Thread() {

    private var running = false
    private val targetFPS = 60

    fun setRunning(isRunning: Boolean) {
        running = isRunning
    }

    override fun run() {
        var startTime: Long
        var timeMillis: Long
        var waitTime: Long
        val targetTime = (500 / targetFPS).toLong()

        while (running) {
            startTime = System.nanoTime()

            synchronized(playerView) {
                game.update()
                playerView.postInvalidate() // Redraw the player view on UI thread
                obstacleViews.forEach { it.postInvalidate() } // Redraw all obstacle views on UI thread
            }

            timeMillis = (System.nanoTime() - startTime) / 1000000
            waitTime = targetTime - timeMillis

            try {
                if (waitTime > 0) {
                    sleep(waitTime)
                }
            } catch (e: InterruptedException) {
                // Handle interruption
            }
        }
    }
}
