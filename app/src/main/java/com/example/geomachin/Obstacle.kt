package com.example.geomachin

import android.graphics.Canvas
import android.graphics.Color

interface Obstacle {
    abstract val positionY: Float
    abstract val positionX: Float

    fun draw(canvas: Canvas)
    fun update(speed: Float)
    fun checkCollision(player: Player): Boolean
    fun color(): Int
    fun size(): Int
}
