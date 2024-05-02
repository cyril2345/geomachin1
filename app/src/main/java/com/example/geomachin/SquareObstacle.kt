package com.example.geomachin

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF

class SquareObstacle(override var positionX: Float, override val positionY: Float) : Obstacle {

    override fun draw(canvas: Canvas) {
        val paint = Paint()
        paint.color = Color.GREEN
        canvas.drawRect(positionX, positionY, positionX + size(), positionY + size(), paint)
    }

    override fun update(speed: Float) {
        // Update obstacle position according to player speed
        positionX -= speed
    }

    override fun checkCollision(player: Player): Boolean {
        val playerRect = RectF(player.positionX, player.positionY, player.positionX + Player.WIDTH, player.positionY + Player.HEIGHT)
        val obstacleRect = RectF(positionX, positionY, positionX + size(), positionY + size())
        return RectF.intersects(playerRect, obstacleRect)
    }

    override fun color(): Int {
        return Color.GREEN
    }

    override fun size(): Int {
        return 150
    }
}
