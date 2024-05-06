package com.example.geomachin

// Player.kt
class Player(
    var positionX: Float,
    var positionY: Float,
    var velocityY: Float,
    var speed: Float,
    var isJumping: Boolean = false,
    var isFalling: Boolean = false,
    var lives: Int = 5  // Default number of lives set to 5
    ) {



    fun update() {
        //new
        val deltaTime =0.1f

        //positionX += speed
        if (isJumping || isFalling) {
            // Update velocity with gravity
            velocityY += GRAVITY * deltaTime

            // Update position with the new velocity
            positionY += velocityY * deltaTime

            // Check if the player has hit the ceiling
            if (positionY <= 0) {
                positionY = 0f
                velocityY = 0f
                isJumping = false
                isFalling = true
            }
            // Check if the player has hit the floor
            if (positionY >= 400) {
                positionY = 400f
                isJumping = false
                isFalling = false
            }
        }

    }



    fun jump() {
        // Make the player jump
        if (!isJumping and !isFalling) {
            isJumping = true
            //positionY -= JUMP_FORCE
            velocityY = -JUMP_VELOCITY  // This is the initial jump force; adjust as necessary for your game
        }
    }

    fun loseLife() {
        if (lives > 0) lives--
    }

    fun addLife() {
        lives++  // Optional: Ability to increase lives, if game mechanics require
    }


    fun resetLives(newLives: Int = 5) {
        lives = newLives
    }

    fun isAlive(): Boolean = lives > 0

    fun translateGameYToScreenY(gameY: Float, screenHeight: Int): Float {
        // Scale the game Y coordinate from a range of 0 to 100 back to screen height
        val scaledY = gameY * screenHeight.toFloat() / 100

        // Reverse the Y coordinate to match the screen's coordinate system (flip bottom and top)
        return screenHeight - scaledY
    }


    companion object {
        const val WIDTH = 80
        const val HEIGHT = 80
        const val GRAVITY = 12f
        const val JUMP_VELOCITY = 80f
        const val JUMP_HEIGHT = 300f
    }
}
