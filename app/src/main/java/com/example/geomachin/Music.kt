package com.example.geomachin

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri

class Music (private val context: Context, private val musicResId: Int){
    private var mediaPlayer: MediaPlayer? = null

    // Default volume level set to the maximum. It can be set anywhere between 0.0f and 1.0f.
    private var volume: Float = 1.0f

    fun setVolume(level: Float) {
        // Ensure the volume level is within the correct range.
        volume = level.coerceIn(0.0f, 1.0f)
        mediaPlayer?.setVolume(volume, volume)
    }

    fun play() {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(context, musicResId)
            mediaPlayer?.isLooping = true // If you want the music to loop
            mediaPlayer?.setVolume(volume, volume)
        }
        mediaPlayer?.start()// Code to play music
    }

    fun pause() {
        if (mediaPlayer?.isPlaying == true) {
            mediaPlayer?.pause()
        }
    }

    fun stop() {
        mediaPlayer?.stop()
        mediaPlayer?.release() // Release resources immediately
        mediaPlayer = null // Help garbage collector by nullifying the reference
    }
}