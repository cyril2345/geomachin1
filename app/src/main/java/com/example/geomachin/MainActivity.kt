package com.example.geomachin

import android.os.Bundle
import android.widget.FrameLayout
import android.app.Activity
import android.content.res.Resources

class MainActivity : Activity() {

    private lateinit var gameLoop: GameLoop
    private lateinit var playerView: PlayerView
    private lateinit var player: Player
    private val obstacleViews = mutableListOf<ObstacleView>()
    private lateinit var game: Game

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        game = Game(10000, 400)

        // Create PlayerView
        playerView = PlayerView(this, game.getPlayer())

        // Add PlayerView to layout
        val container = findViewById<FrameLayout>(R.id.container)
        container.addView(playerView)

        // Create and add obstacle views
        for (obstacle in game.getObstacles()) {
            val obstacleView = ObstacleView(this, obstacle)
            container.addView(obstacleView)
            obstacleViews.add(obstacleView)
        }

        // Create and start GameLoop
        gameLoop = GameLoop(game, playerView, obstacleViews)
        gameLoop.setRunning(true)
        gameLoop.start()
    }
}
