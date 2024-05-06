package com.example.geomachin

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.LinearGradient
import android.graphics.Shader


class TrampoObstacle(override var positionX: Float, override val positionY: Float) : Obstacle {
    override val objectType = ObstacleType.TRAMPO
    override var isVisible: Boolean = true
    override fun draw(canvas: Canvas) {
        val size = size()
        val paint = Paint()

        // Create a LinearGradient for the square
        val gradient = LinearGradient(
            positionX, positionY,
            positionX + size, positionY + size,
            Color.YELLOW, Color.argb(255, 0, 255, 0), Shader.TileMode.CLAMP
        )
        //paint.color = Color.GREEN

        paint.shader = gradient

        canvas.drawRect(positionX, positionY, positionX + size(), positionY + size(), paint)

        // Shadow effect
        val shadowPaint = Paint()
        shadowPaint.color = Color.argb(60, 0, 0, 0) // Semi-transparent black for shadow
        val shadowOffset = 5 // Shadow offset in pixels
        canvas.drawRect(
            positionX + shadowOffset,
            positionY + shadowOffset,
            positionX + size + shadowOffset,
            positionY + size + shadowOffset,
            shadowPaint
        )


    }

    override fun update(speed: Float) {
        // Update obstacle position according to player speed
        positionX -= speed
    }

    override fun checkCollision(player: Player): CollisionType {

        val playerRect = RectF(player.positionX, player.positionY, player.positionX + Player.WIDTH, player.positionY + Player.HEIGHT)
        val obstacleRect = RectF(positionX, positionY, positionX + size(), positionY + size())

        if (RectF.intersects(playerRect, obstacleRect)) {

            // Determine the type of collision
            // Check if player's bottom is colliding with the top of the obstacle
            if (playerRect.bottom >= obstacleRect.top && playerRect.bottom <= obstacleRect.top + 15  ) { // && player.isJumping 10 can be adjusted based on the speed and frame rate attention jumping and falling sont inversés
                if (true) { //playerRect.right > obstacleRect.left && playerRect.left < obstacleRect.right
                    // Player is colliding with the top of the obstacle
                    return CollisionType.TOP_COLLISION
                }

            }
            return CollisionType.GENERAL_COLLISION
        }
        return CollisionType.NO_COLLISION

    }

    override fun color(): Int {
        return Color.YELLOW
    }

    override fun size(): Int {
        return 80
    }
}
