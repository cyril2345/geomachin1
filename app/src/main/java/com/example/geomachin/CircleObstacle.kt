package com.example.geomachin

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.RectF


class CircleObstacle(
    context: Context,
    override var positionX: Float,
    override val positionY: Float
) : Obstacle {
    override val objectType = ObstacleType.CIRCLE
    override var isVisible: Boolean = true  // All obstacles are visible by default
    private var pieceBitmap: Bitmap


    init {
        //old paint.color = Color.RED // Set player color

        pieceBitmap = BitmapFactory.decodeResource(context.resources, R.drawable.piece)
        // Resize the bitmap to fit the player size (optional)
        pieceBitmap = Bitmap.createScaledBitmap(pieceBitmap, 80, 80, false)
    }



    override fun draw(canvas: Canvas) {

        if (isVisible) {
            val radius = size() / 2f // Calculate radius as half the size for the circle

            // Calculate top left corner to center the image on the circle's center
            val left = positionX + radius - pieceBitmap.width / 2f
            val top = positionY + radius - pieceBitmap.height / 2f

            // Draw the bitmap instead of the circle
            canvas.drawBitmap(pieceBitmap, left, top, null)
        }

    }


    override fun update(speed: Float) {
        // Update obstacle position according to player speed
        positionX -= speed
    }

    override fun checkCollision(player: Player): CollisionType  {
        // Circle collision detection
        val playerRect = RectF(player.positionX, player.positionY, player.positionX + Player.WIDTH, player.positionY + Player.HEIGHT)
        val obstacleRect = RectF(positionX, positionY, positionX + pieceBitmap.width, positionY + pieceBitmap.height)
        if (RectF.intersects(playerRect, obstacleRect)) {
            return CollisionType.GENERAL_COLLISION
        }
        else {
            return CollisionType.NO_COLLISION
        }
    }

    override fun color(): Int {
        return Color.YELLOW
    }

    override fun size(): Int {
        return 80
    }
}
