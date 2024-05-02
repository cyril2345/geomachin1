package com.example.geomachin

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.view.View

class ObstacleView(context: Context, private val obstacle: Obstacle) : View(context) {

    private val paint = Paint()

    override fun onDraw(canvas: Canvas) {
        paint.color = obstacle.color()
        super.onDraw(canvas)
        obstacle.draw(canvas)
    }
}
