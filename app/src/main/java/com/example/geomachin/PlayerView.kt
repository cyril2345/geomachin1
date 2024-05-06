package com.example.geomachin
import android.content.Context
import android.graphics.Canvas
import android.view.MotionEvent
import android.view.View

import android.graphics.Bitmap
import android.graphics.BitmapFactory

class PlayerView(context: Context, private val player: Player) : View(context) {


    //old private val paint: Paint = Paint()
    private var playerBitmap: Bitmap

    init {
        //old paint.color = Color.RED // Set player color

        playerBitmap = BitmapFactory.decodeResource(context.resources, R.drawable.face)
        // Resize the bitmap to fit the player size (optional)
        playerBitmap = Bitmap.createScaledBitmap(playerBitmap, Player.WIDTH, Player.HEIGHT, false)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.apply {
            // Draw the player square within the container
            canvas.drawBitmap(playerBitmap, player.positionX, player.positionY, null)

            //old
            //drawRect(
            //    player.positionX,
            //    player.positionY,
            //    player.positionX + Player.WIDTH,
            //    player.positionY + Player.HEIGHT,
            //    paint
            //)

        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                player.jump() // Call jump method when screen is tapped
                invalidate() // Redraw player view
            }
        }
        return true
    }
}
