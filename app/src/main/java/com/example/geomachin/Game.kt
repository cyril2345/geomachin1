package com.example.geomachin

// Game.kt
import android.graphics.RectF
import java.util.*

class Game(private val screenWidth: Int, private val screenHeight: Int) {
    private val player: Player = Player(50f, screenHeight.toFloat(), 3f) // Initial player position
    private val obstacles: MutableList<Obstacle> = mutableListOf()

    init {
        generateObstacles()
    }

    fun update() {
        player.update()
        for (obstacle in obstacles) {
            obstacle.update(player.speed)
        }
        checkCollisions(player)
    }

    private fun generateObstacles() {
        val baseInterval = screenWidth / NUM_OBSTACLES
        val spacingFactor = 8 // Augmenter cet facteur pour agrandir l'intervalle
        val interval = (baseInterval * spacingFactor).toInt() // Calcul de l'intervalle augmenté

        for (i in 0 until NUM_OBSTACLES) {
            val obstacleX = i * interval.toFloat() // Position x calculée pour un espacement plus large
            val obstacleY = (screenHeight - 5).toFloat() // Positionne à la coordonnée y la plus basse du screen

            val obstacle = if (Random().nextBoolean()) {
                SquareObstacle(obstacleX, obstacleY)
            } else {
                TriangleObstacle(obstacleX, obstacleY)
            }
            obstacles.add(obstacle)
        }
    }

    fun checkCollisions(player: Player) {
        for (obstacle in obstacles) {
            val collides = obstacle.checkCollision(player)
            if (collides) {
                println("collision")
            }
        }
    }

    fun getPlayer(): Player {
        return player
    }

    fun getObstacles(): List<Obstacle> {
        return obstacles
    }

    companion object {
        const val NUM_OBSTACLES = 100
    }
}
