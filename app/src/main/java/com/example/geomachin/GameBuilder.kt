package com.example.geomachin


import android.widget.FrameLayout


class GameBuilder(private val activity: MainActivity) {
    private lateinit var game: Game
    private lateinit var playerView: PlayerView
    private var obstacleViews = mutableListOf<ObstacleView>()
    private lateinit var gameLoop: GameLoop
    private lateinit var container: FrameLayout
    private lateinit var music: Music


    fun setupContainer(container: FrameLayout): GameBuilder {
        this.container = container
        return this
    }
    fun build(): GameBuilder {
        game = Game(activity, 2000, 400)  // Setup game with example dimensions
        music = Music(activity, R.raw.xstepmusic) // Initialize music
        return this
    }

    fun addPlayer(): GameBuilder {
        playerView = PlayerView(activity, game.getPlayer())
        return this
    }

    fun addObstacles(): GameBuilder {
        obstacleViews.clear() // Clear previous obstacles if any
        for (obstacle in game.getObstacles()) {
            val obstacleView = ObstacleView(activity, obstacle)
            obstacleViews.add(obstacleView)
        }
        return this
    }

    fun initializeGameLoop(): GameBuilder {
        gameLoop = GameLoop(activity, game, playerView, obstacleViews)
        return this
    }

    fun startGame(): GameBuilder {
        container.addView(playerView)
        obstacleViews.forEach { container.addView(it) }
        gameLoop.setRunning(true)
        gameLoop.start()
        music.play() // start playing music

        return this
    }

    fun stopGame(): GameBuilder {
        gameLoop.setRunning(false)
        music.stop()  // Stop the music
        return this
    }

    fun cleanup(): GameBuilder {
        container.removeAllViews()
        music.stop() // Stop and release music when activity is destroyed
        return this
    }

    fun restartGame(): GameBuilder {
        var player = game.getPlayer() // astuce, peut faire mieux dans la reinitialisation
        val remaininglife = player.lives
        stopGame()
        cleanup()
        build()
        addPlayer()
        player = game.getPlayer()
        player.lives = remaininglife
        player.lives = remaininglife
        addObstacles()
        initializeGameLoop()
        startGame()
        return this
    }

    fun pauseGame() {
        gameLoop.pauseGame()
        music.pause()
    }

    fun resumeGame() {
        gameLoop.resumeGame()
        music.play()
    }

    fun setVolume(level: Float) {
        music.setVolume(level) // set the volume
    }

    fun isPaused(): Boolean {
        return gameLoop.isPaused()
    }
}