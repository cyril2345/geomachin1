package com.example.geomachin

// Game.kt
import java.util.*
import android.content.Context


class Game(private val context: Context,private val screenWidth: Int, private val screenHeight: Int) {
    private val player: Player = Player(50f, screenHeight.toFloat(), 0f,3f  ) // Initial player position
    private val obstacles: MutableList<Obstacle> = mutableListOf()
    //private var obstaclesPassed: Int = 0 // SWI Keep track of passed obstacles
    // SWI a variable to store the game progress
    var gameProgress: Float = 0f
        private set
    var collectedPieces: Int = 0
    var collisionCountSquare: Int = 0 // Count of collisions
    var collisionCountTriangle: Int = 0
    var collisionCountCircle: Int = 0
    var collisionCount: Int = 0



    init {
        //generateObstacles()
        resetGame()  // Initialize game components
    }

    fun update() {
        player.update()
        var obstaclesPassed=0
        for (obstacle in obstacles) {
            obstacle.update(player.speed)

            // SWI Consider an obstacle passed if it moves off screen or player passes its x coordinate
            if (player.positionX > obstacle.positionX) {
                obstaclesPassed++
            }

        }
        gameProgress = (obstaclesPassed.toFloat() / (NUM_OBSTACLES+NUM_PIECES)) * 100
        if (gameProgress > 100) gameProgress = 100f // Ensure progress does not exceed 100%



        checkCollisions(player)

    }


    fun resetGame() {
        obstacles.clear()  // Clear existing obstacles
        generateObstacles()  // Re-generate obstacles
        gameProgress = 0f  // Reset game progress
        // Reset other necessary game states here if there are any
        collisionCountSquare = 0 // Count of collisions
        collisionCountTriangle = 0
        collisionCountCircle = 0
        collisionCount = 0
    }


    private fun generateObstacles() {
        val baseInterval = screenWidth / NUM_OBSTACLES
        val spacingFactor = 4 // Augmenter ce facteur pour agrandir l'intervalle
        val interval = (baseInterval * spacingFactor) // Calcul de l'intervalle augmenté
        val prerundistance = 3*interval // comme on veut

        for (i in 0 until NUM_OBSTACLES) {
            val obstacleX = prerundistance + i * interval.toFloat() // Position x calculée pour un espacement plus large
            val amplitude = screenHeight / 2 // amplitude of the wave
            var obstacleY = translateGameYToScreenY(50f, screenHeight) // SWI in percentage

            // rando selection
            val obstacle = when ((0..2).random()) {  // Generates a random number between 0 and 2
                0 -> {
                    val dd =  amplitude.toFloat()*Random().nextFloat()/screenHeight.toFloat()*100f
                    obstacleY = translateGameYToScreenY(dd, screenHeight) // Calculate Y position
                    SquareObstacle(obstacleX, obstacleY) // Create a SquareObstacle
                }
                1 -> {
                    obstacleY = translateGameYToScreenY(0f, screenHeight) // Set Y position for TriangleObstacle
                    TriangleObstacle(obstacleX, obstacleY) // Create a TriangleObstacle
                }
                else -> {
                    val dd = -17f
                    obstacleY = translateGameYToScreenY(dd, screenHeight) // Calculate Y position for TrampoObstacle
                    TrampoObstacle(obstacleX, obstacleY) // Create a TrampoObstacle
                }
            }
            obstacles.add(obstacle)



        }

        for (i in 0 until NUM_PIECES) {
            val amplitude = screenHeight / 2 // amplitude of the wave
            val obstacleX = prerundistance + screenWidth.toFloat()*spacingFactor.toFloat()*Random().nextFloat()
           // var obstacleY = translateGameYToScreenY(amplitude.toFloat()*Random().nextFloat(), screenHeight)
            val dd =  amplitude.toFloat()*Random().nextFloat()/screenHeight.toFloat()*100f
            val obstacleY = translateGameYToScreenY(dd, screenHeight)
            val obstacle = CircleObstacle(context,obstacleX, obstacleY)
            obstacles.add(obstacle)
        }
    }

    private fun checkCollisions(player: Player) {
        for (obstacle in obstacles) {
            val collisionType = obstacle.checkCollision(player)
            if (collisionType == CollisionType.GENERAL_COLLISION )   {
                when (obstacle) {
                    is SquareObstacle -> collisionCountSquare++
                    is TriangleObstacle -> collisionCountTriangle++
                    is CircleObstacle -> {
                        collisionCountCircle++
                        obstacle.isVisible = false // Hide the circle on collision
                    }
                }


            }
            if (collisionType == CollisionType.TOP_COLLISION )   {
                when (obstacle) {
                    is SquareObstacle -> {handleTopCollision(player)}
                    is TrampoObstacle -> {handleTrampoCollision(player)}

                }


            }
        }
    }


    private fun handleTopCollision(player: Player) {
        //  Reset the player's vertical velocity
        player.velocityY = 0f
        // set both to false do we can jump again
        player.isJumping = false
        //player.isFalling = false

        // Maybe adjust the player's position to sit perfectly on top of the obstacle
        //player.positionY = someCalculatedValue
    }


    private fun handleTrampoCollision(player: Player) {
        // Example: Reset the player's vertical velocity
        player.isJumping = true
        player.velocityY = -100f
        // Maybe adjust the player's position to sit perfectly on top of the obstacle
        //player.positionY = someCalculatedValue
    }

    fun getCoinsResult(): Int {
        var counter = 0
        for (obstacle in obstacles) {
            when (obstacle) {
                is CircleObstacle -> {
                    if (!obstacle.isVisible) {counter ++}

                }
            }
        }

        return counter

    }

    fun getPlayer(): Player {
        return player
    }

    fun losePlayerLife() {
        player.loseLife()
    }

    fun isPlayerAlive(): Boolean = player.isAlive()

    fun playerLives(): Int = player.lives



    fun getObstacles(): List<Obstacle> {
        return obstacles
    }

    private fun translateGameYToScreenY(gameY: Float, screenHeight: Int): Float {
        // Scale the game Y coordinate from a range of 0 to 100 back to screen height
        val scaledY = gameY * screenHeight.toFloat() / 100

        // Reverse the Y coordinate to match the screen's coordinate system (flip bottom and top)
        return screenHeight - scaledY
    }


    companion object {
        const val NUM_OBSTACLES = 20
        const val NUM_PIECES = 8

    }
}
