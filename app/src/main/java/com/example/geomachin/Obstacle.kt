package com.example.geomachin

import android.graphics.Canvas

enum class ObstacleType {
    SQUARE, TRIANGLE, CIRCLE, TRAMPO
}



interface Obstacle {
    var isVisible: Boolean  // Add visibility property
    abstract val positionY: Float
    abstract val positionX: Float
    val objectType: ObstacleType  // Add this line to include the obstacle type

    fun draw(canvas: Canvas)
    fun update(speed: Float)
    fun checkCollision(player: Player): CollisionType
    fun color(): Int
    fun size(): Int
}
