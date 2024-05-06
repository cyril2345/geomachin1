package com.example.geomachin

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PointF
import kotlin.math.abs
import android.graphics.LinearGradient
import android.graphics.Shader



class TriangleObstacle(override var positionX: Float, override val positionY: Float) : Obstacle {
    override val objectType = ObstacleType.TRIANGLE
    override var isVisible: Boolean = true
    override fun draw(canvas: Canvas) {
        val size = size() //SWI
        val paint = Paint()
        //paint.color = Color.BLUE

        // Gradient paint for the triangle
        val gradient = LinearGradient(
            positionX, positionY + size,
            positionX + size / 2, positionY,
            Color.BLUE, Color.CYAN, Shader.TileMode.CLAMP
        )
        paint.shader = gradient

        val path = Path()
        path.moveTo(positionX + size() / 2, positionY) // Top
        path.lineTo(positionX, positionY + size()) // Bottom-left
        path.lineTo(positionX + size(), positionY + size()) // Bottom-right
        path.lineTo(positionX + size() / 2, positionY) // Back to Top

        path.close()

        canvas.drawPath(path, paint)

        // Draw a slightly offset triangle for a shadow effect
        val shadowPaint = Paint()
        shadowPaint.color = Color.argb(50, 0, 0, 0)  // Semi-transparent black

        val shadowPath = Path()
        shadowPath.moveTo(positionX + size / 2 + 5, positionY + 5) // Shifted top
        shadowPath.lineTo(positionX + 5, positionY + size + 5) // Shifted bottom-left
        shadowPath.lineTo(positionX + size + 5, positionY + size + 5) // Shifted bottom-right
        shadowPath.lineTo(positionX + size / 2 + 5, positionY + 5) // Back to Top

        shadowPath.close()

        canvas.drawPath(shadowPath, shadowPaint)
    }

    override fun update(speed: Float) {
        // Update obstacle position according to player speed
        positionX -= speed
    }

    override fun checkCollision(player: Player): CollisionType {
        val playerPosition = PointF(player.positionX + Player.WIDTH / 2, player.positionY + Player.HEIGHT / 2)

        // Define vertices of the triangle
        val v1 = PointF(positionX, positionY + size()) // Bottom-left
        val v2 = PointF(positionX + size(), positionY + size()) // Bottom-right
        val v3 = PointF(positionX + size() / 2, positionY) // Top

        // Calculate areas of the three triangles formed by the player position and each triangle vertex
        val totalArea = triangleArea(v1, v2, v3)
        val area1 = triangleArea(playerPosition, v2, v3)
        val area2 = triangleArea(v1, playerPosition, v3)
        val area3 = triangleArea(v1, v2, playerPosition)

        // If the sum of the three areas equals the total area, the point is inside the triangle
        if (abs(totalArea - (area1 + area2 + area3)) < 0.0001f) {
            return CollisionType.GENERAL_COLLISION// Adjust epsilon as needed
        }
        else return CollisionType.NO_COLLISION

    }

    override fun color(): Int {
        return Color.BLUE
    }

    override fun size(): Int {
        return 80
    }

    private fun triangleArea(a: PointF, b: PointF, c: PointF): Float {
        return abs((a.x * (b.y - c.y) + b.x * (c.y - a.y) + c.x * (a.y - b.y)) / 2)
    }
}
