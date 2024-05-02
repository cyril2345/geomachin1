package com.example.geomachin

// Player.kt
class Player(
    var positionX: Float,
    var positionY: Float,
    var speed: Float,
    var isJumping: Boolean = false,
    var isFalling: Boolean = false,
    ) {

    fun update() {
        // Update player position based on speed
//        positionX += speed
        // Add gravity if jumping
        if (isJumping) {
            positionY -= JUMP_FORCE
            if (positionY <= 0) {
                positionY = 0f
                isJumping = false
                isFalling = true
            }
        } else if (isFalling) {
            positionY += GRAVITY
            if (positionY >= JUMP_HEIGHT) {
                positionY = JUMP_HEIGHT
                isJumping = false
                isFalling = false
            }
        }
    }


    fun jump() {
        // Make the player jump
        if (!isJumping and !isFalling) {
            isJumping = true
            positionY -= JUMP_FORCE
        }
    }

    companion object {
        const val WIDTH = 150
        const val HEIGHT = 150
        const val GRAVITY = 10f
        const val JUMP_FORCE = 10f
        const val JUMP_HEIGHT = 400f
    }
}
